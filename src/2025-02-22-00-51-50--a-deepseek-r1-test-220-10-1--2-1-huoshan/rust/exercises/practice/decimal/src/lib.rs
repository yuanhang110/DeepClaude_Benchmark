/// Type implementing arbitrary-precision decimal arithmetic
#[derive(Debug, Clone)]
pub struct Decimal {
    value: String,  // Stores the number as a string without decimal point
    scale: u32,     // Number of digits after decimal point
}

impl Decimal {
    pub fn try_from(input: &str) -> Option<Decimal> {
        // Check for valid format
        if !input.chars().all(|c| c.is_ascii_digit() || c == '-' || c == '.' || c == '+') {
            return None;
        }

        // Split into sign, integer and fractional parts
        let (sign, num_str) = match input.strip_prefix('-') {
            Some(s) => (true, s),
            None => (false, input.strip_prefix('+').unwrap_or(input)),
        };

        let parts: Vec<&str> = num_str.split('.').collect();
        if parts.len() > 2 {
            return None;  // Multiple decimal points
        }

        let integer_part = parts[0].trim_start_matches('0');
        let fractional_part = parts.get(1).unwrap_or(&"").trim_end_matches('0');

        // Handle special case of zero
        if integer_part.is_empty() && fractional_part.is_empty() {
            return Some(Decimal {
                value: "0".to_string(),
                scale: 0,
            });
        }

        // Combine integer and fractional parts
        let combined = format!("{}{}", integer_part, fractional_part);
        if combined.is_empty() {
            return Some(Decimal {
                value: "0".to_string(),
                scale: 0,
            });
        }

        let scale = fractional_part.len() as u32;
        let value = if sign {
            format!("-{}", combined)
        } else {
            combined
        };

        Some(Decimal { value, scale })
    }
}

use std::cmp::Ordering;
use std::ops::{Add, Sub, Mul};

impl PartialEq for Decimal {
    fn eq(&self, other: &Self) -> bool {
        self.normalized_string() == other.normalized_string()
    }
}

impl PartialOrd for Decimal {
    fn partial_cmp(&self, other: &Self) -> Option<Ordering> {
        Some(self.normalized_string().cmp(&other.normalized_string()))
    }
}

impl Add for Decimal {
    type Output = Self;

    fn add(self, rhs: Self) -> Self::Output {
        let (a, b) = self.align_scales(&rhs);
        let sum = (a.parse::<i128>().unwrap_or(0) + b.parse::<i128>().unwrap_or(0)).to_string();
        Decimal {
            value: sum,
            scale: self.scale.max(rhs.scale),
        }
    }
}

impl Sub for Decimal {
    type Output = Self;

    fn sub(self, rhs: Self) -> Self::Output {
        let (a, b) = self.align_scales(&rhs);
        let diff = (a.parse::<i128>().unwrap_or(0) - b.parse::<i128>().unwrap_or(0)).to_string();
        Decimal {
            value: diff,
            scale: self.scale.max(rhs.scale),
        }
    }
}

impl Mul for Decimal {
    type Output = Self;

    fn mul(self, rhs: Self) -> Self::Output {
        let product = (self.parse_to_int() * rhs.parse_to_int()).to_string();
        Decimal {
            value: product,
            scale: self.scale + rhs.scale,
        }
    }
}

impl Decimal {
    fn parse_to_int(&self) -> i128 {
        self.value.parse().unwrap_or(0)
    }

    fn align_scales(&self, other: &Self) -> (String, String) {
        let max_scale = self.scale.max(other.scale);
        let a = self.adjust_scale(max_scale);
        let b = other.adjust_scale(max_scale);
        (a.value, b.value)
    }

    fn adjust_scale(&self, new_scale: u32) -> Self {
        let scale_diff = new_scale as i32 - self.scale as i32;
        if scale_diff == 0 {
            return self.clone();
        }

        let mut new_value = self.value.clone();
        if scale_diff > 0 {
            new_value.push_str(&"0".repeat(scale_diff as usize));
        } else {
            let split_pos = new_value.len() as i32 + scale_diff;
            if split_pos > 0 {
                new_value = new_value[..split_pos as usize].to_string();
            } else {
                new_value = "0".to_string();
            }
        }

        Decimal {
            value: new_value,
            scale: new_scale,
        }
    }

    fn normalized_string(&self) -> String {
        let mut s = self.value.clone();
        if self.scale > 0 {
            let len = s.len();
            let scale = self.scale as usize;
            if len > scale {
                s.insert(len - scale, '.');
            } else {
                s = format!("0.{}{}", "0".repeat(scale - len), s);
            }
        }
        s.trim_end_matches('0')
         .trim_end_matches('.')
         .to_string()
    }
}
