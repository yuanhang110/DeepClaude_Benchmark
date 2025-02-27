use std::cmp::Ordering;
use std::ops::{Add, Mul, Sub};

/// Type implementing arbitrary-precision decimal arithmetic
pub struct Decimal {
    sign: i8,      // 1 or -1
    digits: String, // Stores digits without leading zeros (except for zero)
    scale: usize,  // Number of decimal places
}

impl Decimal {
    pub fn try_from(input: &str) -> Option<Decimal> {
        let input = input.trim();
        if input.is_empty() {
            return None;
        }

        // Parse sign
        let (sign, rest) = match input.chars().next() {
            Some('-') => (-1, &input[1..]),
            _ => (1, input),
        };

        // Split into integer and fractional parts
        let parts: Vec<&str> = rest.split('.').collect();
        if parts.len() > 2 {
            return None; // Multiple dots
        }

        let integer_part = parts[0];
        let fractional_part = parts.get(1).unwrap_or(&"");

        // Validate parts contain only digits
        if !integer_part.chars().all(|c| c.is_ascii_digit()) 
            || !fractional_part.chars().all(|c| c.is_ascii_digit()) {
            return None;
        }

        // Combine parts into digits string
        let mut digits = String::new();
        digits.push_str(integer_part);
        digits.push_str(fractional_part);

        // Strip leading zeros (but keep one if all zeros)
        let trimmed = digits.trim_start_matches('0');
        digits = if trimmed.is_empty() {
            "0".to_string()
        } else {
            trimmed.to_string()
        };

        Some(Decimal {
            sign,
            digits,
            scale: fractional_part.len(),
        })
    }
}

// Implement required traits
impl PartialEq for Decimal {
    fn eq(&self, other: &Self) -> bool {
        // Two decimals are equal if they represent the same value
        let max_scale = self.scale.max(other.scale);
        let self_scaled = format!("{}{}", self.digits, "0".repeat(max_scale - self.scale));
        let other_scaled = format!("{}{}", other.digits, "0".repeat(max_scale - other.scale));
        
        self.sign == other.sign && self_scaled == other_scaled
    }
}

impl PartialOrd for Decimal {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        if self.sign != other.sign {
            return Some(self.sign.cmp(&other.sign));
        }

        let max_scale = self.scale.max(other.scale);
        let self_scaled = format!("{}{}", self.digits, "0".repeat(max_scale - self.scale));
        let other_scaled = format!("{}{}", other.digits, "0".repeat(max_scale - other.scale));

        Some(match self_scaled.len().cmp(&other_scaled.len()) {
            Ordering::Equal => {
                let ord = self_scaled.cmp(&other_scaled);
                if self.sign == -1 { ord.reverse() } else { ord }
            },
            o => if self.sign == -1 { o.reverse() } else { o }
        })
    }
}

impl Add for Decimal {
    type Output = Self;

    fn add(self, _other: Self) -> Self {
        todo!("Addition not implemented") // Requires arbitrary-precision arithmetic
    }
}

impl Sub for Decimal {
    type Output = Self;

    fn sub(self, _other: Self) -> Self {
        todo!("Subtraction not implemented") // Requires arbitrary-precision arithmetic
    }
}

impl Mul for Decimal {
    type Output = Self;

    fn mul(self, _other: Self) -> Self {
        todo!("Multiplication not implemented") // Requires arbitrary-precision arithmetic
    }
}
