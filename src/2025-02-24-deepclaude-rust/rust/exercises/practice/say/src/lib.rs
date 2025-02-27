pub fn encode(n: u64) -> String {
    if n == 0 {
        return "zero".to_string();
    }
    
    let scales = ["", "thousand", "million", "billion", "trillion", "quadrillion", "quintillion"];
    
    let mut chunks = Vec::new();
    let mut num = n;
    let mut pos = 0;
    
    while num > 0 {
        let chunk = num % 1000;
        chunks.push((chunk, pos));
        num /= 1000;
        pos += 1;
    }
    
    let mut parts = Vec::new();
    
    for &(value, pos) in chunks.iter().rev() {
        if value == 0 {
            continue;
        }
        
        let part = convert_less_than_1000(value);
        let scale = if pos < scales.len() { scales[pos] } else { "" };
        
        if scale.is_empty() {
            parts.push(part);
        } else {
            parts.push(format!("{} {}", part, scale));
        }
    }
    
    parts.join(" ")
}

fn convert_less_than_1000(n: u64) -> String {
    if n == 0 {
        return String::new();
    }
    
    if n >= 100 {
        let hundred = ones((n / 100) as usize);
        let remainder = convert_less_than_100(n % 100);
        
        if remainder.is_empty() {
            format!("{} hundred", hundred)
        } else {
            format!("{} hundred {}", hundred, remainder)
        }
    } else {
        convert_less_than_100(n)
    }
}

fn convert_less_than_100(n: u64) -> String {
    match n {
        0 => String::new(),
        1..=19 => ones(n as usize).to_string(),
        20..=99 => {
            let ten_part = tens((n / 10) as usize);
            let one_part = if n % 10 != 0 {
                format!("-{}", ones((n % 10) as usize))
            } else {
                String::new()
            };
            format!("{}{}", ten_part, one_part)
        },
        _ => String::new(),
    }
}

fn ones(index: usize) -> &'static str {
    const ONES: [&str; 20] = [
        "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten",
        "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
    ];
    ONES[index]
}

fn tens(index: usize) -> &'static str {
    const TENS: [&str; 10] = [
        "", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
    ];
    TENS[index]
}
