use std::cmp::Ordering;
use std::ops::{Add, Sub, Mul};

/// Type implementing arbitrary-precision decimal arithmetic
#[derive(Debug, Clone)]
pub struct Decimal {
    integer: Vec<u8>,    // Digits before decimal point
    fractional: Vec<u8>, // Digits after decimal point
    negative: bool,      // Sign of the number
}

impl Decimal {
    pub fn try_from(input: &str) -> Option<Decimal> {
        let mut input = input.trim();
        if input.is_empty() {
            return None;
        }

        // Handle negative sign
        let negative = input.starts_with('-');
        if negative {
            input = &input[1..];
        }

        // Split into integer and fractional parts
        let parts: Vec<&str> = input.split('.').collect();
        if parts.len() > 2 {
            return None;
        }

        // Parse integer part
        let integer = parts[0].chars()
            .map(|c| c.to_digit(10).map(|d| d as u8))
            .collect::<Option<Vec<u8>>>()?;

        // Parse fractional part if exists
        let fractional = if parts.len() == 2 {
            parts[1].chars()
                .map(|c| c.to_digit(10).map(|d| d as u8))
                .collect::<Option<Vec<u8>>>()?
        } else {
            Vec::new()
        };

        // Remove leading zeros from integer part
        let mut integer = integer;
        while integer.len() > 1 && integer[0] == 0 {
            integer.remove(0);
        }

        // Remove trailing zeros from fractional part
        let mut fractional = fractional;
        while fractional.len() > 0 && fractional[fractional.len() - 1] == 0 {
            fractional.pop();
        }

        Some(Decimal {
            integer,
            fractional,
            negative,
        })
    }

    fn normalize(&mut self) {
        // Remove leading zeros from integer part
        while self.integer.len() > 1 && self.integer[0] == 0 {
            self.integer.remove(0);
        }

        // Remove trailing zeros from fractional part
        while self.fractional.len() > 0 && self.fractional[self.fractional.len() - 1] == 0 {
            self.fractional.pop();
        }
    }

    fn align_fractional(&self, other: &Self) -> (Vec<u8>, Vec<u8>, usize) {
        let max_len = self.fractional.len().max(other.fractional.len());
        let mut a = self.fractional.clone();
        let mut b = other.fractional.clone();

        while a.len() < max_len {
            a.push(0);
        }
        while b.len() < max_len {
            b.push(0);
        }

        (a, b, max_len)
    }
}

impl PartialEq for Decimal {
    fn eq(&self, other: &Self) -> bool {
        if self.negative != other.negative {
            return false;
        }
        self.integer == other.integer && self.fractional == other.fractional
    }
}

impl Eq for Decimal {}

impl PartialOrd for Decimal {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.cmp(other))
    }
}

impl Ord for Decimal {
    fn cmp(&self, other: &Self) -> Ordering {
        if self.negative != other.negative {
            return if self.negative { Ordering::Less } else { Ordering::Greater };
        }

        match self.integer.len().cmp(&other.integer.len()) {
            Ordering::Less => if self.negative { Ordering::Greater } else { Ordering::Less },
            Ordering::Greater => if self.negative { Ordering::Less } else { Ordering::Greater },
            Ordering::Equal => {
                for i in 0..self.integer.len() {
                    match self.integer[i].cmp(&other.integer[i]) {
                        Ordering::Less => return if self.negative { Ordering::Greater } else { Ordering::Less },
                        Ordering::Greater => return if self.negative { Ordering::Less } else { Ordering::Greater },
                        Ordering::Equal => continue,
                    }
                }

                let (a_frac, b_frac, _) = self.align_fractional(other);
                a_frac.cmp(&b_frac)
            }
        }
    }
}

impl Add for Decimal {
    type Output = Self;

    fn add(self, other: Self) -> Self {
        if self.negative != other.negative {
            return if self.negative {
                other - self.abs()
            } else {
                self - other.abs()
            };
        }

        let (a_frac, b_frac, frac_len) = self.align_fractional(&other);
        let mut result = Decimal {
            integer: Vec::new(),
            fractional: Vec::new(),
            negative: self.negative,
        };

        let mut carry = 0;
        // Add fractional parts
        for i in (0..frac_len).rev() {
            let sum = a_frac[i] + b_frac[i] + carry;
            result.fractional.insert(0, sum % 10);
            carry = sum / 10;
        }

        // Add integer parts
        let max_len = self.integer.len().max(other.integer.len());
        for i in 0..max_len {
            let a = self.integer.get(self.integer.len() - 1 - i).unwrap_or(&0);
            let b = other.integer.get(other.integer.len() - 1 - i).unwrap_or(&0);
            let sum = a + b + carry;
            result.integer.insert(0, sum % 10);
            carry = sum / 10;
        }

        if carry > 0 {
            result.integer.insert(0, carry);
        }

        result.normalize();
        result
    }
}

impl Sub for Decimal {
    type Output = Self;

    fn sub(self, other: Self) -> Self {
        if self.negative != other.negative {
            return if self.negative {
                let mut result = self.abs() + other.abs();
                result.negative = true;
                result
            } else {
                self + other.abs()
            };
        }

        if self.abs() < other.abs() {
            let mut result = other - self.clone();
            result.negative = !self.negative;
            return result;
        }

        let (a_frac, b_frac, frac_len) = self.align_fractional(&other);
        let mut result = Decimal {
            integer: Vec::new(),
            fractional: Vec::new(),
            negative: self.negative,
        };

        let mut borrow = 0;
        // Subtract fractional parts
        for i in (0..frac_len).rev() {
            let mut a = a_frac[i] - borrow;
            borrow = 0;
            if a < b_frac[i] {
                a += 10;
                borrow = 1;
            }
            result.fractional.insert(0, a - b_frac[i]);
        }

        // Subtract integer parts
        let max_len = self.integer.len().max(other.integer.len());
        for i in 0..max_len {
            let mut a = *self.integer.get(self.integer.len() - 1 - i).unwrap_or(&0);
            let b = *other.integer.get(other.integer.len() - 1 - i).unwrap_or(&0);
            if a < b + borrow {
                a += 10;
                result.integer.insert(0, a - b - borrow);
                borrow = 1;
            } else {
                result.integer.insert(0, a - b - borrow);
                borrow = 0;
            }
        }

        result.normalize();
        result
    }
}

impl Mul for Decimal {
    type Output = Self;

    fn mul(self, other: Self) -> Self {
        let mut result = Decimal {
            integer: vec![0],
            fractional: Vec::new(),
            negative: self.negative != other.negative,
        };

        // Multiply fractional parts
        let frac_len = self.fractional.len() + other.fractional.len();
        let mut carry = 0;

        // Multiply each digit of self with each digit of other
        for (i, a) in self.integer.iter().chain(&self.fractional).rev().enumerate() {
            for (j, b) in other.integer.iter().chain(&other.fractional).rev().enumerate() {
                let product = *a * *b + carry;
                let pos = i + j;
                if pos < result.integer.len() {
                    let sum = result.integer[pos] + product;
                    result.integer[pos] = sum % 10;
                    carry = sum / 10;
                } else {
                    result.integer.push(product % 10);
                    carry = product / 10;
                }
            }
            if carry > 0 {
                result.integer.push(carry);
                carry = 0;
            }
        }

        // Adjust decimal point
        let integer_len = result.integer.len();
        if frac_len > 0 {
            if frac_len >= integer_len {
                result.fractional = result.integer;
                result.integer = vec![0];
                let zeros_to_add = frac_len - integer_len;
                for _ in 0..zeros_to_add {
                    result.fractional.insert(0, 0);
                }
            } else {
                let split_point = integer_len - frac_len;
                result.fractional = result.integer.split_off(split_point);
            }
        }

        result.normalize();
        result
    }
}

impl Decimal {
    fn abs(&self) -> Self {
        Self {
            integer: self.integer.clone(),
            fractional: self.fractional.clone(),
            negative: false,
        }
    }
}
