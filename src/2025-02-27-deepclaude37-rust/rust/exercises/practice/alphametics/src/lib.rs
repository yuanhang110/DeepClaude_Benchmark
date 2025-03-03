use std::collections::{HashMap, HashSet};

pub fn solve(input: &str) -> Option<HashMap<char, u8>> {
    // Parse the input string into left-hand side terms and right-hand side term
    let (lhs_terms, rhs_term) = parse_input(input)?;
    
    // Collect all unique characters from the terms
    let all_chars = collect_unique_chars(&lhs_terms, rhs_term);
    
    // Identify leading characters (which cannot be zero)
    let leading_letters = collect_leading_letters(&lhs_terms, rhs_term);

    // Sort characters to prioritize leading letters in the backtracking process
    let mut chars: Vec<char> = all_chars.into_iter().collect();
    chars.sort_by(|a, b| {
        let a_leading = leading_letters.contains(a);
        let b_leading = leading_letters.contains(b);
        b_leading.cmp(&a_leading).then_with(|| a.cmp(b))
    });

    // Start the backtracking process
    let mut used_digits = [false; 10];
    let mut assignments = HashMap::new();
    
    backtrack(
        0,
        &chars,
        &leading_letters,
        &lhs_terms,
        rhs_term,
        &mut used_digits,
        &mut assignments,
    )
}

// Parse the input string into LHS terms and RHS term
fn parse_input(input: &str) -> Option<(Vec<&str>, &str)> {
    let mut parts = input.split(" = ");
    let lhs = parts.next()?;
    let rhs = parts.next()?;
    if parts.next().is_some() {
        return None; // More than one "=" symbol
    }
    
    let lhs_terms: Vec<&str> = lhs.split(" + ").collect();
    if lhs_terms.is_empty() {
        return None;
    }
    
    Some((lhs_terms, rhs))
}

// Collect all unique characters from all terms
fn collect_unique_chars(lhs_terms: &[&str], rhs_term: &str) -> HashSet<char> {
    let mut chars = HashSet::new();
    for term in lhs_terms {
        chars.extend(term.chars().filter(|c| c.is_alphabetic()));
    }
    chars.extend(rhs_term.chars().filter(|c| c.is_alphabetic()));
    chars
}

// Identify the leading letters of each term
fn collect_leading_letters(lhs_terms: &[&str], rhs_term: &str) -> HashSet<char> {
    let mut leading = HashSet::new();
    for term in lhs_terms {
        if let Some(c) = term.chars().filter(|c| c.is_alphabetic()).next() {
            leading.insert(c);
        }
    }
    if let Some(c) = rhs_term.chars().filter(|c| c.is_alphabetic()).next() {
        leading.insert(c);
    }
    leading
}

// Backtracking function to find valid digit assignments
fn backtrack(
    idx: usize,
    chars: &[char],
    leading_letters: &HashSet<char>,
    lhs_terms: &[&str],
    rhs_term: &str,
    used_digits: &mut [bool; 10],
    assignments: &mut HashMap<char, u8>,
) -> Option<HashMap<char, u8>> {
    // If all characters have been assigned, check if the equation is satisfied
    if idx == chars.len() {
        return if check_equation(assignments, lhs_terms, rhs_term) {
            Some(assignments.clone())
        } else {
            None
        };
    }

    let c = chars[idx];
    let is_leading = leading_letters.contains(&c);

    // Try each possible digit for the current character
    for digit in 0..=9 {
        if used_digits[digit as usize] {
            continue; // Skip already used digits
        }
        
        if is_leading && digit == 0 {
            continue; // Leading letters cannot be zero
        }

        // Try this assignment
        used_digits[digit as usize] = true;
        assignments.insert(c, digit);

        // Recursive call to assign the next character
        if let Some(solution) = backtrack(
            idx + 1,
            chars,
            leading_letters,
            lhs_terms,
            rhs_term,
            used_digits,
            assignments,
        ) {
            return Some(solution);
        }

        // Backtrack
        assignments.remove(&c);
        used_digits[digit as usize] = false;
    }

    None
}

// Check if the current assignments satisfy the equation
fn check_equation(
    assignments: &HashMap<char, u8>,
    lhs_terms: &[&str],
    rhs_term: &str,
) -> bool {
    let rhs = word_to_number(rhs_term, assignments);
    
    let lhs_sum: Option<u64> = lhs_terms
        .iter()
        .map(|term| word_to_number(term, assignments))
        .sum();

    match (lhs_sum, rhs) {
        (Some(l), Some(r)) => l == r,
        _ => false,
    }
}

// Convert a word to its numeric value based on the character assignments
fn word_to_number(word: &str, assignments: &HashMap<char, u8>) -> Option<u64> {
    let mut num: u64 = 0;
    for c in word.chars() {
        if !c.is_alphabetic() {
            continue; // Skip non-alphabetic characters
        }
        
        let &digit = assignments.get(&c)?;
        num = num.checked_mul(10)?.checked_add(digit as u64)?;
    }
    Some(num)
}
