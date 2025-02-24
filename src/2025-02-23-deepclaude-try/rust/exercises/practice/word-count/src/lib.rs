use std::collections::HashMap;

/// Count occurrences of words.
pub fn word_count(words: &str) -> HashMap<String, u32> {
    let mut counts = HashMap::new();
    let mut current_word = String::new();

    for c in words.chars() {
        if c.is_ascii_alphanumeric() || c == '\'' {
            current_word.push(c);
        } else {
            if !current_word.is_empty() {
                let trimmed = current_word.trim_matches('\'');
                if !trimmed.is_empty() {
                    let lower = trimmed.to_lowercase();
                    *counts.entry(lower).or_insert(0) += 1;
                }
                current_word.clear();
            }
        }
    }

    // Check for any remaining word after loop
    if !current_word.is_empty() {
        let trimmed = current_word.trim_matches('\'');
        if !trimmed.is_empty() {
            let lower = trimmed.to_lowercase();
            *counts.entry(lower).or_insert(0) += 1;
        }
    }

    counts
}
