pub fn lowest_price(books: &[u32]) -> u32 {
    use std::collections::HashMap;
    
    // Count how many copies we have of each book
    let mut counts = vec![0; 5];
    for &book in books {
        if book >= 1 && book <= 5 {
            counts[(book - 1) as usize] += 1;
        }
    }
    
    // Sort in descending order and filter out books with zero copies
    counts.sort_unstable_by(|a, b| b.cmp(a));
    let counts: Vec<u32> = counts.into_iter().filter(|&x| x > 0).collect();
    
    // Use memoization to avoid recalculating the same subproblems
    let mut cache = HashMap::new();
    calculate_price(&counts, &mut cache)
}

fn calculate_price(counts: &[u32], cache: &mut std::collections::HashMap<Vec<u32>, u32>) -> u32 {
    // Base case: no more books to process
    if counts.is_empty() {
        return 0;
    }
    
    // Check if we've already calculated this configuration
    if let Some(&price) = cache.get(counts) {
        return price;
    }
    
    let mut min_price = u32::MAX;
    let max_group_size = counts.len(); // Maximum possible group size is the number of different books
    
    // Try each possible group size from 1 to max_group_size
    for k in 1..=max_group_size {
        // Create new counts after taking one book from the first k different books
        let mut new_counts = counts.to_vec();
        for i in 0..k {
            new_counts[i] = new_counts[i].saturating_sub(1);
        }
        
        // Filter out books with zero copies and sort again
        new_counts.retain(|&x| x > 0);
        new_counts.sort_unstable_by(|a, b| b.cmp(a));
        
        // Calculate the discount based on group size
        let discount = match k {
            5 => 25, // 25% discount for 5 different books
            4 => 20, // 20% discount for 4 different books
            3 => 10, // 10% discount for 3 different books
            2 => 5,  // 5% discount for 2 different books
            _ => 0,  // No discount for 1 book
        };
        
        // Calculate price in cents (each book costs $8 = 800 cents)
        let group_price = (k as u32) * 800 * (100 - discount) / 100;
        
        // Recursively calculate the price for remaining books
        let remaining_price = calculate_price(&new_counts, cache);
        
        // Update minimum price if this grouping is better
        min_price = min_price.min(group_price + remaining_price);
    }
    
    // Cache the result before returning
    cache.insert(counts.to_vec(), min_price);
    min_price
}
