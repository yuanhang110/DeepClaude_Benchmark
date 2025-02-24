pub struct Luhn(String);

impl Luhn {
    pub fn is_valid(&self) -> bool {
        let s = &self.0;
        if s.len() < 2 || s.chars().any(|c| !c.is_ascii_digit()) {
            return false;
        }

        let sum: u32 = s
            .chars()
            .rev()
            .enumerate()
            .map(|(i, c)| {
                let digit = c.to_digit(10).unwrap();
                if i % 2 == 1 {
                    let doubled = digit * 2;
                    if doubled > 9 { doubled - 9 } else { doubled }
                } else {
                    digit
                }
            })
            .sum();

        sum % 10 == 0
    }
}

impl<T: ToString> From<T> for Luhn {
    fn from(input: T) -> Self {
        let cleaned = input.to_string().replace(' ', "");
        Luhn(cleaned)
    }
}
