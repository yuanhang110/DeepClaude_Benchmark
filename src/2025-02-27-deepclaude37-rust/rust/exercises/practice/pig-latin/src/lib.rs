pub fn translate(input: &str) -> String {
    input.split_whitespace()
        .map(process_word)
        .collect::<Vec<_>>()
        .join(" ")
}

fn process_word(word: &str) -> String {
    // Rule 1: Check for xr, yt, or starting with vowel
    if word.starts_with("xr") || word.starts_with("yt") {
        return format!("{}ay", word);
    }

    if let Some(first_char) = word.chars().next() {
        if is_vowel(first_char) {
            return format!("{}ay", word);
        }
    }

    // Rule 3: Check for consonants followed by qu
    if let Some(qu_end) = find_qu_after_consonants(word) {
        let (consonants_qu, rest) = word.split_at(qu_end);
        return format!("{}{}ay", rest, consonants_qu);
    }

    // Rule 4: Check for consonants followed by y
    if let Some(y_pos) = find_y_after_consonants(word) {
        let (consonants, rest) = word.split_at(y_pos);
        return format!("{}{}ay", rest, consonants);
    }

    // Rule 2: Move leading consonants to end
    let split = find_consonant_split(word);
    let (consonants, rest) = word.split_at(split);
    format!("{}{}ay", rest, consonants)
}

fn is_vowel(c: char) -> bool {
    matches!(c, 'a' | 'e' | 'i' | 'o' | 'u')
}

fn find_qu_after_consonants(word: &str) -> Option<usize> {
    let qu_pos = word.find("qu")?;
    // Check if all characters before 'qu' are consonants
    let prefix = &word[..qu_pos];
    if prefix.chars().all(|c| !is_vowel(c)) {
        Some(qu_pos + 2) // Split after 'qu'
    } else {
        None
    }
}

fn find_y_after_consonants(word: &str) -> Option<usize> {
    let y_pos = word.find('y')?;
    if y_pos == 0 {
        return None; // 'y' can't be preceded by consonants if it's the first character
    }
    let prefix = &word[..y_pos];
    if prefix.chars().all(|c| !is_vowel(c)) {
        Some(y_pos)
    } else {
        None
    }
}

fn find_consonant_split(word: &str) -> usize {
    word.chars()
        .take_while(|&c| !is_vowel(c))
        .map(|c| c.len_utf8())
        .sum()
}
