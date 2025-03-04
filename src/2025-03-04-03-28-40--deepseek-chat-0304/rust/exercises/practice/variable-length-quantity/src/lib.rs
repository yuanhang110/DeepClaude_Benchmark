#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    IncompleteNumber,
}

/// Convert a list of numbers to a stream of bytes encoded with variable length encoding.
pub fn to_bytes(values: &[u32]) -> Vec<u8> {
    let mut result = Vec::new();
    
    for &value in values {
        let mut val = value;
        let mut buffer = Vec::new();
        
        // Handle zero case
        if val == 0 {
            buffer.push(0);
        } else {
            while val > 0 {
                let mut byte = (val & 0x7F) as u8;
                val >>= 7;
                if !buffer.is_empty() {
                    byte |= 0x80;
                }
                buffer.push(byte);
            }
        }
        
        // Reverse to get correct order
        buffer.reverse();
        result.extend(buffer);
    }
    
    result
}

/// Given a stream of bytes, extract all numbers which are encoded in there.
pub fn from_bytes(bytes: &[u8]) -> Result<Vec<u32>, Error> {
    let mut result = Vec::new();
    let mut current_value = 0u32;
    let mut has_data = false;
    
    for &byte in bytes {
        current_value = (current_value << 7) | (byte & 0x7F) as u32;
        has_data = true;
        
        if (byte & 0x80) == 0 {
            result.push(current_value);
            current_value = 0;
            has_data = false;
        }
    }
    
    if has_data {
        Err(Error::IncompleteNumber)
    } else {
        Ok(result)
    }
}
