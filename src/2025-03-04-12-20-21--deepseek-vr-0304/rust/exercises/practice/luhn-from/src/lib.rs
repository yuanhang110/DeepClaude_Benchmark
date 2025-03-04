pub struct Luhn(String);

impl Luhn {
    pub fn is_valid(&self) -> bool {
        let s = self.0.replace(' ', "");
        if s.len() < 2 || !s.chars().all(|c| c.is_ascii_digit()) {
            return false;
        }

        let digits: Vec<u8> = s.chars()
            .filter_map(|c| c.to_digit(10).map(|d| d as u8))
            .collect();

        let sum: u32 = digits.iter()
            .rev()
            .enumerate()
            .map(|(i, &d)| {
                if i % 2 == 1 {
                    let doubled = d * 2;
                    (doubled / 10 + doubled % 10) as u32
                } else {
                    d as u32
                }
            })
            .sum();

        sum % 10 == 0
    }
}

/// Here is the example of how the From trait could be implemented
/// for the &str type. Naturally, you can implement this trait
/// by hand for every other type presented in the test suite,
/// but your solution will fail if a new type is presented.
/// Perhaps there exists a better solution for this problem?
impl<T: ToString> From<T> for Luhn {
    fn from(input: T) -> Self {
        Luhn(input.to_string())
    }
}
