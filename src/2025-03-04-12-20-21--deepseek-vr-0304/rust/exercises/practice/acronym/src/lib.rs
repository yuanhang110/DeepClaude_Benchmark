pub fn abbreviate(phrase: &str) -> String {
    let mut acronym = String::new();
    let processed = phrase.replace('-', " ");
    for word in processed.split_whitespace() {
        let cleaned: Vec<char> = word.chars().filter(|c| c.is_alphabetic()).collect();
        if cleaned.is_empty() {
            continue;
        }
        acronym.push(cleaned[0].to_ascii_uppercase());
        for i in 1..cleaned.len() {
            if cleaned[i].is_uppercase() && cleaned[i-1].is_lowercase() {
                acronym.push(cleaned[i].to_ascii_uppercase());
            }
        }
    }
    acronym
}
