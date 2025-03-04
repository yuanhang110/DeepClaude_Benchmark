use std::collections::HashMap;

pub type Value = i32;
pub type Result = std::result::Result<(), Error>;

pub struct Forth {
    stack: Vec<Value>,
    words: HashMap<String, Vec<String>>,
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
            words: HashMap::new(),
        }
    }

    pub fn stack(&self) -> &[Value] {
        &self.stack
    }

    pub fn eval(&mut self, input: &str) -> Result {
        let mut tokens: Vec<String> = Vec::new();
        self.process_input(input, &mut tokens)?;
        
        let mut i = 0;
        while i < tokens.len() {
            let token = &tokens[i];
            
            if let Ok(num) = token.parse::<Value>() {
                self.stack.push(num);
            } else if token == ":" {
                // Word definition
                if i + 2 >= tokens.len() {
                    return Err(Error::InvalidWord);
                }
                
                let name = tokens[i+1].to_string();
                if name.parse::<Value>().is_ok() {
                    return Err(Error::InvalidWord);
                }
                
                let mut definition = Vec::new();
                i += 2;
                while i < tokens.len() && tokens[i] != ";" {
                    definition.push(tokens[i].clone());
                    i += 1;
                }
                
                if i >= tokens.len() {
                    return Err(Error::InvalidWord);
                }
                
                // Preprocess the definition to expand nested words
                let mut processed_def = Vec::new();
                self.process_definition(&definition, &mut processed_def)?;
                self.words.insert(name.to_uppercase(), processed_def);
            } else {
                // Check if it's a custom word
                if let Some(def) = self.words.get(token) {
                    tokens.splice(i..=i, def.iter().cloned());
                    continue;
                }
                
                // Built-in operations
                match token.as_str() {
                    "+" => self.add()?,
                    "-" => self.sub()?,
                    "*" => self.mul()?,
                    "/" => self.div()?,
                    "DUP" => self.dup()?,
                    "DROP" => self.drop()?,
                    "SWAP" => self.swap()?,
                    "OVER" => self.over()?,
                    _ => return Err(Error::UnknownWord),
                }
            }
            i += 1;
        }
        
        Ok(())
    }

    fn process_input(&self, input: &str, tokens: &mut Vec<String>) -> Result {
        for token in input.split_whitespace() {
            tokens.push(token.to_uppercase());
        }
        Ok(())
    }

    fn process_definition(&self, definition: &[String], processed_def: &mut Vec<String>) -> Result {
        for token in definition {
            if let Some(def) = self.words.get(token) {
                self.process_definition(def, processed_def)?;
            } else {
                processed_def.push(token.clone());
            }
        }
        Ok(())
    }

    fn add(&mut self) -> Result {
        let (a, b) = self.pop_two()?;
        self.stack.push(a + b);
        Ok(())
    }

    fn sub(&mut self) -> Result {
        let (a, b) = self.pop_two()?;
        self.stack.push(a - b);
        Ok(())
    }

    fn mul(&mut self) -> Result {
        let (a, b) = self.pop_two()?;
        self.stack.push(a * b);
        Ok(())
    }

    fn div(&mut self) -> Result {
        let (a, b) = self.pop_two()?;
        if b == 0 {
            return Err(Error::DivisionByZero);
        }
        self.stack.push(a / b);
        Ok(())
    }

    fn dup(&mut self) -> Result {
        let a = self.pop_one()?;
        self.stack.push(a);
        self.stack.push(a);
        Ok(())
    }

    fn drop(&mut self) -> Result {
        self.pop_one()?;
        Ok(())
    }

    fn swap(&mut self) -> Result {
        let (a, b) = self.pop_two()?;
        self.stack.push(b);
        self.stack.push(a);
        Ok(())
    }

    fn over(&mut self) -> Result {
        let (a, b) = self.pop_two()?;
        self.stack.push(a);
        self.stack.push(b);
        self.stack.push(a);
        Ok(())
    }

    fn pop_one(&mut self) -> std::result::Result<Value, Error> {
        self.stack.pop().ok_or(Error::StackUnderflow)
    }

    fn pop_two(&mut self) -> std::result::Result<(Value, Value), Error> {
        let b = self.pop_one()?;
        let a = self.pop_one()?;
        Ok((a, b))
    }
}
