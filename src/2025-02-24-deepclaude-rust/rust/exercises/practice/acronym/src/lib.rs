pub fn abbreviate(phrase: &str) -> String {
    let processed = phrase.replace('-', " ");
    let words = processed.split_whitespace();
    let mut acronym = String::new();
    
    for word in words {
        let filtered: String = word.chars().filter(|c| c.is_alphabetic()).collect();
        if filtered.is_empty() {
            continue;
        }
        
        let mut prev_char: Option<char> = None;
        for (i, c) in filtered.chars().enumerate() {
            if i == 0 {
                acronym.push(c.to_ascii_uppercase());
            } else if c.is_uppercase() && prev_char.map_or(false, |p| p.is_lowercase()) {
                acronym.push(c);
            }
            prev_char = Some(c);
        }
    }
    
    acronym
}
