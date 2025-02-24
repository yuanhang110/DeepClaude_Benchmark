#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    IncompleteNumber,
}

/// Convert a list of numbers to a stream of bytes encoded with variable length encoding.
pub fn to_bytes(values: &[u32]) -> Vec<u8> {
    let mut result = Vec::new();
    
    for &value in values {
        if value == 0 {
            result.push(0x00);
            continue;
        }
        
        let mut chunks = Vec::new();
        let mut n = value;
        
        while n > 0 {
            chunks.push((n & 0x7F) as u8);
            n >>= 7;
        }
        
        chunks.reverse();
        
        for i in 0..chunks.len() {
            if i != chunks.len() - 1 {
                chunks[i] |= 0x80;
            }
        }
        
        result.extend_from_slice(&chunks);
    }
    
    result
}

/// Given a stream of bytes, extract all numbers which are encoded in there.
pub fn from_bytes(bytes: &[u8]) -> Result<Vec<u32>, Error> {
    let mut result = Vec::new();
    let mut current_number = 0u32;
    let mut has_data = false;
    
    for &byte in bytes {
        let value = (byte & 0x7F) as u32;
        
        current_number = current_number
            .checked_shl(7)
            .ok_or(Error::IncompleteNumber)?
            .checked_or(value)
            .ok_or(Error::IncompleteNumber)?;
        
        has_data = true;
        
        if (byte & 0x80) == 0 {
            result.push(current_number);
            current_number = 0;
            has_data = false;
        }
    }
    
    if has_data {
        Err(Error::IncompleteNumber)
    } else {
        Ok(result)
    }
}
