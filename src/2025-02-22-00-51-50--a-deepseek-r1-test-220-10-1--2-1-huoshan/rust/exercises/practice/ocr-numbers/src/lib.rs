// The code below is a stub. Just enough to satisfy the compiler.
// In order to pass the tests you can add-to or change any of this code.

#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    InvalidRowCount(usize),
    InvalidColumnCount(usize),
}

pub fn convert(input: &str) -> Result<String, Error> {
    let lines: Vec<&str> = input.lines().collect();
    if lines.len() % 4 != 0 {
        return Err(Error::InvalidRowCount(lines.len()));
    }
    
    let mut result = Vec::new();
    
    for chunk in lines.chunks(4) {
        let group_lines = chunk;
        let row_count = group_lines.len();
        if row_count != 4 {
            return Err(Error::InvalidRowCount(row_count));
        }
        
        let col_count = group_lines[0].len();
        if col_count % 3 != 0 {
            return Err(Error::InvalidColumnCount(col_count));
        }
        
        for line in group_lines.iter().skip(1) {
            if line.len() != col_count {
                return Err(Error::InvalidColumnCount(line.len()));
            }
        }
        
        let mut digits = String::new();
        for i in 0..(col_count / 3) {
            let offset = i * 3;
            let rows: Vec<&str> = group_lines.iter()
                .take(3)
                .map(|line| &line[offset..offset+3])
                .collect();
                
            let digit = match (rows[0], rows[1], rows[2]) {
                (" _ ", "| |", "|_|") => '0',
                ("   ", "  |", "  |") => '1',
                (" _ ", " _|", "|_ ") => '2',
                (" _ ", " _|", " _|") => '3',
                ("   ", "|_|", "  |") => '4',
                (" _ ", "|_ ", " _|") => '5',
                (" _ ", "|_ ", "|_|") => '6',
                (" _ ", "  |", "  |") => '7',
                (" _ ", "|_|", "|_|") => '8',
                (" _ ", "|_|", " _|") => '9',
                _ => '?',
            };
            digits.push(digit);
        }
        result.push(digits);
    }
    
    Ok(result.join(","))
}
