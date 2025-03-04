use std::collections::HashMap;

/// Count occurrences of words.
pub fn word_count(words: &str) -> HashMap<String, u32> {
    let mut counts = HashMap::new();
    
    // Convert to lowercase and iterate through characters
    let mut word = String::new();
    for c in words.to_lowercase().chars() {
        if c.is_alphanumeric() || c == '\'' {
            word.push(c);
        } else {
            if !word.is_empty() {
                *counts.entry(word).or_insert(0) += 1;
                word = String::new();
            }
        }
    }
    
    // Handle the last word if any
    if !word.is_empty() {
        *counts.entry(word).or_insert(0) += 1;
    }
    
    counts
}
