pub struct Luhn {
    code: String,
}

impl Luhn {
    pub fn is_valid(&self) -> bool {
        let code = self.code.replace(" ", "");
        if code.len() <= 1 || code.chars().any(|c| !c.is_digit(10)) {
            return false;
        }

        let sum: u32 = code
            .chars()
            .rev()
            .filter_map(|c| c.to_digit(10))
            .enumerate()
            .map(|(i, digit)| {
                if i % 2 == 1 {
                    let doubled = digit * 2;
                    if doubled > 9 {
                        doubled - 9
                    } else {
                        doubled
                    }
                } else {
                    digit
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
impl From<&str> for Luhn {
    fn from(input: &str) -> Self {
        Luhn {
            code: input.to_string(),
        }
    }
}

impl From<String> for Luhn {
    fn from(input: String) -> Self {
        Luhn { code: input }
    }
}

impl From<u8> for Luhn {
    fn from(input: u8) -> Self {
        Luhn {
            code: input.to_string(),
        }
    }
}

impl From<u16> for Luhn {
    fn from(input: u16) -> Self {
        Luhn {
            code: input.to_string(),
        }
    }
}

impl From<u32> for Luhn {
    fn from(input: u32) -> Self {
        Luhn {
            code: input.to_string(),
        }
    }
}

impl From<u64> for Luhn {
    fn from(input: u64) -> Self {
        Luhn {
            code: input.to_string(),
        }
    }
}

impl From<usize> for Luhn {
    fn from(input: usize) -> Self {
        Luhn {
            code: input.to_string(),
        }
    }
}
