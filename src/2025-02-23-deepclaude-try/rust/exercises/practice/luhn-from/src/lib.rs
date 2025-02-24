pub struct Luhn(String);

impl Luhn {
    pub fn is_valid(&self) -> bool {
        if self.0.len() < 2 || !self.0.chars().all(|c| c.is_ascii_digit()) {
            return false;
        }
        
        let sum: u32 = self.0.chars()
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

impl From<&str> for Luhn {
    fn from(input: &str) -> Self {
        Self(input.replace(' ', ""))
    }
}

impl From<String> for Luhn {
    fn from(input: String) -> Self {
        Self(input.replace(' ', ""))
    }
}

macro_rules! impl_from_unsigned_int {
    ($($t:ty)*) => {
        $(
            impl From<$t> for Luhn {
                fn from(input: $t) -> Self {
                    Self(input.to_string())
                }
            }
        )*
    };
}

impl_from_unsigned_int!(u8 u16 u32 u64 u128 usize);
