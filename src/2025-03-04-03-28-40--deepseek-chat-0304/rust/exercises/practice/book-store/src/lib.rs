pub fn lowest_price(books: &[u32]) -> u32 {
    if books.is_empty() {
        return 0;
    }

    // Count how many of each book we have
    let mut counts = [0; 5];
    for &book in books {
        if book >= 1 && book <= 5 {
            counts[(book - 1) as usize] += 1;
        }
    }
    counts.sort_unstable_by(|a, b| b.cmp(a));

    // Try different grouping strategies to find the minimal price
    let mut min_price = u32::MAX;
    
    // Try grouping with maximum 5-book groups first
    let mut current_counts = counts.clone();
    let mut price = 0;
    while current_counts.iter().any(|&c| c > 0) {
        let group_size = current_counts.iter().filter(|&&c| c > 0).count().min(5);
        if group_size == 0 {
            break;
        }
        price += match group_size {
            1 => 800,
            2 => (800 * 2 * 95) / 100,
            3 => (800 * 3 * 90) / 100,
            4 => (800 * 4 * 80) / 100,
            5 => (800 * 5 * 75) / 100,
            _ => 0,
        };
        for i in 0..group_size {
            current_counts[i] -= 1;
        }
        current_counts.sort_unstable_by(|a, b| b.cmp(a));
    }
    min_price = min_price.min(price);

    // Try grouping with maximum 4-book groups first
    let mut current_counts = counts.clone();
    let mut price = 0;
    while current_counts.iter().any(|&c| c > 0) {
        let group_size = current_counts.iter().filter(|&&c| c > 0).count().min(4);
        if group_size == 0 {
            break;
        }
        price += match group_size {
            1 => 800,
            2 => (800 * 2 * 95) / 100,
            3 => (800 * 3 * 90) / 100,
            4 => (800 * 4 * 80) / 100,
            _ => 0,
        };
        for i in 0..group_size {
            current_counts[i] -= 1;
        }
        current_counts.sort_unstable_by(|a, b| b.cmp(a));
    }
    min_price = min_price.min(price);

    min_price
}
