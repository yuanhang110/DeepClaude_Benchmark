pub type Value = i32;
pub type Result = std::result::Result<(), Error>;

use std::rc::Rc;

pub struct Forth {
    stack: Vec<Value>,
    definitions: std::collections::HashMap<String, Vec<Rc<str>>>,
}

#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    DivisionByZero,
    StackUnderflow,
    UnknownWord,
    InvalidWord,
}

impl Forth {
    pub fn new() -> Self {
        Self {
            stack: Vec::new(),
            definitions: std::collections::HashMap::new(),
        }
    }

    pub fn stack(&self) -> &[Value] {
        &self.stack
    }

    pub fn eval(&mut self, input: &str) -> Result {
        let input = input.trim();
        if input.is_empty() {
            return Ok(());
        }

        let tokens = input.split_whitespace().collect::<Vec<_>>();
        let mut i = 0;

        // Handle word definitions starting with ":"
        if tokens[i].eq_ignore_ascii_case(":") {
            if tokens.len() < 4 {  // Need at least ": word definition ;"
                return Err(Error::InvalidWord);
            }

            i += 1;
            let name = tokens[i].to_ascii_lowercase();
            // Word names can't be numbers
            if name.parse::<i32>().is_ok() {
                return Err(Error::InvalidWord);
            }

            i += 1;
            let mut definition = Vec::new();
            while i < tokens.len() && !tokens[i].eq_ignore_ascii_case(";") {
                let token = tokens[i].to_ascii_lowercase();
                if let Some(def) = self.definitions.get(&token) {
                    definition.extend(def.iter().cloned());
                } else {
                    definition.push(Rc::from(token));
                }
                i += 1;
            }

            if i >= tokens.len() {
                return Err(Error::InvalidWord);
            }

            self.definitions.insert(name, definition);
            return Ok(());
        }

        // Process tokens iteratively
        let mut processing = std::collections::VecDeque::new();
        processing.extend(tokens.iter().map(|s| Rc::from(s.to_ascii_lowercase())));

        while let Some(token) = processing.pop_front() {
            match token.as_str() {
                "+" => {
                    let (b, a) = self.pop_two()?;
                    self.stack.push(a + b);
                }
                "-" => {
                    let (b, a) = self.pop_two()?;
                    self.stack.push(a - b);
                }
                "*" => {
                    let (b, a) = self.pop_two()?;
                    self.stack.push(a * b);
                }
                "/" => {
                    let (b, a) = self.pop_two()?;
                    if b == 0 {
                        return Err(Error::DivisionByZero);
                    }
                    self.stack.push(a / b);
                }
                "dup" => {
                    let a = self.pop_one()?;
                    self.stack.push(a);
                    self.stack.push(a);
                }
                "drop" => {
                    self.pop_one()?;
                }
                "swap" => {
                    let (b, a) = self.pop_two()?;
                    self.stack.push(b);
                    self.stack.push(a);
                }
                "over" => {
                    let (b, a) = self.pop_two()?;
                    self.stack.push(a);
                    self.stack.push(b);
                    self.stack.push(a);
                }
                _ => {
                    if let Ok(num) = token.as_ref().parse::<i32>() {
                        self.stack.push(num);
                    } else if let Some(def) = self.definitions.get(token.as_ref()) {
                        // Add definition tokens in reverse order to maintain correct processing order
                        // Since we're using Rc<str>, cloning is cheap (just incrementing ref count)
                        for t in def.iter().rev() {
                            processing.push_front(Rc::clone(t));
                        }
                    } else {
                        return Err(Error::UnknownWord);
                    }
                }
            }
        }

        Ok(())
    }

    fn pop_one(&mut self) -> std::result::Result<Value, Error> {
        self.stack.pop().ok_or(Error::StackUnderflow)
    }

    fn pop_two(&mut self) -> std::result::Result<(Value, Value), Error> {
        let b = self.pop_one()?;
        let a = self.pop_one()?;
        Ok((b, a))
    }
}
