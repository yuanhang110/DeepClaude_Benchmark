#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    IncompleteNumber,
}

/// Convert a list of numbers to a stream of bytes encoded with variable length encoding.
pub fn to_bytes(values: &[u32]) -> Vec<u8> {
    let mut bytes = Vec::new();
    for &value in values {
        if value == 0 {
            bytes.push(0);
            continue;
        }
        let mut chunks = Vec::new();
        let mut n = value;
        while n > 0 {
            chunks.push((n % 128) as u8);
            n /= 128;
        }
        chunks.reverse();
        for i in 0..chunks.len() {
            if i != chunks.len() - 1 {
                chunks[i] |= 0x80;
            }
        }
        bytes.extend_from_slice(&chunks);
    }
    bytes
}

/// Given a stream of bytes, extract all numbers which are encoded in there.
pub fn from_bytes(bytes: &[u8]) -> Result<Vec<u32>, Error> {
    let mut result = Vec::new();
    let mut current_num = 0;
    let mut in_progress = false;
    for &byte in bytes {
        let value = (byte & 0x7F) as u32;
        current_num = current_num << 7 | value;
        in_progress = true;
        if (byte & 0x80) == 0 {
            result.push(current_num);
            current_num = 0;
            in_progress = false;
        }
    }
    if in_progress {
        Err(Error::IncompleteNumber)
    } else {
        Ok(result)
    }
}
