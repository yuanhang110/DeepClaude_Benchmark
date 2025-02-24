pub fn abbreviate(phrase: &str) -> String {
    phrase.replace('-', " ")
        .split_whitespace()
        .flat_map(|word| {
            let cleaned: String = word.chars()
                .filter(|c| c.is_alphabetic())
                .collect();
            if cleaned.is_empty() {
                return vec![].into_iter();
            }
            let mut initials = Vec::new();
            let mut chars = cleaned.chars();
            let first_char = chars.next().unwrap().to_ascii_uppercase();
            initials.push(first_char);
            let mut prev_char = first_char;
            for c in chars {
                if prev_char.is_ascii_lowercase() && c.is_ascii_uppercase() {
                    initials.push(c);
                }
                prev_char = c;
            }
            initials.into_iter()
        })
        .collect()
}
