use std::collections::HashMap;
use std::thread;

pub fn frequency(input: &[&str], worker_count: usize) -> HashMap<char, usize> {
    if input.is_empty() {
        return HashMap::new();
    }

    let chunk_size = (input.len() + worker_count - 1) / worker_count;
    let chunks = input.chunks(chunk_size);

    thread::scope(|s| {
        let mut handles = Vec::new();
        for chunk in chunks {
            let handle = s.spawn(move || {
                let mut count_map = HashMap::new();
                for text in chunk {
                    for c in text.chars() {
                        if c.is_alphabetic() {
                            for lower_c in c.to_lowercase() {
                                *count_map.entry(lower_c).or_insert(0) += 1;
                            }
                        }
                    }
                }
                count_map
            });
            handles.push(handle);
        }

        let mut result = HashMap::new();
        for handle in handles {
            let partial_map = handle.join().unwrap();
            for (c, count) in partial_map {
                *result.entry(c).or_insert(0) += count;
            }
        }
        result
    })
}
