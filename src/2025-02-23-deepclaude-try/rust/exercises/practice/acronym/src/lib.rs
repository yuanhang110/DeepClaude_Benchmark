pub fn abbreviate(phrase: &str) -> String {
    phrase.replace('-', " ")
        .chars()
        .filter(|c| c.is_alphabetic() || c.is_whitespace())
        .collect::<String>()
        .split_whitespace()
        .flat_map(|word| {
            let mut prev_char = None;
            word.chars().filter_map(move |c| {
                let result = if prev_char.is_none() {
                    Some(c.to_ascii_uppercase())
                } else if let Some(prev) = prev_char {
                    if prev.is_ascii_lowercase() && c.is_ascii_uppercase() {
                        Some(c)
                    } else {
                        None
                    }
                } else {
                    None
                };
                prev_char = Some(c);
                result
            })
        })
        .collect()
}
