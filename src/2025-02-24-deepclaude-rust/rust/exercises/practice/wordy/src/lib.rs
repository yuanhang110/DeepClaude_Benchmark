pub fn answer(command: &str) -> Option<i32> {
    let query = command.strip_prefix("What is ")?.strip_suffix("?")?;
    let tokens: Vec<&str> = query.split_whitespace().collect();
    if tokens.is_empty() {
        return None;
    }

    let mut acc = tokens[0].parse::<i32>().ok()?;
    let mut i = 1;

    while i < tokens.len() {
        let (op, consumed) = if tokens[i] == "plus" {
            ("add", 1)
        } else if tokens[i] == "minus" {
            ("subtract", 1)
        } else if i + 1 < tokens.len() && tokens[i] == "multiplied" && tokens[i+1] == "by" {
            ("multiply", 2)
        } else if i + 1 < tokens.len() && tokens[i] == "divided" && tokens[i+1] == "by" {
            ("divide", 2)
        } else {
            return None;
        };

        i += consumed;
        if i >= tokens.len() {
            return None;
        }

        let num = tokens[i].parse::<i32>().ok()?;
        i += 1;

        match op {
            "add" => acc += num,
            "subtract" => acc -= num,
            "multiply" => acc *= num,
            "divide" => {
                if num == 0 {
                    return None;
                }
                acc /= num;
            }
            _ => unreachable!(),
        }
    }

    Some(acc)
}
