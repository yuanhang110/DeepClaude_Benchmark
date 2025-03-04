pub fn translate(input: &str) -> String {
    input.split_whitespace()
        .map(|word| {
            // Rule 1: Starts with vowel, xr, or yt
            if word.starts_with(|c: char| is_vowel(c)) 
                || word.starts_with("xr") 
                || word.starts_with("yt") {
                return format!("{}ay", word);
            }
            
            // Find the first vowel position
            let first_vowel = word.find(|c: char| is_vowel(c)).unwrap_or(0);
            
            // Rule 3: Handle 'qu' cases
            if let Some(qu_pos) = word.find("qu") {
                if qu_pos == first_vowel {
                    let split = qu_pos + 2;
                    return format!("{}{}ay", &word[split..], &word[..split]);
                }
            }
            
            // Rule 4: Handle 'y' after consonants
            if let Some(y_pos) = word.find('y') {
                if y_pos > 0 && (y_pos == first_vowel || first_vowel >= word.len()) {
                    return format!("{}{}ay", &word[y_pos..], &word[..y_pos]);
                }
            }
            
            // Rule 2: Default consonant case
            format!("{}{}ay", &word[first_vowel..], &word[..first_vowel])
        })
        .collect::<Vec<_>>()
        .join(" ")
}

fn is_vowel(c: char) -> bool {
    matches!(c.to_ascii_lowercase(), 'a' | 'e' | 'i' | 'o' | 'u')
}
