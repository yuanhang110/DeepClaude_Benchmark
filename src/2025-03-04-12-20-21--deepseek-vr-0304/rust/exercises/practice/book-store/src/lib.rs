use std::collections::HashMap;

pub fn lowest_price(books: &[u32]) -> u32 {
    let mut counts = [0; 5];
    for &book in books {
        if book >= 1 && book <= 5 {
            counts[book as usize - 1] += 1;
        }
    }
    let mut counts = counts.to_vec();
    counts.sort_unstable_by(|a, b| b.cmp(a));
    let mut cache = HashMap::new();
    calculate_price(&counts, &mut cache)
}

fn calculate_price(counts: &[u32], cache: &mut HashMap<Vec<u32>, u32>) -> u32 {
    if counts.iter().all(|&c| c == 0) {
        return 0;
    }
    if let Some(&price) = cache.get(counts) {
        return price;
    }
    
    let mut min_price = u32::MAX;
    
    for group_size in (1..=5).rev() {
        if counts.len() < group_size {
            continue;
        }
        if counts[0..group_size].iter().any(|&c| c == 0) {
            continue;
        }
        
        let mut new_counts = counts.to_vec();
        for i in 0..group_size {
            new_counts[i] -= 1;
        }
        new_counts.sort_unstable_by(|a, b| b.cmp(a));
        
        let group_price = match group_size {
            5 => 3000,
            4 => 2560,
            3 => 2160,
            2 => 1520,
            1 => 800,
            _ => unreachable!(),
        };
        
        let remaining_price = calculate_price(&new_counts, cache);
        let total_price = group_price + remaining_price;
        
        if total_price < min_price {
            min_price = total_price;
        }
    }
    
    cache.insert(counts.to_vec(), min_price);
    min_price
}
