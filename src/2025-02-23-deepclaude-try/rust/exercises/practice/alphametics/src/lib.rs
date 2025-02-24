use std::collections::{HashMap, HashSet};

pub fn solve(input: &str) -> Option<HashMap<char, u8>> {
    let equation_parts: Vec<&str> = input.split(" = ").collect();
    if equation_parts.len() != 2 {
        return None;
    }
    let left_terms: Vec<&str> = equation_parts[0].split(" + ").collect();
    let right = equation_parts[1];
    
    if left_terms.is_empty() || right.is_empty() {
        return None;
    }
    
    let mut all_letters = HashSet::new();
    let mut leading_letters = HashSet::new();
    
    for term in &left_terms {
        let first_char = term.chars().next()?;
        leading_letters.insert(first_char);
        for c in term.chars() {
            all_letters.insert(c);
        }
    }
    let rhs_first_char = right.chars().next()?;
    leading_letters.insert(rhs_first_char);
    for c in right.chars() {
        all_letters.insert(c);
    }
    
    let mut letters: Vec<char> = all_letters.into_iter().collect();
    letters.sort_by(|a, b| b.cmp(a));
    
    let mut used = [false; 10];
    let mut current_map = HashMap::new();
    
    backtrack(
        &letters,
        &leading_letters,
        &left_terms,
        right,
        &mut used,
        &mut current_map,
    )
}

fn backtrack(
    letters: &[char],
    leading_letters: &HashSet<char>,
    left_terms: &[&str],
    right: &str,
    used: &mut [bool; 10],
    current_map: &mut HashMap<char, u8>,
) -> Option<HashMap<char, u8>> {
    if current_map.len() == letters.len() {
        // Check leading zeros (though backtracking should prevent this)
        for &c in leading_letters {
            if *current_map.get(&c).unwrap_or(&1) == 0 {
                return None;
            }
        }
        
        // Calculate sum of left terms
        let mut left_sum = 0u64;
        for term in left_terms {
            let num = term_to_num(term, current_map)?;
            left_sum = left_sum.checked_add(num)?;
        }
        
        // Calculate right term
        let right_num = term_to_num(right, current_map)?;
        
        if left_sum == right_num {
            return Some(current_map.clone());
        } else {
            return None;
        }
    }
    
    let next_char = letters[current_map.len()];
    for digit in (0..10u8).rev() {
        if used[digit as usize] {
            continue;
        }
        // Check if assigning zero to a leading letter
        if digit == 0 && leading_letters.contains(&next_char) {
            continue;
        }
        used[digit as usize] = true;
        current_map.insert(next_char, digit);
        let result = backtrack(letters, leading_letters, left_terms, right, used, current_map);
        if result.is_some() {
            return result;
        }
        current_map.remove(&next_char);
        used[digit as usize] = false;
    }
    
    None
}

fn term_to_num(term: &str, mapping: &HashMap<char, u8>) -> Option<u64> {
    let mut num = 0u64;
    for c in term.chars() {
        let digit = *mapping.get(&c)? as u64;
        num = num.checked_mul(10)?;
        num = num.checked_add(digit)?;
    }
    Some(num)
}
