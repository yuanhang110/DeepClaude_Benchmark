pub fn encode(key: &str, s: &str) -> Option<String> {
    // Validate that key is not empty and contains only lowercase letters
    if key.is_empty() || !key.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }

    // Validate that input string contains only lowercase letters
    if !s.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }

    let key_bytes = key.as_bytes();
    let key_len = key_bytes.len();
    let mut result = String::with_capacity(s.len());

    for (i, c) in s.bytes().enumerate() {
        // Calculate shift based on the corresponding key character
        let shift = key_bytes[i % key_len] - b'a';
        
        // Apply shift to the current character
        let encoded = ((c - b'a' + shift) % 26) + b'a';
        result.push(encoded as char);
    }

    Some(result)
}

pub fn decode(key: &str, s: &str) -> Option<String> {
    // Validate that key is not empty and contains only lowercase letters
    if key.is_empty() || !key.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }

    // Validate that input string contains only lowercase letters
    if !s.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }

    let key_bytes = key.as_bytes();
    let key_len = key_bytes.len();
    let mut result = String::with_capacity(s.len());

    for (i, c) in s.bytes().enumerate() {
        // Calculate shift based on the corresponding key character
        let shift = key_bytes[i % key_len] - b'a';
        
        // Apply reverse shift to the current character (add 26 to handle negative values)
        let decoded = ((c - b'a' + 26 - shift) % 26) + b'a';
        result.push(decoded as char);
    }

    Some(result)
}

pub fn encode_random(s: &str) -> (String, String) {
    use std::time::{SystemTime, UNIX_EPOCH};
    
    // Validate that input string contains only lowercase letters
    if !s.chars().all(|c| c.is_ascii_lowercase()) {
        panic!("Input string must contain only lowercase letters");
    }
    
    // Generate a pseudo-random key using current time as seed
    let seed = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .unwrap()
        .as_nanos();
    
    let mut key = String::with_capacity(100);
    let mut current = seed;
    
    // Generate a key of 100 lowercase letters
    for _ in 0..100 {
        // Simple Linear Congruential Generator
        current = current.wrapping_mul(6364136223846793005).wrapping_add(1442695040888963407);
        let c = ((current % 26) as u8) + b'a';
        key.push(c as char);
    }
    
    // Encode the string using the generated key
    let encoded = encode(&key, s).unwrap();
    
    (key, encoded)
}
