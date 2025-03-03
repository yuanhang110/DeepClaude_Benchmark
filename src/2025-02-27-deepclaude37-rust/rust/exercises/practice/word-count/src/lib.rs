use std::collections::HashMap;

/// Count occurrences of words.
pub fn word_count(words: &str) -> HashMap<String, u32> {
    let mut counts = HashMap::new();
    let mut current_word = String::new();

    for c in words.chars() {
        if c.is_ascii_alphanumeric() || c == '\'' {
            current_word.push(c);
        } else if !current_word.is_empty() {
            process_word(&mut counts, &current_word);
            current_word.clear();
        }
    }

    // Process the last word if any
    if !current_word.is_empty() {
        process_word(&mut counts, &current_word);
    }

    counts
}

// Helper function to process a word and update the counts
fn process_word(counts: &mut HashMap<String, u32>, word: &str) {
    let trimmed = word.trim_matches('\'');
    if !trimmed.is_empty() {
        let lower = trimmed.to_lowercase();
        *counts.entry(lower).or_insert(0) += 1;
    }
}
