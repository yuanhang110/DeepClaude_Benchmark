pub fn encode(key: &str, s: &str) -> Option<String> {
    if key.is_empty() || !key.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    
    let key_bytes = key.as_bytes();
    let mut result = String::with_capacity(s.len());
    
    for (i, c) in s.chars().enumerate() {
        if c.is_ascii_lowercase() {
            let key_char = key_bytes[i % key.len()];
            let shift = (key_char - b'a') as i16;
            let c_code = c as i16 - b'a' as i16;
            let encoded = ((c_code + shift).rem_euclid(26) as u8) + b'a';
            result.push(encoded as char);
        } else {
            result.push(c);
        }
    }
    
    Some(result)
}

pub fn decode(key: &str, s: &str) -> Option<String> {
    if key.is_empty() || !key.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    
    let key_bytes = key.as_bytes();
    let mut result = String::with_capacity(s.len());
    
    for (i, c) in s.chars().enumerate() {
        if c.is_ascii_lowercase() {
            let key_char = key_bytes[i % key.len()];
            let shift = (key_char - b'a') as i16;
            let c_code = c as i16 - b'a' as i16;
            let decoded = ((c_code - shift).rem_euclid(26) as u8) + b'a';
            result.push(decoded as char);
        } else {
            result.push(c);
        }
    }
    
    Some(result)
}

pub fn encode_random(s: &str) -> (String, String) {
    use rand::Rng;
    
    let mut rng = rand::thread_rng();
    let key: String = (0..100)
        .map(|_| {
            let random_byte = rng.gen_range(b'a'..=b'z');
            random_byte as char
        })
        .collect();
    
    let encoded = encode(&key, s).unwrap();
    (key, encoded)
}
