use std::cmp::Ordering;
use std::ops::{Add, Sub, Mul};

/// Type implementing arbitrary-precision decimal arithmetic
pub struct Decimal {
    // Store significant digits without leading zeros
    digits: Vec<u8>,
    // Number of digits after the decimal point
    exponent: i32,
    // Whether the number is negative
    negative: bool,
}

impl Decimal {
    pub fn try_from(input: &str) -> Option<Decimal> {
        let input = input.trim();
        if input.is_empty() {
            return None;
        }

        // Parse sign
        let (negative, rest) = match input.chars().next()? {
            '-' => (true, &input[1..]),
            '+' => (false, &input[1..]),
            _ => (false, input),
        };

        if rest.is_empty() {
            return None; // Only sign present
        }

        // Split into integer and fractional parts
        let parts: Vec<&str> = rest.split('.').collect();
        if parts.len() > 2 {
            return None; // More than one decimal point
        }

        let integer_part = parts[0];
        let fractional_part = parts.get(1).unwrap_or(&"");

        // Validate parts contain only digits
        if !integer_part.chars().all(|c| c.is_ascii_digit()) ||
           !fractional_part.chars().all(|c| c.is_ascii_digit()) {
            return None;
        }

        // Need at least one digit in the number
        if integer_part.is_empty() && fractional_part.is_empty() {
            return None;
        }

        // Trim leading zeros from integer part and trailing zeros from fractional part
        let integer_trimmed = integer_part.trim_start_matches('0');
        let fractional_trimmed = fractional_part.trim_end_matches('0');

        // Combine digits
        let mut digits = Vec::new();
        digits.extend(integer_trimmed.chars().filter_map(|c| c.to_digit(10).map(|d| d as u8)));
        digits.extend(fractional_trimmed.chars().filter_map(|c| c.to_digit(10).map(|d| d as u8)));

        // Remove leading zeros from combined digits
        let leading_zeros = digits.iter().take_while(|&&d| d == 0).count();
        digits.drain(0..leading_zeros);

        // Handle zero case
        if digits.is_empty() {
            return Some(Decimal {
                digits: vec![],
                exponent: 0,
                negative: false, // -0 is treated as 0
            });
        }

        // Calculate exponent (number of digits in fractional part after trimming)
        let exponent = fractional_trimmed.len() as i32;

        Some(Decimal {
            digits,
            exponent,
            negative,
        })
    }

    // Helper method to normalize decimal representation
    fn normalize(&mut self) {
        // Remove trailing zeros from digits and adjust exponent
        while self.exponent > 0 && self.digits.last() == Some(&0) {
            self.digits.pop();
            self.exponent -= 1;
        }

        // Remove leading zeros
        let leading_zeros = self.digits.iter().take_while(|&&d| d == 0).count();
        self.digits.drain(0..leading_zeros);

        // If no digits remain, this is zero
        if self.digits.is_empty() {
            self.exponent = 0;
            self.negative = false;
        }
    }
}

// Implementation of PartialEq for Decimal
impl PartialEq for Decimal {
    fn eq(&self, other: &Self) -> bool {
        // If both are empty (zero), they're equal
        if self.digits.is_empty() && other.digits.is_empty() {
            return true;
        }

        // Different signs mean not equal
        if self.negative != other.negative {
            return false;
        }

        // Compare digits and exponents
        if self.exponent != other.exponent {
            return false;
        }

        self.digits == other.digits
    }
}

impl Eq for Decimal {}

// Implementation of PartialOrd for Decimal
impl PartialOrd for Decimal {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

// Implementation of Ord for Decimal
impl Ord for Decimal {
    fn cmp(&self, other: &Self) -> Ordering {
        // Handle zero cases
        if self.digits.is_empty() && other.digits.is_empty() {
            return Ordering::Equal;
        }
        if self.digits.is_empty() {
            return if other.negative { Ordering::Greater } else { Ordering::Less };
        }
        if other.digits.is_empty() {
            return if self.negative { Ordering::Less } else { Ordering::Greater };
        }

        // Different signs
        match (self.negative, other.negative) {
            (true, false) => return Ordering::Less,
            (false, true) => return Ordering::Greater,
            _ => {}
        }

        // Same sign, need to compare absolute values
        let mut abs_ordering = Ordering::Equal;

        // For negative numbers, we'll reverse the comparison result later

        // Compare the number of digits before decimal point
        let self_int_digits = self.digits.len() as i32 - self.exponent;
        let other_int_digits = other.digits.len() as i32 - other.exponent;
        
        if self_int_digits != other_int_digits {
            abs_ordering = self_int_digits.cmp(&other_int_digits);
            return if self.negative {
                abs_ordering.reverse()
            } else {
                abs_ordering
            };
        }

        // Compare digits directly
        let mut self_digits = self.digits.clone();
        let mut other_digits = other.digits.clone();

        // Pad with zeros to make comparison easier
        while self_digits.len() < other_digits.len() {
            self_digits.push(0);
        }
        while other_digits.len() < self_digits.len() {
            other_digits.push(0);
        }

        for (s, o) in self_digits.iter().zip(other_digits.iter()) {
            if s != o {
                abs_ordering = s.cmp(o);
                break;
            }
        }

        if self.negative {
            abs_ordering.reverse()
        } else {
            abs_ordering
        }
    }
}

// Implementation of Add for Decimal
impl Add for Decimal {
    type Output = Decimal;

    fn add(self, other: Decimal) -> Decimal {
        // Handle zero cases
        if self.digits.is_empty() {
            return other;
        }
        if other.digits.is_empty() {
            return self;
        }

        // If signs are different, delegate to subtraction
        if self.negative != other.negative {
            if self.negative {
                // -a + b = b - a
                return other.sub(Decimal { digits: self.digits, exponent: self.exponent, negative: false });
            } else {
                // a + (-b) = a - b
                return self.sub(Decimal { digits: other.digits, exponent: other.exponent, negative: false });
            }
        }

        // Align exponents by padding with zeros
        let mut self_digits = self.digits;
        let mut other_digits = other.digits;
        let max_exponent = self.exponent.max(other.exponent);
        
        // Pad with zeros to align decimal points
        while self.exponent < max_exponent {
            self_digits.push(0);
        }
        while other.exponent < max_exponent {
            other_digits.push(0);
        }
        
        // Ensure both vectors have the same length
        while self_digits.len() < other_digits.len() {
            self_digits.insert(0, 0);
        }
        while other_digits.len() < self_digits.len() {
            other_digits.insert(0, 0);
        }
        
        // Add digits
        let mut result_digits = Vec::with_capacity(self_digits.len() + 1);
        let mut carry = 0;
        
        for i in (0..self_digits.len()).rev() {
            let sum = self_digits[i] + other_digits[i] + carry;
            result_digits.push(sum % 10);
            carry = sum / 10;
        }
        
        if carry > 0 {
            result_digits.push(carry);
        }
        
        result_digits.reverse();
        
        let mut result = Decimal {
            digits: result_digits,
            exponent: max_exponent,
            negative: self.negative, // Both operands have the same sign
        };
        
        result.normalize();
        result
    }
}

// Implementation of Sub for Decimal
impl Sub for Decimal {
    type Output = Decimal;

    fn sub(self, other: Decimal) -> Decimal {
        // Handle zero cases
        if self.digits.is_empty() {
            return Decimal { 
                digits: other.digits, 
                exponent: other.exponent, 
                negative: !other.negative 
            };
        }
        if other.digits.is_empty() {
            return self;
        }

        // If signs are different, delegate to addition
        if self.negative != other.negative {
            if self.negative {
                // -a - b = -(a + b)
                let self_neg = self.negative;
                let mut result = self.add(Decimal { 
                    digits: other.digits.clone(), 
                    exponent: other.exponent, 
                    negative: self_neg 
                });
                result.negative = true;
                return result;
            } else {
                // a - (-b) = a + b
                return self.add(Decimal { 
                    digits: other.digits, 
                    exponent: other.exponent, 
                    negative: false 
                });
            }
        }

        // Both operands have the same sign
        // Compare absolute values to determine result sign and order of subtraction
        let abs_comparison = {
            let self_abs = Decimal { 
                digits: self.digits.clone(), 
                exponent: self.exponent, 
                negative: false 
            };
            let other_abs = Decimal { 
                digits: other.digits.clone(), 
                exponent: other.exponent, 
                negative: false 
            };
            self_abs.cmp(&other_abs)
        };

        let (larger, smaller, result_negative) = match abs_comparison {
            Ordering::Less => (&other, &self, !self.negative),
            _ => (&self, &other, self.negative),
        };

        // Align exponents
        let mut larger_digits = larger.digits.clone();
        let mut smaller_digits = smaller.digits.clone();
        let max_exponent = larger.exponent.max(smaller.exponent);
        
        // Pad with zeros to align decimal points
        while larger.exponent < max_exponent {
            larger_digits.push(0);
        }
        while smaller.exponent < max_exponent {
            smaller_digits.push(0);
        }
        
        // Ensure both vectors have the same length
        while larger_digits.len() < smaller_digits.len() {
            larger_digits.insert(0, 0);
        }
        while smaller_digits.len() < larger_digits.len() {
            smaller_digits.insert(0, 0);
        }
        
        // Subtract digits
        let mut result_digits = Vec::with_capacity(larger_digits.len());
        let mut borrow = 0;
        
        for i in (0..larger_digits.len()).rev() {
            let mut diff = larger_digits[i] as i32 - smaller_digits[i] as i32 - borrow;
            borrow = 0;
            if diff < 0 {
                diff += 10;
                borrow = 1;
            }
            result_digits.push(diff as u8);
        }
        
        result_digits.reverse();
        
        let mut result = Decimal {
            digits: result_digits,
            exponent: max_exponent,
            negative: result_negative,
        };
        
        result.normalize();
        result
    }
}

// Implementation of Mul for Decimal
impl Mul for Decimal {
    type Output = Decimal;

    fn mul(self, other: Decimal) -> Decimal {
        // Handle zero cases
        if self.digits.is_empty() || other.digits.is_empty() {
            return Decimal {
                digits: vec![],
                exponent: 0,
                negative: false,
            };
        }

        // Determine sign of result
        let result_negative = self.negative != other.negative;
        
        // Calculate total exponent
        let result_exponent = self.exponent + other.exponent;
        
        // Perform multiplication
        let mut result_digits = vec![0; self.digits.len() + other.digits.len()];
        
        for (i, &d1) in self.digits.iter().rev().enumerate() {
            let mut carry = 0;
            for (j, &d2) in other.digits.iter().rev().enumerate() {
                let pos = i + j;
                let product = result_digits[pos] as u32 + (d1 as u32 * d2 as u32) + carry;
                result_digits[pos] = (product % 10) as u8;
                carry = product / 10;
            }
            
            let mut pos = i + other.digits.len();
            while carry > 0 {
                let sum = result_digits[pos] as u32 + carry;
                result_digits[pos] = (sum % 10) as u8;
                carry = sum / 10;
                pos += 1;
            }
        }
        
        result_digits.reverse();
        
        let mut result = Decimal {
            digits: result_digits,
            exponent: result_exponent,
            negative: result_negative,
        };
        
        result.normalize();
        result
    }
}
