pub struct Luhn(String);

impl Luhn {
    pub fn is_valid(&self) -> bool {
        let s = self.0.replace(' ', "");
        if s.len() < 2 || s.chars().any(|c| !c.is_ascii_digit()) {
            return false;
        }
        
        let sum = s.chars()
            .rev()
            .filter_map(|c| c.to_digit(10))
            .enumerate()
            .map(|(i, n)| match i % 2 {
                0 => n,
                _ => {
                    let doubled = n * 2;
                    if doubled > 9 { doubled - 9 } else { doubled }
                }
            })
            .sum::<u32>();
        
        sum % 10 == 0
    }
}

/// Implement From trait for any type that implements Display
/// This allows conversion from &str, String, numeric types, etc.
impl<T: std::fmt::Display> From<T> for Luhn {
    fn from(input: T) -> Self {
        Luhn(input.to_string())
    }
}
