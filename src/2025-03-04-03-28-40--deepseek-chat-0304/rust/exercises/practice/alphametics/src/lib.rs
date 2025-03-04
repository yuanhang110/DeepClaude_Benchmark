use std::collections::HashMap;

pub fn solve(input: &str) -> Option<HashMap<char, u8>> {
    let parts: Vec<&str> = input.split(" == ").collect();
    if parts.len() != 2 {
        return None;
    }
    
    let left = parts[0];
    let right = parts[1];
    
    let words: Vec<&str> = left.split(" + ").collect();
    let result = right;
    
    // Get all unique letters
    let mut letters: Vec<char> = input.chars()
        .filter(|c| c.is_ascii_alphabetic())
        .collect();
    letters.sort();
    letters.dedup();
    
    // Leading letters can't be zero
    let mut leading_letters = Vec::new();
    for word in words.iter().chain(std::iter::once(&result)) {
        if let Some(c) = word.chars().next() {
            leading_letters.push(c);
        }
    }
    
    // Generate all possible digit permutations
    let digits: Vec<u8> = (0..=9).collect();
    let mut used = vec![false; 10];
    let mut mapping = HashMap::new();
    
    if let Some(solution) = permute(&letters, &digits, &mut used, &mut mapping, &words, &result, &leading_letters) {
        return Some(solution);
    }
    
    None
}

fn permute(
    letters: &[char],
    digits: &[u8],
    used: &mut Vec<bool>,
    mapping: &mut HashMap<char, u8>,
    words: &[&str],
    result: &str,
    leading_letters: &[char]
) -> Option<HashMap<char, u8>> {
    if mapping.len() == letters.len() {
        // Check if the equation holds
        let sum_words: u64 = words.iter()
            .map(|word| word_to_num(word, mapping))
            .sum();
        let result_num = word_to_num(result, mapping);
        
        if sum_words == result_num {
            return Some(mapping.clone());
        }
        return None;
    }
    
    let current_char = letters[mapping.len()];
    
    for (i, &digit) in digits.iter().enumerate() {
        // Skip if digit is already used
        if used[i] {
            continue;
        }
        
        // Skip if leading letter is assigned to 0
        if digit == 0 && leading_letters.contains(&current_char) {
            continue;
        }
        
        used[i] = true;
        mapping.insert(current_char, digit);
        
        if let Some(solution) = permute(letters, digits, used, mapping, words, result, leading_letters) {
            return Some(solution);
        }
        
        mapping.remove(&current_char);
        used[i] = false;
    }
    
    None
}

fn word_to_num(word: &str, mapping: &HashMap<char, u8>) -> u64 {
    word.chars()
        .fold(0, |acc, c| acc * 10 + *mapping.get(&c).unwrap() as u64)
}
