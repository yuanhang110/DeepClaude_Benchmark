pub type Value = i32;
pub type Result = std::result::Result<(), Error>;

pub struct Forth {
    stack: Vec<Value>,
    definitions: std::collections::HashMap<String, Vec<String>>,
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
            definitions: std::collections::HashMap::new(),
        }
    }

    pub fn stack(&self) -> &[Value] {
        &self.stack
    }

    pub fn eval(&mut self, input: &str) -> Result {
        let mut tokens = input.split_whitespace().peekable();
        let mut defining = false;
        let mut new_definition = Vec::new();
        let mut word_name = String::new();

        while let Some(token) = tokens.next() {
            let token_lower = token.to_lowercase();
            
            // Handle word definitions
            if token_lower == ":" {
                defining = true;
                // Get word name (must be next token and not a number)
                word_name = tokens.next().ok_or(Error::InvalidWord)?.to_lowercase();
                if word_name.parse::<Value>().is_ok() {
                    return Err(Error::InvalidWord);
                }
                continue;
            }
            
            if defining {
                if token_lower == ";" {
                    self.definitions.insert(word_name.clone(), new_definition.clone());
                    defining = false;
                    new_definition.clear();
                    continue;
                }
                new_definition.push(token.to_string());
                continue;
            }

            // Expand custom words first
            if let Some(def) = self.definitions.get(&token_lower) {
                // Prepend definition tokens to the token stream
                let mut def_tokens = def.iter().map(|s| s.as_str()).peekable();
                while let Some(def_token) = def_tokens.next() {
                    let def_token_lower = def_token.to_lowercase();
                    tokens = def_tokens.chain(tokens).peekable();
                    break;
                }
                continue;
            }

            // Handle numbers
            if let Ok(num) = token.parse() {
                self.stack.push(num);
                continue;
            }

            // Handle built-in operations
            match token_lower.as_str() {
                "+" => self.arithmetic(|a, b| a.checked_add(b))?,
                "-" => self.arithmetic(|a, b| a.checked_sub(b))?,
                "*" => self.arithmetic(|a, b| a.checked_mul(b))?,
                "/" => self.division()?,
                "dup" => self.dup()?,
                "drop" => self.drop()?,
                "swap" => self.swap()?,
                "over" => self.over()?,
                _ => return Err(Error::UnknownWord),
            }
        }
        
        Ok(())
    }

    fn arithmetic<F>(&mut self, op: F) -> Result
    where
        F: FnOnce(Value, Value) -> Option<Value>,
    {
        let b = self.stack.pop().ok_or(Error::StackUnderflow)?;
        let a = self.stack.pop().ok_or(Error::StackUnderflow)?;
        match op(a, b) {
            Some(result) => {
                self.stack.push(result);
                Ok(())
            }
            None => Err(Error::DivisionByZero),
        }
    }

    fn division(&mut self) -> Result {
        let b = self.stack.pop().ok_or(Error::StackUnderflow)?;
        let a = self.stack.pop().ok_or(Error::StackUnderflow)?;
        if b == 0 {
            return Err(Error::DivisionByZero);
        }
        self.stack.push(a / b);
        Ok(())
    }

    fn dup(&mut self) -> Result {
        let top = self.stack.last().ok_or(Error::StackUnderflow)?.clone();
        self.stack.push(top);
        Ok(())
    }

    fn drop(&mut self) -> Result {
        self.stack.pop().ok_or(Error::StackUnderflow)?;
        Ok(())
    }

    fn swap(&mut self) -> Result {
        let a = self.stack.pop().ok_or(Error::StackUnderflow)?;
        let b = self.stack.pop().ok_or(Error::StackUnderflow)?;
        self.stack.push(a);
        self.stack.push(b);
        Ok(())
    }

    fn over(&mut self) -> Result {
        if self.stack.len() < 2 {
            return Err(Error::StackUnderflow);
        }
        let second = self.stack[self.stack.len() - 2];
        self.stack.push(second);
        Ok(())
    }
}
