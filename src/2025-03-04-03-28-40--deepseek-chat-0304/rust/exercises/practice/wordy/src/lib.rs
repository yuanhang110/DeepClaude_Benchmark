pub fn answer(command: &str) -> Option<i32> {
    // Check if command starts with "What is" and ends with "?"
    if !command.starts_with("What is ") || !command.ends_with('?') {
        return None;
    }

    // Remove prefix and suffix
    let mut content = command.trim_start_matches("What is ").trim_end_matches('?').to_string();

    // Replace "multiplied by" with "*" and "divided by" with "/"
    content = content.replace("multiplied by", "*").replace("divided by", "/");

    // Split into tokens
    let tokens: Vec<&str> = content.split_whitespace().collect();

    // Must have at least one number
    if tokens.is_empty() {
        return None;
    }

    // Parse first number
    let mut result = match tokens[0].parse::<i32>() {
        Ok(n) => n,
        Err(_) => return None,
    };

    // Process operations
    let mut i = 1;
    while i < tokens.len() {
        let op = tokens[i];
        if i + 1 >= tokens.len() {
            return None; // Missing operand
        }

        let next_num = match tokens[i + 1].parse::<i32>() {
            Ok(n) => n,
            Err(_) => return None,
        };

        match op {
            "plus" => result += next_num,
            "minus" => result -= next_num,
            "*" => result *= next_num,
            "/" => result /= next_num,
            _ => return None, // Invalid operation
        }

        i += 2;
    }

    Some(result)
}
