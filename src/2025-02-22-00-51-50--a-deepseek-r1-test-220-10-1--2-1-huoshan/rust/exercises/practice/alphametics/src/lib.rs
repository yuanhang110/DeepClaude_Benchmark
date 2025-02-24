use std::collections::{HashMap, HashSet};

pub fn solve(input: &str) -> Option<HashMap<char, u8>> {
    // Parse input into left and right parts
    let parts: Vec<&str> = input.split(" = ").collect();
    if parts.len() != 2 {
        return None;
    }
    let left_operands: Vec<&str> = parts[0].split(" + ").collect();
    if left_operands.is_empty() {
        return None;
    }
    let right_result = parts[1];

    // Collect all unique characters and leading characters
    let mut leading_chars = HashSet::new();
    let mut all_chars = HashSet::new();
    
    for word in left_operands.iter().chain(std::iter::once(&right_result)) {
        if let Some(first_char) = word.chars().next() {
            leading_chars.insert(first_char);
            all_chars.insert(first_char);
            for c in word.chars().skip(1) {
                all_chars.insert(c);
            }
        }
    }
    
    let mut sorted_chars: Vec<char> = all_chars.into_iter().collect();
    sorted_chars.sort_unstable();
    
    // Use backtracking to try all permutations
    let mut stack = Vec::new();
    stack.push((HashMap::new(), 0u16, 0)); // (assignments, used_digits_mask, char_index)
    
    while let Some((assignments, used_mask, char_index)) = stack.pop() {
        if char_index == sorted_chars.len() {
            // Check if the sum matches
            let sum_left: Option<u64> = left_operands.iter()
                .map(|word| word.chars()
                    .fold(Some(0), |num, c| Some(num? * 10 + *assignments.get(&c)? as u64))
                )
                .sum();
            
            let sum_right = right_result.chars()
                .fold(Some(0), |num, c| Some(num? * 10 + *assignments.get(&c)? as u64));
            
            if let (Some(left_sum), Some(right_sum)) = (sum_left, sum_right) {
                if left_sum == right_sum {
                    return Some(assignments);
                }
            }
        } else {
            let current_char = sorted_chars[char_index];
            let is_leading = leading_chars.contains(&current_char);
            let start_digit = if is_leading { 1 } else { 0 };
            
            for digit in (start_digit..=9).rev() {
                if (used_mask & (1 << digit)) == 0 {
                    let mut new_assignments = assignments.clone();
                    new_assignments.insert(current_char, digit as u8);
                    let new_used_mask = used_mask | (1 << digit);
                    stack.push((new_assignments, new_used_mask, char_index + 1));
                }
            }
        }
    }
    
    None
}
