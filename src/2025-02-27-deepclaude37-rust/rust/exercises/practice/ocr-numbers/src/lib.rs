// The code below is a stub. Just enough to satisfy the compiler.
// In order to pass the tests you can add-to or change any of this code.

#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    InvalidRowCount(usize),
    InvalidColumnCount(usize),
}

pub fn convert(input: &str) -> Result<String, Error> {
    let lines: Vec<&str> = input.lines().collect();
    let num_rows = lines.len();
    
    // Each "number" consists of 4 rows (the last one being blank)
    if num_rows % 4 != 0 {
        return Err(Error::InvalidRowCount(num_rows));
    }
    
    let mut result = Vec::new();
    
    // Process each group of 4 rows as a number
    for chunk in lines.chunks(4) {
        // We only need the first 3 rows for recognition (4th is blank)
        let rows = &chunk[0..3];
        
        // All rows should have the same length, and it should be multiple of 3
        let width = rows[0].len();
        if width % 3 != 0 || rows.iter().any(|r| r.len() != width) {
            return Err(Error::InvalidColumnCount(width));
        }
        
        let mut number = String::new();
        
        // Process each digit (3 columns wide)
        for i in 0..(width / 3) {
            let start = i * 3;
            let end = start + 3;
            
            // Extract the 3x3 grid for this digit
            let digit_rows: Vec<&str> = rows.iter()
                .map(|row| {
                    if start < row.len() && end <= row.len() {
                        &row[start..end]
                    } else {
                        "   " // Handle potential out-of-bounds
                    }
                })
                .collect();
            
            // Match the pattern to a digit
            let digit = match digit_rows.as_slice() {
                [" _ ", "| |", "|_|"] => '0',
                ["   ", "  |", "  |"] => '1',
                [" _ ", " _|", "|_ "] => '2',
                [" _ ", " _|", " _|"] => '3',
                ["   ", "|_|", "  |"] => '4',
                [" _ ", "|_ ", " _|"] => '5',
                [" _ ", "|_ ", "|_|"] => '6',
                [" _ ", "  |", "  |"] => '7',
                [" _ ", "|_|", "|_|"] => '8',
                [" _ ", "|_|", " _|"] => '9',
                _ => '?',
            };
            
            number.push(digit);
        }
        
        result.push(number);
    }
    
    Ok(result.join(","))
}
