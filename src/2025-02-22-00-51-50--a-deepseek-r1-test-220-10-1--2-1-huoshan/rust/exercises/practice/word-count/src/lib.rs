use std::collections::HashMap;

/// Count occurrences of words.
pub fn word_count(words: &str) -> HashMap<String, u32> {
    let mut counts = HashMap::new();
    let mut current_token = String::new();

    for c in words.chars() {
        if c.is_alphanumeric() || c == '\'' {
            current_token.push(c);
        } else {
            if !current_token.is_empty() {
                process_token(&mut counts, &current_token);
                current_token.clear();
            }
        }
    }

    // Process the last token after iteration
    if !current_token.is_empty() {
        process_token(&mut counts, &current_token);
    }

    counts
}

fn process_token(counts: &mut HashMap<String, u32>, token: &str) {
    let trimmed = token.trim_matches('\'');
    if trimmed.is_empty() {
        return;
    }
    if trimmed.chars().any(|c| c.is_alphanumeric()) {
        let lower = trimmed.to_lowercase();
        *counts.entry(lower).or_insert(0) += 1;
    }
}
