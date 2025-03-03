pub fn abbreviate(phrase: &str) -> String {
    // Replace hyphens with spaces to treat them as word separators
    let processed = phrase.replace("-", " ");
    
    // Filter to keep only alphabetic characters and spaces
    let filtered: String = processed
        .chars()
        .filter(|c| c.is_alphabetic() || c.is_whitespace())
        .collect();
    
    // Split into words, process each word for camelCase, uppercase, and join
    filtered
        .split_whitespace()
        .flat_map(|word| {
            let mut acronym_letters = Vec::new();
            let mut prev_char: Option<char> = None;
            
            for c in word.chars() {
                if prev_char.is_none() {
                    // First character is always part of the acronym
                    acronym_letters.push(c);
                } else if let Some(prev) = prev_char {
                    // If current is uppercase and previous is lowercase, it's a new word in camelCase
                    if prev.is_ascii_lowercase() && c.is_ascii_uppercase() {
                        acronym_letters.push(c);
                    }
                }
                prev_char = Some(c);
            }
            
            acronym_letters
        })
        .map(|c| c.to_ascii_uppercase())
        .collect()
}
