pub fn lowest_price(books: &[u32]) -> u32 {
    let mut counts = [0; 5];
    for &book in books {
        if (1..=5).contains(&book) {
            counts[(book - 1) as usize] += 1;
        }
    }
    let mut counts = counts.map(|c| c as i32);

    let mut groups = [0; 6]; // groups[0] unused

    loop {
        counts.sort_by(|a, b| b.cmp(a));

        let max_group = counts.iter().take_while(|&&c| c > 0).count();

        if max_group == 0 {
            break;
        }

        groups[max_group] += 1;

        for i in 0..max_group {
            counts[i] -= 1;
        }
    }

    let replace = groups[5].min(groups[3]);
    groups[5] -= replace;
    groups[3] -= replace;
    groups[4] += 2 * replace;

    let cost_per_group = [0, 800, 1520, 2160, 2560, 3000];
    let mut total = 0;

    for (s, &count) in groups.iter().enumerate().skip(1) {
        total += count as u32 * cost_per_group[s];
    }

    total
}
