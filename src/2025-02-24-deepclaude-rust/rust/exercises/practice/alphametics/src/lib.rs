use std::collections::{HashMap, HashSet};

fn parse_input(input: &str) -> Option<(Vec<&str>, &str)> {
    let mut parts = input.split('=').map(|s| s.trim());
    let left_part = parts.next()?;
    let right = parts.next()?;
    if parts.next().is_some() {
        return None; // More than one '=' found
    }
    let left: Vec<&str> = left_part.split('+').map(|s| s.trim()).collect();
    if left.is_empty() {
        None
    } else {
        Some((left, right))
    }
}

fn collect_letters(left: &[&str], right: &str) -> Vec<char> {
    let mut letters = Vec::new();
    let mut seen = HashSet::new();
    let leading_chars = get_leading_chars(left, right);

    // Collect leading characters first, in order of appearance
    for word in left.iter().chain(std::iter::once(&right)) {
        if let Some(c) = word.chars().next() {
            if leading_chars.contains(&c) && !seen.contains(&c) {
                letters.push(c);
                seen.insert(c);
            }
        }
    }

    // Collect remaining characters from right to left in words
    let mut others = Vec::new();
    let max_len = left.iter().chain(std::iter::once(&right))
        .map(|w| w.len())
        .max()
        .unwrap_or(0);

    // Process column by column from right to left
    for pos in (0..max_len).rev() {
        for word in left.iter().chain(std::iter::once(&right)) {
            if let Some(c) = word.chars().nth(word.len().saturating_sub(pos + 1)) {
                if !seen.contains(&c) {
                    others.push(c);
                    seen.insert(c);
                }
            }
        }
    }

    letters.extend(others);
    letters
}

fn get_leading_chars(left: &[&str], right: &str) -> HashSet<char> {
    let mut set = HashSet::new();
    for word in left {
        if let Some(c) = word.chars().next() {
            set.insert(c);
        }
    }
    if let Some(c) = right.chars().next() {
        set.insert(c);
    }
    set
}

fn word_to_number(word: &str, map: &HashMap<char, u8>) -> Option<u64> {
    let mut num = 0;
    for c in word.chars() {
        let &digit = map.get(&c)?;
        num = num * 10 + u64::from(digit);
    }
    Some(num)
}

fn check_equation(left: &[&str], right: &str, map: &HashMap<char, u8>) -> bool {
    let right_num = match word_to_number(right, map) {
        Some(n) => n,
        None => return false,
    };

    let left_sum = left
        .iter()
        .map(|word| word_to_number(word, map))
        .try_fold(0u64, |acc, x| x.map(|v| acc + v));

    matches!(left_sum, Some(sum) if sum == right_num)
}

fn backtrack(
    all_letters: &[char],
    leading_chars: &HashSet<char>,
    left: &[&str],
    right: &str,
    index: usize,
    used: &mut [bool; 10],
    map: &mut HashMap<char, u8>,
) -> Option<HashMap<char, u8>> {
    if index == all_letters.len() {
        return if check_equation(left, right, map) {
            Some(map.clone())
        } else {
            None
        };
    }

    let c = all_letters[index];
    for d in 0..10 {
        if used[d] || (d == 0 && leading_chars.contains(&c)) {
            continue;
        }
        used[d] = true;
        map.insert(c, d as u8);
        let result = backtrack(all_letters, leading_chars, left, right, index + 1, used, map);
        if result.is_some() {
            return result;
        }
        map.remove(&c);
        used[d] = false;
    }
    None
}

pub fn solve(input: &str) -> Option<HashMap<char, u8>> {
    let (left, right) = parse_input(input)?;
    let all_letters = collect_letters(&left, right);
    let leading_chars = get_leading_chars(&left, right);
    let mut used = [false; 10];
    let mut map = HashMap::new();
    backtrack(
        &all_letters,
        &leading_chars,
        &left,
        right,
        0,
        &mut used,
        &mut map,
    )
}
