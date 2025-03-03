#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    IncompleteNumber,
}

/// Convert a list of numbers to a stream of bytes encoded with variable length encoding.
pub fn to_bytes(values: &[u32]) -> Vec<u8> {
    let mut result = Vec::new();
    
    for &value in values {
        let mut chunks = Vec::new();
        let mut val = value;
        
        if val == 0 {
            chunks.push(0);
        } else {
            while val > 0 {
                chunks.push((val & 0x7F) as u8);
                val >>= 7;
            }
        }
        
        // Reverse to get most significant chunks first
        chunks.reverse();
        
        // Set the high bit (bit 7) on all bytes except the last one
        for i in 0..chunks.len() {
            if i < chunks.len() - 1 {
                chunks[i] |= 0x80;
            }
        }
        
        result.extend_from_slice(&chunks);
    }
    
    result
}

/// Given a stream of bytes, extract all numbers which are encoded in there.
pub fn from_bytes(bytes: &[u8]) -> Result<Vec<u32>, Error> {
    let mut numbers = Vec::new();
    let mut current_number: u64 = 0;
    let mut num_bytes = 0;  // Track number of bytes processed for current number
    
    for &byte in bytes {
        let data = (byte & 0x7F) as u64;
        num_bytes += 1;
        
        // Shift current number left by 7 bits and add new data
        current_number = (current_number << 7) | data;
        
        // Check for overflow (number won't fit in u32)
        if current_number > 0xFFFFFFFF {
            return Err(Error::IncompleteNumber);
        }
        
        if (byte & 0x80) == 0 {
            // This is the last byte of the current number
            numbers.push(current_number as u32);
            current_number = 0;
            num_bytes = 0;  // Reset byte counter for next number
        }
    }
    
    // If we've processed any bytes that weren't finalized (even if value is zero)
    if num_bytes > 0 {
        return Err(Error::IncompleteNumber);
    }
    
    Ok(numbers)
}
