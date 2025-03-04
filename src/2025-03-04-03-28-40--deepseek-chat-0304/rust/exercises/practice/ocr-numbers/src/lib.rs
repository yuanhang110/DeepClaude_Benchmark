#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    InvalidRowCount(usize),
    InvalidColumnCount(usize),
}

const DIGITS: [&str; 10] = [
    " _ | ||_|",  // 0
    "     |  |",  // 1
    " _  _||_ ",  // 2
    " _  _| _|",  // 3
    "   |_|  |",  // 4
    " _ |_  _|",  // 5
    " _ |_ |_|",  // 6
    " _   |  |",  // 7
    " _ |_||_|",  // 8
    " _ |_| _|",  // 9
];

fn validate_input(input: &str) -> Result<(), Error> {
    let lines: Vec<&str> = input.lines().collect();
    if lines.len() % 4 != 0 {
        return Err(Error::InvalidRowCount(lines.len()));
    }
    for line in &lines {
        if line.len() % 3 != 0 {
            return Err(Error::InvalidColumnCount(line.len()));
        }
    }
    Ok(())
}

fn parse_digit(lines: &[&str], pos: usize) -> char {
    let mut pattern = String::new();
    for row in 0..3 {
        pattern.push_str(&lines[row][pos..pos+3]);
    }
    
    DIGITS.iter()
        .position(|&d| d == pattern)
        .map_or('?', |i| (i as u8 + b'0') as char)
}

pub fn convert(input: &str) -> Result<String, Error> {
    validate_input(input)?;
    
    let lines: Vec<&str> = input.lines().collect();
    let mut result = String::new();
    
    for chunk in lines.chunks(4) {
        if !result.is_empty() {
            result.push(',');
        }
        
        let digit_count = chunk[0].len() / 3;
        for i in 0..digit_count {
            result.push(parse_digit(chunk, i * 3));
        }
    }
    
    Ok(result)
}
