pub fn translate(input: &str) -> String {
    input.split_whitespace()
        .map(|word| {
            let chars: Vec<char> = word.chars().collect();
            if chars.is_empty() {
                return String::new();
            }

            let mut split_index = 0;
            let first_char = chars[0];

            // Rule 1: Vowel, xr, yt
            if is_vowel(first_char) || starts_with(&chars, "xr") || starts_with(&chars, "yt") {
                split_index = 0;
            } else {
                // Rule 3: Check for 'qu' after consonants
                let mut qu_pos = None;
                for i in 0..chars.len() {
                    if i + 1 < chars.len() && chars[i] == 'q' && chars[i+1] == 'u' {
                        if (0..i).all(|j| !is_vowel(chars[j])) {
                            qu_pos = Some(i);
                            break;
                        }
                    }
                }
                if let Some(i) = qu_pos {
                    split_index = i + 2;
                } else {
                    // Rule 4: Check for 'y' after consonants
                    if let Some(y_pos) = chars.iter().position(|&c| c == 'y') {
                        if y_pos > 0 && (0..y_pos).all(|j| !is_vowel(chars[j])) {
                            split_index = y_pos;
                        } else {
                            // Rule 2: Split after consonants
                            split_index = find_first_vowel(&chars).unwrap_or(chars.len());
                        }
                    } else {
                        // Rule 2: Split after consonants
                        split_index = find_first_vowel(&chars).unwrap_or(chars.len());
                    }
                }
            }

            let suffix: String = chars[split_index..].iter().collect();
            let prefix: String = chars[..split_index].iter().collect();
            format!("{}{}ay", suffix, prefix)
        })
        .collect::<Vec<_>>()
        .join(" ")
}

fn is_vowel(c: char) -> bool {
    matches!(c, 'a' | 'e' | 'i' | 'o' | 'u')
}

fn starts_with(chars: &[char], s: &str) -> bool {
    let s_chars: Vec<char> = s.chars().collect();
    chars.len() >= s_chars.len() && chars[0..s_chars.len()] == s_chars[..]
}

fn find_first_vowel(chars: &[char]) -> Option<usize> {
    chars.iter().position(|&c| is_vowel(c))
}
