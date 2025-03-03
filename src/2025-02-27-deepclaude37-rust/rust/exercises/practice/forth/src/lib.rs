pub type Value = i32;
pub type Result = std::result::Result<(), Error>;

use std::collections::HashMap;

pub struct Forth {
    stack: Vec<Value>,
    dict: HashMap<String, Vec<String>>,
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
            dict: HashMap::new(),
        }
    }

    pub fn stack(&self) -> &[Value] {
        &self.stack
    }

    fn is_number(word: &str) -> bool {
        word.parse::<Value>().is_ok()
    }

    fn eval_token(&mut self, token: &str) -> Result {
        if let Ok(num) = token.parse::<Value>() {
            self.stack.push(num);
            return Ok(());
        }

        match token.to_uppercase().as_str() {
            "+" => {
                if self.stack.len() < 2 {
                    return Err(Error::StackUnderflow);
                }
                let b = self.stack.pop().unwrap();
                let a = self.stack.pop().unwrap();
                self.stack.push(a.checked_add(b).ok_or(Error::InvalidWord)?);
            }
            "-" => {
                if self.stack.len() < 2 {
                    return Err(Error::StackUnderflow);
                }
                let b = self.stack.pop().unwrap();
                let a = self.stack.pop().unwrap();
                self.stack.push(a.checked_sub(b).ok_or(Error::InvalidWord)?);
            }
            "*" => {
                if self.stack.len() < 2 {
                    return Err(Error::StackUnderflow);
                }
                let b = self.stack.pop().unwrap();
                let a = self.stack.pop().unwrap();
                self.stack.push(a.checked_mul(b).ok_or(Error::InvalidWord)?);
            }
            "/" => {
                if self.stack.len() < 2 {
                    return Err(Error::StackUnderflow);
                }
                let b = self.stack.pop().unwrap();
                if b == 0 {
                    return Err(Error::DivisionByZero);
                }
                let a = self.stack.pop().unwrap();
                self.stack.push(a.checked_div(b).ok_or(Error::InvalidWord)?);
            }
            "DUP" => {
                if self.stack.is_empty() {
                    return Err(Error::StackUnderflow);
                }
                let a = *self.stack.last().unwrap();
                self.stack.push(a);
            }
            "DROP" => {
                if self.stack.is_empty() {
                    return Err(Error::StackUnderflow);
                }
                self.stack.pop();
            }
            "SWAP" => {
                if self.stack.len() < 2 {
                    return Err(Error::StackUnderflow);
                }
                let len = self.stack.len();
                self.stack.swap(len - 2, len - 1);
            }
            "OVER" => {
                if self.stack.len() < 2 {
                    return Err(Error::StackUnderflow);
                }
                let a = self.stack[self.stack.len() - 2];
                self.stack.push(a);
            }
            word => {
                if let Some(definition) = self.dict.get(&word.to_uppercase()).cloned() {
                    for token in &definition {
                        self.eval_token(token)?;
                    }
                } else {
                    return Err(Error::UnknownWord);
                }
            }
        }
        Ok(())
    }

    pub fn eval(&mut self, input: &str) -> Result {
        let tokens: Vec<&str> = input.split_whitespace().collect();
        
        if let Some(&":") = tokens.first() {
            if tokens.len() < 4 || tokens.last() != Some(&";") {
                return Err(Error::InvalidWord);
            }
            
            let name = tokens[1].to_uppercase();
            if Self::is_number(&name) {
                return Err(Error::InvalidWord);
            }
            
            let definition: Vec<String> = tokens[2..tokens.len()-1]
                .iter()
                .map(|&s| s.to_string())
                .collect();
                
            self.dict.insert(name, definition);
            Ok(())
        } else {
            for token in tokens {
                self.eval_token(token)?;
            }
            Ok(())
        }
    }
}
