// The code below is a stub. Just enough to satisfy the compiler.
// In order to pass the tests you can add-to or change any of this code.

#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    InvalidRowCount(usize),
    InvalidColumnCount(usize),
}

pub fn convert(input: &str) -> Result<String, Error> {
    let lines: Vec<&str> = input.lines().collect();
    
    // Validate row count
    if lines.len() % 4 != 0 {
        return Err(Error::InvalidRowCount(lines.len()));
    }
    
    // Validate column count and uniformity
    let cols = lines.first().map(|s| s.len()).unwrap_or(0);
    if cols % 3 != 0 {
        return Err(Error::InvalidColumnCount(cols));
    }
    if lines.iter().any(|line| line.len() != cols) {
        return Err(Error::InvalidColumnCount(cols));
    }
    
    let mut result = Vec::new();
    
    // Process each 4-line chunk as a separate line of digits
    for chunk in lines.chunks(4) {
        let mut line = String::new();
        
        // Check each 3-column group across all 4 lines
        for i in 0..(cols / 3) {
            let mut digit_lines = Vec::with_capacity(4);
            for row in chunk {
                let start = i * 3;
                let end = start + 3;
                digit_lines.push(&row[start..end]);
            }
            
            line.push(match_digit(&digit_lines));
        }
        
        result.push(line);
    }
    
    // Join lines with commas between them
    Ok(result.join(","))
}

fn match_digit(rows: &[&str]) -> char {
    match (rows[0], rows[1], rows[2], rows[3]) {
        (" _ ", "| |", "|_|", "   ") => '0',
        ("   ", "  |", "  |", "   ") => '1',
        (" _ ", " _|", "|_ ", "   ") => '2',
        (" _ ", " _|", " _|", "   ") => '3',
        ("   ", "|_|", "  |", "   ") => '4',
        (" _ ", "|_ ", " _|", "   ") => '5',
        (" _ ", "|_ ", "|_|", "   ") => '6',
        (" _ ", "  |", "  |", "   ") => '7',
        (" _ ", "|_|", "|_|", "   ") => '8',
        (" _ ", "|_|", " _|", "   ") => '9',
        _ => '?',
    }
}
