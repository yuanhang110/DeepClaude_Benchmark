pub fn encode(n: u64) -> String {
    if n == 0 {
        return "zero".to_string();
    }
    
    let chunks = split_into_chunks(n);
    let reversed_chunks: Vec<_> = chunks.into_iter().rev().collect();
    let mut parts = Vec::new();
    
    for (i, &chunk) in reversed_chunks.iter().enumerate() {
        if chunk == 0 {
            continue;
        }
        
        let scale_index = reversed_chunks.len() - 1 - i;
        let chunk_str = convert_chunk(chunk);
        let scale = SCALES[scale_index];
        
        if scale.is_empty() {
            parts.push(chunk_str);
        } else {
            parts.push(format!("{} {}", chunk_str, scale));
        }
    }
    
    parts.join(" ")
}

fn split_into_chunks(n: u64) -> Vec<u64> {
    let mut chunks = Vec::new();
    let mut num = n;
    
    while num > 0 {
        chunks.push(num % 1000);
        num /= 1000;
    }
    
    chunks
}

fn convert_chunk(chunk: u64) -> String {
    let mut parts = Vec::new();
    let hundreds = chunk / 100;
    let remainder = chunk % 100;
    
    if hundreds > 0 {
        parts.push(format!("{} hundred", convert_ones(hundreds)));
    }
    
    if remainder > 0 {
        parts.push(convert_less_than_one_hundred(remainder));
    }
    
    parts.join(" ")
}

fn convert_less_than_one_hundred(n: u64) -> String {
    match n {
        0..=9 => convert_ones(n).to_string(),
        10..=19 => convert_teens(n).to_string(),
        20..=99 => {
            let tens = (n / 10) * 10;
            let ones = n % 10;
            if ones == 0 {
                convert_tens(tens).to_string()
            } else {
                format!("{}-{}", convert_tens(tens), convert_ones(ones))
            }
        }
        _ => panic!("Unexpected value: {}", n),
    }
}

fn convert_ones(n: u64) -> &'static str {
    ONES[n as usize]
}

fn convert_teens(n: u64) -> &'static str {
    TEENS[(n - 10) as usize]
}

fn convert_tens(n: u64) -> &'static str {
    TENS[(n / 10 - 2) as usize]
}

const ONES: &[&str] = &[
    "zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
];

const TEENS: &[&str] = &[
    "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", 
    "sixteen", "seventeen", "eighteen", "nineteen",
];

const TENS: &[&str] = &[
    "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
];

const SCALES: &[&str] = &[
    "", "thousand", "million", "billion", "trillion", 
    "quadrillion", "quintillion", "sextillion",
];
