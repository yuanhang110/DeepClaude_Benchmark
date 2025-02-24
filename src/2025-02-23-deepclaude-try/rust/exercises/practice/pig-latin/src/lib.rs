pub fn translate(input: &str) -> String {
    input.split_whitespace()
        .map(translate_word)
        .collect::<Vec<_>>()
        .join(" ")
}

fn translate_word(word: &str) -> String {
    // Rule 1: words beginning with vowel sound
    if starts_with_vowel(word) || starts_with_xr_or_yt(word) {
        return format!("{}ay", word);
    }

    // Rule 3: words beginning with consonant(s) followed by "qu"
    if let Some(split_at) = find_qu_split(word) {
        let (move_part, remain) = word.split_at(split_at);
        return format!("{}{}ay", remain, move_part);
    }

    // Rule 4: words beginning with consonant(s) followed by 'y'
    if let Some(y_split) = find_y_split(word) {
        let (move_part, remain) = word.split_at(y_split);
        return format!("{}{}ay", remain, move_part);
    }

    // Rule 2: words beginning with consonant sound
    let split_at = find_consonant_cluster_end(word);
    let (move_part, remain) = word.split_at(split_at);
    format!("{}{}ay", remain, move_part)
}

fn is_vowel(c: char) -> bool {
    matches!(c, 'a' | 'e' | 'i' | 'o' | 'u')
}

fn starts_with_vowel(word: &str) -> bool {
    word.chars().next().map_or(false, is_vowel)
}

fn starts_with_xr_or_yt(word: &str) -> bool {
    word.starts_with("xr") || word.starts_with("yt")
}

fn find_qu_split(word: &str) -> Option<usize> {
    for i in 0..word.len().saturating_sub(1) {
        if word.get(i..i+2) == Some("qu") {
            if is_consonant_cluster(&word[..i]) {
                return Some(i + 2);
            }
        }
    }
    None
}

fn is_consonant_cluster(s: &str) -> bool {
    s.chars().all(|c| !is_vowel(c))
}

fn find_y_split(word: &str) -> Option<usize> {
    word.find('y').and_then(|y_pos| {
        if y_pos > 0 && is_consonant_cluster(&word[..y_pos]) {
            Some(y_pos)
        } else {
            None
        }
    })
}

fn find_consonant_cluster_end(word: &str) -> usize {
    word.chars()
        .take_while(|&c| !is_vowel(c))
        .count()
}
