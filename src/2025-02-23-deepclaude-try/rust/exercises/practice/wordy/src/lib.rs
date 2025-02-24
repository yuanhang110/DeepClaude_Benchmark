pub fn answer(command: &str) -> Option<i32> {
    // Check command format
    if !command.starts_with("What is ") || !command.ends_with('?') {
        return None;
    }
    
    // Extract expression and normalize operators
    let expr = command["What is ".len()..command.len()-1].trim();
    let expr = expr
        .replace("multiplied by", "*")
        .replace("divided by", "/")
        .replace("plus", "+")
        .replace("minus", "-");
    
    // Split into tokens
    let tokens: Vec<&str> = expr.split_whitespace().collect();
    if tokens.is_empty() {
        return None;
    }
    
    // Parse initial number
    let initial = tokens[0].parse::<i32>().ok()?;
    
    // Check remaining tokens form valid operation pairs
    if (tokens.len() - 1) % 2 != 0 {
        return None;
    }
    
    // Parse operations
    let mut operations = Vec::new();
    for i in 0..(tokens.len() - 1) / 2 {
        let op_index = 1 + 2 * i;
        let num_index = op_index + 1;
        
        if op_index >= tokens.len() || num_index >= tokens.len() {
            return None;
        }
        
        let op = tokens[op_index];
        let num = tokens[num_index].parse::<i32>().ok()?;
        operations.push((op, num));
    }
    
    // Evaluate operations left-to-right
    let mut result = initial;
    for (op, num) in operations {
        match op {
            "+" => result += num,
            "-" => result -= num,
            "*" => result *= num,
            "/" => {
                if num == 0 {
                    return None;
                }
                result /= num;
            }
            _ => return None, // Invalid operator
        }
    }
    
    Some(result)
}
