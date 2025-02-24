const ONES: &[&str] = &[
    "", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
    "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
];

const TENS: &[&str] = &[
    "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
];

const SCALES: &[&str] = &[
    "", "thousand", "million", "billion", "trillion", "quadrillion", "quintillion",
];

fn three_digits(n: u64) -> String {
    let hundreds = n / 100;
    let remainder = n % 100;
    let mut result = String::new();
    
    if hundreds > 0 {
        result.push_str(ONES[hundreds as usize]);
        result.push_str(" hundred");
        if remainder > 0 {
            result.push(' ');
        }
    }
    
    if remainder > 0 {
        if remainder < 20 {
            result.push_str(ONES[remainder as usize]);
        } else {
            let tens = remainder / 10;
            let ones = remainder % 10;
            result.push_str(TENS[tens as usize]);
            if ones > 0 {
                result.push('-');
                result.push_str(ONES[ones as usize]);
            }
        }
    }
    
    result
}

pub fn encode(n: u64) -> String {
    if n == 0 {
        return "zero".to_string();
    }
    
    let mut parts = Vec::new();
    let mut remaining = n;
    let mut scale_index = 0;
    
    while remaining > 0 {
        let chunk = remaining % 1000;
        remaining /= 1000;
        
        if chunk > 0 {
            let mut part = three_digits(chunk);
            if scale_index > 0 {
                part.push(' ');
                part.push_str(SCALES[scale_index]);
            }
            parts.push(part);
        }
        
        scale_index += 1;
    }
    
    parts.reverse();
    parts.join(" ")
}
