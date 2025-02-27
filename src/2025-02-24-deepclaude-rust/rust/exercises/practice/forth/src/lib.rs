pub type Value = i32;
pub type Result = std::result::Result<(), Error>;

pub struct Forth {
    stack: Vec<Value>,
    words: std::collections::HashMap<String, Vec<String>>,
}

#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    DivisionByZero,
    StackUnderflow,
    UnknownWord,
    InvalidWord,
}

impl Forth {
    pub fn new() -> Forth {
        Forth {
            stack: Vec::new(),
            words: std::collections::HashMap::new(),
        }
    }

    pub fn stack(&self) -> &[Value] {
        &self.stack
    }

    pub fn eval(&mut self, input: &str) -> Result {
        use std::collections::VecDeque;

        let tokens = input.split_whitespace();
        let mut deque: VecDeque<String> = tokens.map(|s| s.to_string()).collect();

        while let Some(token) = deque.pop_front() {
            let token_lower = token.to_lowercase();
            if token_lower == ":" {
                // Handle word definition
                let name = match deque.pop_front() {
                    Some(n) => n.to_lowercase(),
                    None => return Err(Error::InvalidWord),
                };
                // Check if name is a number
                if name.parse::<i32>().is_ok() {
                    return Err(Error::InvalidWord);
                }
                let mut definition = Vec::new();
                let mut found_semicolon = false;
                while let Some(t) = deque.pop_front() {
                    let t_lower = t.to_lowercase();
                    if t_lower == ";" {
                        found_semicolon = true;
                        break;
                    }
                    // Preserve numbers as-is, lowercase others
                    if t.parse::<i32>().is_ok() {
                        definition.push(t);
                    } else {
                        definition.push(t_lower);
                    }
                }
                if !found_semicolon {
                    return Err(Error::InvalidWord);
                }
                self.words.insert(name, definition);
            } else if let Ok(num) = token_lower.parse::<i32>() {
                self.stack.push(num);
            } else {
                if let Some(def) = self.words.get(&token_lower) {
                    // Prepend definition tokens to deque
                    for t in def.iter().rev() {
                        deque.push_front(t.clone());
                    }
                } else {
                    match token_lower.as_str() {
                        "+" | "-" | "*" | "/" => self.arithmetic(&token_lower)?,
                        "dup" => self.dup()?,
                        "drop" => self.drop()?,
                        "swap" => self.swap()?,
                        "over" => self.over()?,
                        _ => return Err(Error::UnknownWord),
                    }
                }
            }
        }
        Ok(())
    }

    fn arithmetic(&mut self, op: &str) -> Result {
        if self.stack.len() < 2 {
            return Err(Error::StackUnderflow);
        }
        let b = self.stack.pop().unwrap();
        let a = self.stack.pop().unwrap();
        match op {
            "+" => self.stack.push(a + b),
            "-" => self.stack.push(a - b),
            "*" => self.stack.push(a * b),
            "/" => {
                if b == 0 {
                    return Err(Error::DivisionByZero);
                }
                self.stack.push(a / b)
            }
            _ => unreachable!(),
        }
        Ok(())
    }

    fn dup(&mut self) -> Result {
        if self.stack.is_empty() {
            return Err(Error::StackUnderflow);
        }
        let top = *self.stack.last().unwrap();
        self.stack.push(top);
        Ok(())
    }

    fn drop(&mut self) -> Result {
        if self.stack.is_empty() {
            return Err(Error::StackUnderflow);
        }
        self.stack.pop();
        Ok(())
    }

    fn swap(&mut self) -> Result {
        if self.stack.len() < 2 {
            return Err(Error::StackUnderflow);
        }
        let a = self.stack.pop().unwrap();
        let b = self.stack.pop().unwrap();
        self.stack.push(a);
        self.stack.push(b);
        Ok(())
    }

    fn over(&mut self) -> Result {
        if self.stack.len() < 2 {
            return Err(Error::StackUnderflow);
        }
        let len = self.stack.len();
        let val = self.stack[len - 2];
        self.stack.push(val);
        Ok(())
    }
}
