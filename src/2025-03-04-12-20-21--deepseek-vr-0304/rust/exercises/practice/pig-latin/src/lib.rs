pub fn translate(input: &str) -> String {
    input.split_whitespace()
        .map(|word| {
            if word.starts_with("xr") || word.starts_with("yt") {
                format!("{word}ay")
            } else if let Some(c) = word.chars().next() {
                if is_vowel(c) {
                    format!("{word}ay")
                } else {
                    let chars: Vec<char> = word.chars().collect();
                    let n = chars.len();
                    let mut split = 0;
                    while split < n {
                        let current = chars[split];
                        if is_vowel(current) {
                            break;
                        }
                        // Check for 'qu' sequence
                        if current == 'q' && split + 1 < n && chars[split + 1] == 'u' {
                            split += 2;
                            break;
                        }
                        // Check if next character is 'y'
                        if split + 1 < n && chars[split + 1] == 'y' {
                            split += 1;
                            break;
                        }
                        split += 1;
                    }
                    let (consonants, rest) = word.split_at(split);
                    format!("{rest}{consonants}ay")
                }
            } else {
                String::new()
            }
        })
        .collect::<Vec<_>>()
        .join(" ")
}

fn is_vowel(c: char) -> bool {
    matches!(c, 'a' | 'e' | 'i' | 'o' | 'u')
}
