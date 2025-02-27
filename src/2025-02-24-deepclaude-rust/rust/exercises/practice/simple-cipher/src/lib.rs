pub fn encode(key: &str, s: &str) -> Option<String> {
    // Validate key and input
    if key.is_empty() || !key.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    if !s.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    
    // Convert key characters to shift values (0-25)
    let key_shifts: Vec<u8> = key.bytes().map(|b| b - b'a').collect();
    let key_len = key_shifts.len();
    
    // Apply shift to each character in the input string
    let mut result = String::with_capacity(s.len());
    for (i, c) in s.bytes().enumerate() {
        let shift = key_shifts[i % key_len];
        let shifted = (c - b'a' + shift) % 26;
        result.push((shifted + b'a') as char);
    }
    
    Some(result)
}

pub fn decode(key: &str, s: &str) -> Option<String> {
    // Validate key and input
    if key.is_empty() || !key.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    if !s.chars().all(|c| c.is_ascii_lowercase()) {
        return None;
    }
    
    // Convert key characters to shift values (0-25)
    let key_shifts: Vec<u8> = key.bytes().map(|b| b - b'a').collect();
    let key_len = key_shifts.len();
    
    // Apply reverse shift to each character in the input string
    let mut result = String::with_capacity(s.len());
    for (i, c) in s.bytes().enumerate() {
        let shift = key_shifts[i % key_len];
        let shifted = (c - b'a' + 26 - shift) % 26;  // Add 26 to handle underflow
        result.push((shifted + b'a') as char);
    }
    
    Some(result)
}

// Simple Linear Congruential Generator for random numbers
struct Lcg {
    state: u64,
}

impl Lcg {
    fn new(seed: u64) -> Self {
        Self { state: seed }
    }

    fn next(&mut self) -> u64 {
        // Constants from Numerical Recipes
        self.state = self.state.wrapping_mul(6364136223846793005)
            .wrapping_add(1442695040888963407);
        self.state
    }
}

pub fn encode_random(s: &str) -> (String, String) {
    use std::time::{SystemTime, UNIX_EPOCH};
    
    // Generate seed from current time
    let seed = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .unwrap()
        .as_nanos() as u64;
    
    // Initialize our random number generator
    let mut rng = Lcg::new(seed);
    
    // Generate a random key of 100 lowercase letters
    let key: String = (0..100)
        .map(|_| (rng.next() % 26) as u8 + b'a')
        .map(|b| b as char)
        .collect();
    
    // Encode the string with the generated key
    let encoded = encode(&key, s).expect("Failed to encode with generated key");
    
    (key, encoded)
}
