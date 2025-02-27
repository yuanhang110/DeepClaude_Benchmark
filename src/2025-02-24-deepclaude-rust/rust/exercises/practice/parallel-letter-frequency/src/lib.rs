use std::collections::HashMap;

pub fn frequency(input: &[&str], worker_count: usize) -> HashMap<char, usize> {
    let owned_strings: Vec<String> = input.iter().map(|s| s.to_string()).collect();
    let worker_count = if worker_count == 0 { 1 } else { worker_count };
    let mut worker_strings = vec![vec![]; worker_count];
    for (i, s) in owned_strings.into_iter().enumerate() {
        let idx = i % worker_count;
        worker_strings[idx].push(s);
    }
    let handles: Vec<_> = worker_strings
        .into_iter()
        .map(|strings| {
            std::thread::spawn(move || {
                let mut counts = HashMap::new();
                for s in strings {
                    for c in s.chars().filter(|c| c.is_alphabetic()) {
                        for lower_c in c.to_lowercase() {
                            *counts.entry(lower_c).or_insert(0) += 1;
                        }
                    }
                }
                counts
            })
        })
        .collect();
    let mut total_counts = HashMap::new();
    for handle in handles {
        let counts = handle.join().unwrap();
        for (c, count) in counts {
            *total_counts.entry(c).or_insert(0) += count;
        }
    }
    total_counts
}
