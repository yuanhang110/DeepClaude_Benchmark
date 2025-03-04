use std::collections::HashMap;
use std::sync::{Arc, Mutex};
use std::thread;

pub fn frequency(input: &[&str], worker_count: usize) -> HashMap<char, usize> {
    // Combine all input strings into one for easier chunking
    let combined: String = input.join("");
    let len = combined.len();
    
    // If empty input or single worker, do sequential count
    if len == 0 || worker_count == 1 {
        return sequential_count(&combined);
    }

    // Create shared result map
    let result = Arc::new(Mutex::new(HashMap::<char, usize>::new()));
    
    // Calculate chunk size
    let chunk_size = (len / worker_count).max(1);
    
    let mut handles = vec![];

    // Use char indices to ensure we split on character boundaries
    let char_indices: Vec<(usize, char)> = combined.char_indices().collect();
    let chars_per_chunk = char_indices.len() / worker_count;
    
    for i in 0..worker_count {
        let start_index = i * chars_per_chunk;
        let end_index = if i == worker_count - 1 {
            char_indices.len()
        } else {
            (i + 1) * chars_per_chunk
        };
        
        // Get slice of the combined string using character boundaries
        let chunk = if start_index >= char_indices.len() {
            String::new()
        } else {
            let start = char_indices[start_index].0;
            let end = if end_index >= char_indices.len() {
                combined.len()
            } else {
                char_indices[end_index].0
            };
            combined[start..end].to_string()
        };
        let result = Arc::clone(&result);
        
        // Spawn thread to process chunk
        let handle = thread::spawn(move || {
            let mut local_map = HashMap::new();
            
            // Count letters in chunk
            for c in chunk.chars().filter(|c| c.is_alphabetic()) {
                *local_map.entry(c.to_ascii_lowercase()).or_insert(0) += 1;
            }
            
            // Merge results into shared map
            let mut global_map = result.lock().unwrap();
            for (k, v) in local_map {
                *global_map.entry(k).or_insert(0) += v;
            }
        });
        
        handles.push(handle);
    }

    // Wait for all threads to finish
    for handle in handles {
        handle.join().unwrap();
    }

    // Return the final result
    Arc::try_unwrap(result).unwrap().into_inner().unwrap()
}

fn sequential_count(input: &str) -> HashMap<char, usize> {
    let mut map = HashMap::new();
    for c in input.chars().filter(|c| c.is_alphabetic()) {
        *map.entry(c.to_ascii_lowercase()).or_insert(0) += 1;
    }
    map
}
