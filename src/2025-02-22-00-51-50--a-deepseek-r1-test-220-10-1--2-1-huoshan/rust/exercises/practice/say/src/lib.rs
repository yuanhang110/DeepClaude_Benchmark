const LESS_THAN_TWENTY: &[&str] = &[
    "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
    "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen",
    "nineteen",
];

const TENS: &[&str] = &[
    "zero", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
];

const SCALES: &[&str] = &[
    "", 
    "thousand", 
    "million", 
    "billion", 
    "trillion", 
    "quadrillion", 
    "quintillion",
];

fn convert_less_than_hundred(n: u64) -> String {
    if n < 20 {
        LESS_THAN_TWENTY[n as usize].to_string()
    } else {
        let ten = (n / 10) as usize;
        let ones = (n % 10) as usize;
        if ones == 0 {
            TENS[ten].to_string()
        } else {
            format!("{}-{}", TENS[ten], LESS_THAN_TWENTY[ones])
        }
    }
}

fn convert_less_than_one_thousand(n: u64) -> String {
    if n == 0 {
        return String::new();
    }

    let hundreds = n / 100;
    let remainder = n % 100;

    let mut parts = Vec::new();

    if hundreds > 0 {
        parts.push(format!("{} hundred", LESS_THAN_TWENTY[hundreds as usize]));
    }

    if remainder > 0 {
        parts.push(convert_less_than_hundred(remainder));
    }

    parts.join(" ")
}

pub fn encode(n: u64) -> String {
    if n == 0 {
        return "zero".to_string();
    }

    let mut chunks = Vec::new();
    let mut num = n;

    // Split into chunks of three digits (least significant first)
    while num > 0 {
        chunks.push(num % 1000);
        num /= 1000;
    }

    chunks.reverse();

    let mut parts = Vec::new();

    for (i, &chunk) in chunks.iter().enumerate() {
        if chunk == 0 {
            continue;
        }

        let exponent = chunks.len().saturating_sub(1).saturating_sub(i);
        let scale = SCALES.get(exponent).copied().unwrap_or("");

        let chunk_str = convert_less_than_one_thousand(chunk);

        if scale.is_empty() {
            parts.push(chunk_str);
        } else {
            parts.push(format!("{} {}", chunk_str, scale));
        }
    }

    parts.join(" ")
}
