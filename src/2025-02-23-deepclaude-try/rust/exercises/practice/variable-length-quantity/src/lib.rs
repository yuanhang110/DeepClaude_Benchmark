#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    IncompleteNumber,
}

/// Convert a list of numbers to a stream of bytes encoded with variable length encoding.
pub fn to_bytes(values: &[u32]) -> Vec<u8> {
    let mut result = Vec::new();
    for &value in values {
        let mut chunks = Vec::new();
        let mut num = value;
        if num == 0 {
            chunks.push(0);
        } else {
            while num > 0 {
                chunks.push((num & 0x7F) as u8);
                num >>= 7;
            }
            chunks.reverse();
        }
        for i in 0..chunks.len() {
            let mut byte = chunks[i];
            if i != chunks.len() - 1 {
                byte |= 0x80;
            }
            result.push(byte);
        }
    }
    result
}

/// Given a stream of bytes, extract all numbers which are encoded in there.
pub fn from_bytes(bytes: &[u8]) -> Result<Vec<u32>, Error> {
    let mut result = Vec::new();
    let mut current = 0;
    let mut has_pending = false;
    for &byte in bytes {
        current = (current << 7) | (byte & 0x7F) as u32;
        if (byte & 0x80) == 0 {
            result.push(current);
            current = 0;
            has_pending = false;
        } else {
            has_pending = true;
        }
    }
    if has_pending || current != 0 {
        Err(Error::IncompleteNumber)
    } else {
        Ok(result)
    }
}
