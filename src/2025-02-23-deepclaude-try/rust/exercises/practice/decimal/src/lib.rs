/// Type implementing arbitrary-precision decimal arithmetic
use std::ops::Neg;

#[derive(Debug, Clone)]
pub struct Decimal {
    sign: bool,  // true for positive, false for negative
    digits: Vec<u8>,  // digits stored in reverse order for easier arithmetic
    exponent: i32,  // position of decimal point from the right
}

impl Decimal {
    pub fn try_from(input: &str) -> Option<Decimal> {
        let input = input.trim();
        if input.is_empty() {
            return None;
        }

        // Parse sign
        let (sign, rest) = match input.chars().next()? {
            '-' => (false, &input[1..]),
            '+' => (true, &input[1..]),
            _ => (true, input),
        };

        // Split into integer and fractional parts
        let mut parts = rest.splitn(2, '.');
        let integer_part = parts.next()?;
        let fractional_part = parts.next().unwrap_or("");

        // Validate digits
        if !integer_part.chars().all(|c| c.is_ascii_digit()) 
            || !fractional_part.chars().all(|c| c.is_ascii_digit()) {
            return None;
        }

        // Process integer digits (remove leading zeros)
        let mut integer_digits: Vec<u8> = integer_part
            .chars()
            .filter_map(|c| c.to_digit(10).map(|d| d as u8))
            .collect();
        
        // Remove leading zeros
        while integer_digits.len() > 1 && integer_digits[0] == 0 {
            integer_digits.remove(0);
        }

        // Process fractional digits (remove trailing zeros)
        let mut fractional_digits: Vec<u8> = fractional_part
            .chars()
            .filter_map(|c| c.to_digit(10).map(|d| d as u8))
            .collect();
        
        let original_fractional_len = fractional_digits.len();
        while fractional_digits.last() == Some(&0) {
            fractional_digits.pop();
        }

        // Combine digits and determine exponent
        let mut digits = integer_digits;
        digits.extend(fractional_digits);
        let exponent = original_fractional_len as i32;

        // Handle zero case
        if digits.is_empty() || digits.iter().all(|&d| d == 0) {
            return Some(Decimal {
                sign: true,
                digits: vec![0],
                exponent: 0,
            });
        }

        Some(Decimal {
            sign,
            digits,
            exponent,
        })
    }

    fn align_with(&self, other: &Self) -> (Self, Self) {
        let max_exponent = self.exponent.max(other.exponent);
        (
            self.scale_to(max_exponent),
            other.scale_to(max_exponent),
        )
    }

    fn scale_to(&self, new_exponent: i32) -> Self {
        let mut scaled = self.clone();
        let exponent_diff = new_exponent - scaled.exponent;
        
        if exponent_diff > 0 {
            scaled.digits.resize(scaled.digits.len() + exponent_diff as usize, 0);
            scaled.exponent = new_exponent;
        }
        
        scaled
    }

    fn normalize(&mut self) {
        // Remove trailing zeros
        while self.digits.last() == Some(&0) && self.exponent > 0 {
            self.digits.pop();
            self.exponent -= 1;
        }
        
        // Handle zero
        if self.digits.iter().all(|&d| d == 0) {
            self.sign = true;
            self.digits = vec![0];
            self.exponent = 0;
        }
    }
}

impl PartialEq for Decimal {
    fn eq(&self, other: &Self) -> bool {
        if self.sign != other.sign {
            return false;
        }
        
        let (a, b) = self.align_with(other);
        a.digits == b.digits
    }
}

impl Eq for Decimal {}

impl PartialOrd for Decimal {
    fn partial_cmp(&self, other: &Self) -> Option<std::cmp::Ordering> {
        if self.sign != other.sign {
            return if self.sign {
                Some(std::cmp::Ordering::Greater)
            } else {
                Some(std::cmp::Ordering::Less)
            };
        }
        
        let (a, b) = self.align_with(other);
        Some(if a.digits == b.digits {
            std::cmp::Ordering::Equal
        } else {
            let ordering = a.digits.iter().rev().cmp(b.digits.iter().rev());
            if self.sign { ordering } else { ordering.reverse() }
        })
    }
}

impl Ord for Decimal {
    fn cmp(&self, other: &Self) -> std::cmp::Ordering {
        self.partial_cmp(other).unwrap()
    }
}

impl std::ops::Add for Decimal {
    type Output = Self;

    fn add(self, other: Self) -> Self {
        if self.sign != other.sign {
            // If signs differ, convert to subtraction
            if self.sign {
                // a + (-b) = a - b
                self - other.neg()
            } else {
                // (-a) + b = b - a
                other - self.neg()
            }
        } else {
            // Signs are the same, perform addition
            let (a, b) = self.align_with(&other);
            let mut result = a.clone();
            let mut carry = 0u8;

            for (i, &digit) in b.digits.iter().enumerate() {
                let sum = result.digits[i] as u16 + digit as u16 + carry as u16;
                result.digits[i] = (sum % 10) as u8;
                carry = (sum / 10) as u8;
            }

            if carry > 0 {
                result.digits.push(carry);
            }

            result.normalize();
            result
        }
    }
}

impl std::ops::Neg for Decimal {
    type Output = Self;

    fn neg(mut self) -> Self {
        self.sign = !self.sign;
        self
    }
}

impl std::ops::Sub for Decimal {
    type Output = Self;

    fn sub(self, other: Self) -> Self {
        if self.sign != other.sign {
            // If signs differ, convert to addition
            // a - (-b) = a + b
            // (-a) - b = -(a + b)
            let mut result = self + other.neg();
            result.sign = self.sign;
            result
        } else {
            // Signs are the same, perform subtraction
            let (mut a, b) = self.align_with(&other);
            
            // Determine if we need to negate the result
            let negate = if a.digits.iter().rev().cmp(b.digits.iter().rev()) == std::cmp::Ordering::Less {
                std::mem::swap(&mut a.digits, &mut b.digits);
                true
            } else {
                false
            };

            let mut result = a.clone();
            let mut borrow = 0i8;

            for (i, &digit) in b.digits.iter().enumerate() {
                let mut diff = result.digits[i] as i8 - digit as i8 - borrow;
                if diff < 0 {
                    diff += 10;
                    borrow = 1;
                } else {
                    borrow = 0;
                }
                result.digits[i] = diff as u8;
            }

            result.sign = if negate { !self.sign } else { self.sign };
            result.normalize();
            result
        }
    }
}

impl std::ops::Mul for Decimal {
    type Output = Self;

    fn mul(self, other: Self) -> Self {
        let mut result = Decimal {
            sign: !(self.sign ^ other.sign),  // true if signs are same
            digits: vec![0; self.digits.len() + other.digits.len()],
            exponent: self.exponent + other.exponent,
        };

        for (i, &digit1) in self.digits.iter().enumerate() {
            let mut carry = 0u8;
            for (j, &digit2) in other.digits.iter().enumerate() {
                let prod = result.digits[i + j] as u16 + (digit1 as u16 * digit2 as u16) + carry as u16;
                result.digits[i + j] = (prod % 10) as u8;
                carry = (prod / 10) as u8;
            }
            if carry > 0 {
                result.digits[i + other.digits.len()] = carry;
            }
        }

        result.normalize();
        result
    }
}
