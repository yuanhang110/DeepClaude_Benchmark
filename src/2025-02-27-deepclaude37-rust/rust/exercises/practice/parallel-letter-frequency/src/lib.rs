use std::collections::HashMap;
use std::sync::{mpsc, Arc};
use std::thread;

pub fn frequency(input: &[&str], worker_count: usize) -> HashMap<char, usize> {
    // Handle empty input case early
    if input.is_empty() {
        return HashMap::new();
    }

    // Ensure at least 1 worker to avoid division by zero
    let worker_count = worker_count.max(1);
    
    // Convert input strings to owned Strings for thread safety
    let texts: Vec<String> = input.iter().map(|s| s.to_string()).collect();
    let texts = Arc::new(texts);
    
    // Create channel for workers to send their results
    let (tx, rx) = mpsc::channel();
    
    // Calculate chunk size for dividing work
    let chunk_size = (texts.len() + worker_count - 1) / worker_count;

    // Spawn worker threads
    for i in 0..worker_count {
        let start = i * chunk_size;
        let end = (i + 1) * chunk_size;
        let end = end.min(texts.len());
        
        // Skip spawning threads with no work
        if start >= texts.len() {
            // Still send an empty result to maintain expected message count
            tx.send(HashMap::new()).unwrap();
            continue;
        }
        
        let texts_clone = Arc::clone(&texts);
        let tx_clone = tx.clone();
        
        thread::spawn(move || {
            let mut counts = HashMap::new();
            for text in &texts_clone[start..end] {
                for c in text.chars() {
                    if c.is_alphabetic() {
                        *counts.entry(c.to_ascii_lowercase()).or_insert(0) += 1;
                    }
                }
            }
            tx_clone.send(counts).unwrap();
        });
    }

    // Drop the original sender to avoid deadlock
    drop(tx);
    
    // Collect and merge results from all workers
    let mut total = HashMap::new();
    for _ in 0..worker_count {
        if let Ok(counts) = rx.recv() {
            for (c, n) in counts {
                *total.entry(c).or_insert(0) += n;
            }
        }
    }

    total
}
