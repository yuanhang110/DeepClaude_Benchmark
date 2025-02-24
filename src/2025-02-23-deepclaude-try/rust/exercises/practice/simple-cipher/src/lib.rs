pub fn encode(key: &str, s: &str) -> Option<String> {
    if key.is_empty() || !key.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    if !s.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    let key_shifts: Vec<u8> = key.chars().map(|c| c as u8 - b'a').collect();
    let mut result = String::new();
    for (i, c) in s.chars().enumerate() {
        let shift = key_shifts[i % key_shifts.len()];
        let shifted = (c as u8 - b'a' + shift) % 26;
        result.push((shifted + b'a') as char);
    }
    Some(result)
}

pub fn decode(key: &str, s: &str) -> Option<String> {
    if key.is_empty() || !key.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    if !s.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    let key_shifts: Vec<u8> = key.chars().map(|c| c as u8 - b'a').collect();
    let mut result = String::new();
    for (i, c) in s.chars().enumerate() {
        let shift = key_shifts[i % key_shifts.len()];
        let shifted = (c as u8 - b'a' + 26 - shift) % 26;
        result.push((shifted + b'a') as char);
    }
    Some(result)
}

pub fn encode_random(s: &str) -> (String, String) {
    use std::time::{SystemTime, UNIX_EPOCH};
    
    let seed = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .unwrap()
        .as_nanos();
    
    let mut rng = seed;
    let mut key = String::new();
    for _ in 0..100 {
        rng = rng.wrapping_mul(1103515245).wrapping_add(12345);
        let c = (rng % 26) as u8;
        key.push((b'a' + c) as char);
    }
    
    let encoded = encode(&key, s).expect("Invalid input");
    (key, encoded)
}
