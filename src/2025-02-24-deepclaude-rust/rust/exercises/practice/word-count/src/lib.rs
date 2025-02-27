use std::collections::HashMap;

/// Count occurrences of words.
pub fn word_count(words: &str) -> HashMap<String, u32> {
    let mut counts = HashMap::new();
    let mut current_token = String::new();
    let mut tokens = Vec::new();

    // Split input into tokens
    for c in words.chars() {
        if c.is_ascii_alphanumeric() || c == '\'' {
            current_token.push(c);
        } else if !current_token.is_empty() {
            tokens.push(current_token);
            current_token = String::new();
        }
    }
    
    // Add the last token if any
    if !current_token.is_empty() {
        tokens.push(current_token);
    }

    // Process each token
    for token in tokens {
        let trimmed = token.trim_matches('\'');
        if !trimmed.is_empty() && trimmed.chars().any(|c| c.is_ascii_alphanumeric()) {
            let word = trimmed.to_lowercase();
            *counts.entry(word).or_insert(0) += 1;
        }
    }

    counts
}
