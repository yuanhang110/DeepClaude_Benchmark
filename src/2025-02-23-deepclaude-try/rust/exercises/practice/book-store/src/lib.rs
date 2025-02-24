use std::collections::HashMap;

pub fn lowest_price(books: &[u32]) -> u32 {
    let mut counts = [0; 5];
    for &book in books {
        if book >= 1 && book <= 5 {
            counts[(book - 1) as usize] += 1;
        }
    }
    let mut counts_vec = counts.to_vec();
    counts_vec.sort_unstable_by(|a, b| b.cmp(a));
    let mut cache = HashMap::new();
    compute_min_cost(&counts_vec, &mut cache)
}

fn compute_min_cost(counts: &Vec<u32>, cache: &mut HashMap<Vec<u32>, u32>) -> u32 {
    if counts.iter().all(|&c| c == 0) {
        return 0;
    }
    
    if let Some(&cached) = cache.get(counts) {
        return cached;
    }
    
    let mut min_cost = u32::MAX;
    for k in (1..=5).rev() {
        // Check if we can form a group of size `k`
        if counts.len() < k || counts[0..k].iter().any(|&c| c == 0) {
            continue;
        }
        
        // Create new counts state
        let mut new_counts = counts.clone();
        for i in 0..k {
            new_counts[i] -= 1;
        }
        new_counts.sort_unstable_by(|a, b| b.cmp(a));
        
        // Calculate cost recursively
        let cost = compute_min_cost(&new_counts, cache) + calculate_group_cost(k);
        if cost < min_cost {
            min_cost = cost;
        }
    }
    
    cache.insert(counts.clone(), min_cost);
    min_cost
}

fn calculate_group_cost(k: usize) -> u32 {
    match k {
        1 => 800,                    // No discount
        2 => 2 * 800 * 95 / 100,    // 5% discount
        3 => 3 * 800 * 90 / 100,    // 10% discount
        4 => 4 * 800 * 80 / 100,    // 20% discount
        5 => 5 * 800 * 75 / 100,    // 25% discount
        _ => 0,
    }
}
