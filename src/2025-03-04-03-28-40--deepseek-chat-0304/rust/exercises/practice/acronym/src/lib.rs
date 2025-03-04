pub fn abbreviate(phrase: &str) -> String {
    phrase
        .split(|c: char| c.is_whitespace() || c == '-')  // Split on whitespace and hyphens
        .filter(|word| !word.is_empty())  // Remove empty strings
        .flat_map(|word| {
            // Split camelCase words and get first letters
            let mut chars = word.chars();
            let mut result = Vec::new();
            if let Some(first) = chars.next() {
                if first.is_alphabetic() {
                    result.push(first.to_ascii_uppercase());
                }
            }
            // Add uppercase letters in the middle of the word
            let mut prev_lower = true;
            for c in chars {
                if c.is_uppercase() && prev_lower {
                    result.push(c);
                }
                prev_lower = c.is_lowercase();
            }
            result
        })
        .collect()
}
