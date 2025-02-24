use std::collections::HashMap;
use std::sync::mpsc;
use std::thread;

pub fn frequency(input: &[&str], worker_count: usize) -> HashMap<char, usize> {
    if input.is_empty() {
        return HashMap::new();
    }

    // Calculate chunk size, ensuring all texts are distributed
    let chunk_size = (input.len() + worker_count - 1) / worker_count;
    let (tx, rx) = mpsc::channel();

    // Spawn threads to process chunks
    for chunk_start in (0..input.len()).step_by(chunk_size) {
        let chunk_end = (chunk_start + chunk_size).min(input.len());
        let chunk = input[chunk_start..chunk_end].to_vec();
        let tx = tx.clone();

        thread::spawn(move || {
            let mut local_counts: HashMap<char, usize> = HashMap::new();
            
            for text in chunk {
                for c in text.chars() {
                    if c.is_alphabetic() {
                        // Process each lowercase variant of the character
                        for lower_c in c.to_lowercase() {
                            *local_counts.entry(lower_c).or_insert(0) += 1;
                        }
                    }
                }
            }
            
            tx.send(local_counts).unwrap();
        });
    }

    // Drop the original sender so rx.recv() can complete when all threads are done
    drop(tx);

    // Collect and merge results from all threads
    let mut final_counts = HashMap::new();
    while let Ok(local_counts) = rx.recv() {
        for (c, count) in local_counts {
            *final_counts.entry(c).or_insert(0) += count;
        }
    }

    final_counts
}
