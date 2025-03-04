use rand::Rng;

const ALPHABET: &str = "abcdefghijklmnopqrstuvwxyz";

fn shift_char(c: char, shift: u8) -> Option<char> {
    if !c.is_ascii_lowercase() {
        return None;
    }
    let pos = ALPHABET.find(c)? as u8;
    let shifted = (pos + shift) % 26;
    ALPHABET.chars().nth(shifted as usize)
}

pub fn encode(key: &str, s: &str) -> Option<String> {
    if key.is_empty() || !key.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    
    s.chars()
        .zip(key.chars().cycle())
        .map(|(c, k)| {
            let shift = ALPHABET.find(k)? as u8;
            shift_char(c, shift)
        })
        .collect()
}

pub fn decode(key: &str, s: &str) -> Option<String> {
    if key.is_empty() || !key.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    
    s.chars()
        .zip(key.chars().cycle())
        .map(|(c, k)| {
            let shift = 26 - (ALPHABET.find(k)? as u8);
            shift_char(c, shift)
        })
        .collect()
}

pub fn encode_random(s: &str) -> (String, String) {
    let mut rng = rand::thread_rng();
    let key: String = (0..100)
        .map(|_| {
            let idx = rng.gen_range(0..26);
            ALPHABET.chars().nth(idx).unwrap()
        })
        .collect();
    
    let encoded = encode(&key, s).unwrap();
    (key, encoded)
}
