pub fn translate(input: &str) -> String {
    input.split_whitespace()
        .map(translate_word)
        .collect::<Vec<_>>()
        .join(" ")
}

fn translate_word(word: &str) -> String {
    if word.starts_with(|c| is_vowel(c)) || word.starts_with("xr") || word.starts_with("yt") {
        return format!("{}ay", word);
    }

    if let Some(qu_pos) = word.find("qu") {
        let (before_qu, after_qu) = word.split_at(qu_pos);
        if before_qu.chars().all(|c| !is_vowel(c)) && after_qu.starts_with("qu") {
            let split_point = qu_pos + 2;
            let (prefix, suffix) = word.split_at(split_point);
            return format!("{}{}ay", suffix, prefix);
        }
    }

    if let Some(y_pos) = word.find('y') {
        if y_pos > 0 {
            let (before_y, after_y) = word.split_at(y_pos);
            if before_y.chars().all(|c| !is_vowel(c)) {
                return format!("{}{}ay", after_y, before_y);
            }
        }
    }

    let split_pos = word.find(|c| is_vowel(c)).unwrap_or(0);
    let (consonants, rest) = word.split_at(split_pos);
    format!("{}{}ay", rest, consonants)
}

fn is_vowel(c: char) -> bool {
    matches!(c, 'a' | 'e' | 'i' | 'o' | 'u')
}
