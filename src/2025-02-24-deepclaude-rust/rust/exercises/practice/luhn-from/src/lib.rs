pub struct Luhn {
    is_valid: bool,
}

impl Luhn {
    pub fn is_valid(&self) -> bool {
        self.is_valid
    }
}

/// Implement From for any type that can be converted to a String
impl<T: ToString> From<T> for Luhn {
    fn from(input: T) -> Self {
        let s = input.to_string();
        
        // Check for any invalid characters (non-digits and non-spaces)
        if s.chars().any(|c| !c.is_ascii_digit() && !c.is_whitespace()) {
            return Luhn { is_valid: false };
        }
        
        let digits: Vec<char> = s.chars().filter(|c| c.is_ascii_digit()).collect();
        
        // Luhn algorithm requires at least two digits
        if digits.len() < 2 {
            return Luhn { is_valid: false };
        }
        
        let sum: u32 = digits
            .iter()
            .rev()
            .enumerate()
            .map(|(i, &c)| {
                let digit = c.to_digit(10).unwrap();
                if i % 2 == 1 { // even positions in original (since we reversed)
                    let doubled = digit * 2;
                    if doubled > 9 { doubled - 9 } else { doubled }
                } else {
                    digit
                }
            })
            .sum();
        
        Luhn { is_valid: sum % 10 == 0 }
    }
}
