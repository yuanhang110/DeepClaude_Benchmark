const UNITS: [&str; 20] = [
    "zero", "one", "two", "three", "four", 
    "five", "six", "seven", "eight", "nine",
    "ten", "eleven", "twelve", "thirteen", "fourteen",
    "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
];

const TENS: [&str; 10] = [
    "", "", "twenty", "thirty", "forty", 
    "fifty", "sixty", "seventy", "eighty", "ninety",
];

const SCALES: [&str; 7] = [
    "",
    "thousand",
    "million",
    "billion",
    "trillion",
    "quadrillion",
    "quintillion",
];

fn convert_chunk(n: u64) -> String {
    match n {
        0..=19 => UNITS[n as usize].to_string(),
        20..=99 => {
            let ten = TENS[(n / 10) as usize];
            let unit = if n % 10 != 0 {
                format!("-{}", UNITS[(n % 10) as usize])
            } else {
                String::new()
            };
            format!("{}{}", ten, unit)
        }
        100..=999 => {
            let hundred = UNITS[(n / 100) as usize];
            let remainder = n % 100;
            if remainder != 0 {
                format!("{} hundred {}", hundred, convert_chunk(remainder))
            } else {
                format!("{} hundred", hundred)
            }
        }
        _ => String::new(),
    }
}

pub fn encode(n: u64) -> String {
    if n == 0 {
        return UNITS[0].to_string();
    }
    
    let mut chunks = Vec::new();
    let mut remaining = n;
    let mut scale_index = 0;
    
    while remaining > 0 {
        let chunk = remaining % 1000;
        if chunk != 0 {
            let chunk_words = convert_chunk(chunk);
            if scale_index > 0 {
                chunks.push(format!("{} {}", chunk_words, SCALES[scale_index]));
            } else {
                chunks.push(chunk_words);
            }
        }
        remaining /= 1000;
        scale_index += 1;
    }
    
    chunks.reverse();
    chunks.join(" ")
}
