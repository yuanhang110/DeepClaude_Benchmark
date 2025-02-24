pub fn answer(command: &str) -> Option<i32> {
    // Basic format validation
    if !command.starts_with("What is ") || !command.ends_with('?') {
        return None;
    }
    
    let expr = command["What is ".len()..command.len()-1].trim();
    if expr.is_empty() {
        return None;
    }
    
    // Split into tokens (numbers and operations)
    let mut tokens = expr.split_whitespace();
    let mut result = tokens.next().and_then(|n| n.parse::<i32>().ok())?;
    
    while let Some(op) = tokens.next() {
        let num = tokens.next().and_then(|n| n.parse::<i32>().ok())?;
        
        match op.to_lowercase().as_str() {
            "plus" => result += num,
            "minus" => result -= num,
            "multiplied" => {
                if tokens.next() != Some("by") { return None; }
                result *= num
            },
            "divided" => {
                if tokens.next() != Some("by") { return None; }
                if num == 0 { return None; }
                result /= num
            },
            _ => return None, // Invalid operation
        }
    }
    
    // Should consume all input tokens
    if tokens.next().is_none() {
        Some(result)
    } else {
        None
    }
}
