use std::collections::HashMap;

pub fn lowest_price(books: &[u32]) -> u32 {
    let mut counts = [0; 5];
    for &book in books {
        let index = (book - 1) as usize;
        counts[index] += 1;
    }
    counts.sort_unstable_by(|a, b| b.cmp(a));
    let mut cache = HashMap::new();
    compute_min_cost(counts, &mut cache)
}

fn compute_min_cost(counts: [u32; 5], cache: &mut HashMap<[u32; 5], u32>) -> u32 {
    if let Some(&cached) = cache.get(&counts) {
        return cached;
    }

    let total: u32 = counts.iter().sum();
    if total == 0 {
        return 0;
    }

    let mut min_cost = u32::MAX;

    for group_size in 1..=5 {
        if can_form_group(&counts, group_size) {
            let mut new_counts = counts;
            for i in 0..group_size {
                new_counts[i] -= 1;
            }
            new_counts.sort_unstable_by(|a, b| b.cmp(a));
            let cost = compute_min_cost(new_counts, cache) + calculate_group_cost(group_size);
            if cost < min_cost {
                min_cost = cost;
            }
        }
    }

    cache.insert(counts, min_cost);
    min_cost
}

fn can_form_group(counts: &[u32; 5], group_size: usize) -> bool {
    if group_size < 1 || group_size > 5 {
        return false;
    }
    counts[group_size - 1] > 0
}

fn calculate_group_cost(group_size: usize) -> u32 {
    match group_size {
        1 => 800,
        2 => 1520,
        3 => 2160,
        4 => 2560,
        5 => 3000,
        _ => 0,
    }
}
