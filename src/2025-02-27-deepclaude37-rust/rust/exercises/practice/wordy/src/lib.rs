pub fn answer(command: &str) -> Option<i32> {
    // Check command starts with "What is " and ends with "?"
    let body = command.strip_prefix("What is ")?;
    let body = body.strip_suffix('?')?;
    let body = body.trim();
    if body.is_empty() {
        return None;
    }

    let words: Vec<&str> = body.split_whitespace().collect();
    let mut tokens = Vec::new();
    let mut i = 0;
    while i < words.len() {
        match words[i] {
            "multiplied" | "divided" => {
                if i + 1 >= words.len() || words[i + 1] != "by" {
                    return None;
                }
                let op = if words[i] == "multiplied" {
                    "multiplied by"
                } else {
                    "divided by"
                };
                tokens.push(op);
                i += 2;
            }
            "plus" | "minus" => {
                tokens.push(words[i]);
                i += 1;
            }
            _ => {
                // Check if it's a number
                if words[i].parse::<i32>().is_ok() {
                    tokens.push(words[i]);
                    i += 1;
                } else {
                    return None;
                }
            }
        }
    }

    if tokens.is_empty() {
        return None;
    }

    // Parse first number
    let first_num = tokens[0].parse::<i32>().ok()?;

    // Parse remaining operations and numbers
    let rest = &tokens[1..];
    if rest.len() % 2 != 0 {
        return None;
    }

    let mut operations = Vec::new();
    let mut numbers = Vec::new();

    for chunk in rest.chunks_exact(2) {
        let op = chunk[0];
        let num = chunk[1].parse::<i32>().ok()?;
        operations.push(op);
        numbers.push(num);
    }

    // Apply operations left-to-right
    let mut result = first_num;
    for (op, num) in operations.iter().zip(numbers.iter()) {
        match *op {
            "plus" => result += num,
            "minus" => result -= num,
            "multiplied by" => result *= num,
            "divided by" => result /= num,
            _ => return None,
        }
    }

    Some(result)
}
