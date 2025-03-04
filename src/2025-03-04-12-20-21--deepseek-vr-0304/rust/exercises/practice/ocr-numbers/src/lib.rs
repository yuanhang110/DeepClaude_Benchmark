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
        let group = chunk;
        if group.len() != 4 {
            return Err(Error::InvalidRowCount(group.len()));
        }

        let width = group[0].len();
        for line in group {
            if line.len() != width {
                return Err(Error::InvalidColumnCount(line.len()));
            }
        }

        if width % 3 != 0 {
            return Err(Error::InvalidColumnCount(width));
        }

        let num_digits = width / 3;
        let mut number = String::new();
        for i in 0..num_digits {
            let mut rows = Vec::new();
            for j in 0..3 {
                let start = i * 3;
                let end = start + 3;
                if end > group[j].len() {
                    return Err(Error::InvalidColumnCount(group[j].len()));
                }
                rows.push(&group[j][start..end]);
            }
            let pattern = rows.join("");
            let digit = match pattern.as_str() {
                " _ | ||_|" => '0',
                "     |  |" => '1',
                " _  _||_ " => '2',
                " _  _| _|" => '3',
                "   |_|  |" => '4',
                " _ |_  _|" => '5',
                " _ |_ |_|" => '6',
                " _   |  |" => '7',
                " _ |_||_|" => '8',
                " _ |_| _|" => '9',
                _ => '?',
            };
            number.push(digit);
        }
        result.push(number);
    }

    Ok(result.join(","))
}
