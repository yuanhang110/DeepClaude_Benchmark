use std::collections::HashMap;
use std::thread;

pub fn frequency(input: &[&str], worker_count: usize) -> HashMap<char, usize> {
    if input.is_empty() {
        return HashMap::new();
    }

    let worker_count = worker_count.max(1);
    let chunk_size = (input.len() + worker_count - 1) / worker_count;

    thread::scope(|s| {
        let mut handles = vec![];

        for chunk in input.chunks(chunk_size) {
            let handle = s.spawn(move || {
                let mut counts = HashMap::new();
                for text in chunk {
                    for c in text.chars() {
                        if c.is_alphabetic() {
                            for lower_c in c.to_lowercase() {
                                *counts.entry(lower_c).or_insert(0) += 1;
                            }
                        }
                    }
                }
                counts
            });
            handles.push(handle);
        }

        let mut result = HashMap::new();
        for handle in handles {
            let counts = handle.join().unwrap();
            for (c, count) in counts {
                *result.entry(c).or_insert(0) += count;
            }
        }

        result
    })
}
