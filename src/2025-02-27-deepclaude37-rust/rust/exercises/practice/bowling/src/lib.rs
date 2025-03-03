#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    NotEnoughPinsLeft,
    GameComplete,
}

pub struct BowlingGame {
    rolls: Vec<u16>,
    current_frame: u16,
    rolls_in_frame: u16,
    game_complete: bool,
}

impl BowlingGame {
    pub fn new() -> Self {
        Self {
            rolls: Vec::new(),
            current_frame: 1,
            rolls_in_frame: 0,
            game_complete: false,
        }
    }

    pub fn roll(&mut self, pins: u16) -> Result<(), Error> {
        if self.game_complete {
            return Err(Error::GameComplete);
        }

        if pins > 10 {
            return Err(Error::NotEnoughPinsLeft);
        }

        if self.current_frame <= 9 {
            // Handle frames 1-9
            match self.rolls_in_frame {
                0 => {
                    // First roll in frame
                    if pins == 10 {
                        // Strike
                        self.rolls.push(pins);
                        self.current_frame += 1;
                        self.rolls_in_frame = 0;
                    } else {
                        self.rolls.push(pins);
                        self.rolls_in_frame = 1;
                    }
                }
                1 => {
                    // Second roll in frame
                    let prev = self.rolls.last().unwrap();
                    if prev + pins > 10 {
                        return Err(Error::NotEnoughPinsLeft);
                    }
                    self.rolls.push(pins);
                    self.current_frame += 1;
                    self.rolls_in_frame = 0;
                }
                _ => unreachable!(),
            }
        } else {
            // Handle 10th frame
            let rolls_in_10th = self.rolls.len() - self.rolls_before_10th();
            
            match rolls_in_10th {
                0 => {
                    // First roll
                    self.rolls.push(pins);
                    if pins < 10 {
                        self.rolls_in_frame = 1;
                    }
                }
                1 => {
                    // Second roll
                    let first = self.rolls[self.rolls_before_10th()];
                    if first == 10 {
                        // After a strike, any second roll is valid
                        if pins > 10 {
                            return Err(Error::NotEnoughPinsLeft);
                        }
                        self.rolls.push(pins);
                    } else {
                        // After non-strike, validate pins
                        if first + pins > 10 {
                            return Err(Error::NotEnoughPinsLeft);
                        }
                        self.rolls.push(pins);
                        if first + pins < 10 {
                            // Open frame, game complete
                            self.game_complete = true;
                        }
                    }
                }
                2 => {
                    // Third roll only allowed after strike or spare
                    let first = self.rolls[self.rolls_before_10th()];
                    let second = self.rolls[self.rolls_before_10th() + 1];
                    
                    if first == 10 || first + second == 10 {
                        if first == 10 {
                            // After a strike in 10th frame
                            if second == 10 {
                                // Second ball was also a strike
                                if pins > 10 {
                                    return Err(Error::NotEnoughPinsLeft);
                                }
                            } else {
                                // Second ball was not a strike
                                // Third ball must not exceed remaining pins
                                if pins > 10 - second {
                                    return Err(Error::NotEnoughPinsLeft);
                                }
                            }
                        } else {
                            // After a spare in 10th frame
                            if pins > 10 {
                                return Err(Error::NotEnoughPinsLeft);
                            }
                        }
                        self.rolls.push(pins);
                        self.game_complete = true;
                    } else {
                        return Err(Error::GameComplete);
                    }
                }
                _ => {
                    return Err(Error::GameComplete);
                }
            }
        }

        Ok(())
    }

    // Helper function to determine the index where 10th frame starts
    fn rolls_before_10th(&self) -> usize {
        let mut rolls = 0;
        let mut frame = 1;
        let mut i = 0;
        
        while frame < 10 && i < self.rolls.len() {
            if self.rolls[i] == 10 {
                // Strike takes one roll
                rolls += 1;
                i += 1;
                frame += 1;
            } else if i + 1 < self.rolls.len() {
                // Non-strike takes two rolls
                rolls += 2;
                i += 2;
                frame += 1;
            } else {
                break;
            }
        }
        
        rolls
    }

    pub fn score(&self) -> Option<u16> {
        if !self.game_complete {
            return None;
        }

        let mut total = 0;
        let mut i = 0;

        // Process first 9 frames
        for _frame in 0..9 {
            if i >= self.rolls.len() {
                return None;
            }

            if self.rolls[i] == 10 {
                // Strike
                total += 10;
                
                // Add bonus: next two rolls
                if i + 1 >= self.rolls.len() || i + 2 >= self.rolls.len() {
                    return None;
                }
                total += self.rolls[i + 1] + self.rolls[i + 2];
                
                i += 1;
            } else {
                // Non-strike
                if i + 1 >= self.rolls.len() {
                    return None;
                }
                
                let frame_score = self.rolls[i] + self.rolls[i + 1];
                total += frame_score;
                
                if frame_score == 10 {
                    // Spare - add bonus
                    if i + 2 >= self.rolls.len() {
                        return None;
                    }
                    total += self.rolls[i + 2];
                }
                
                i += 2;
            }
        }

        // Process 10th frame
        let tenth_frame_start = self.rolls_before_10th();
        if tenth_frame_start >= self.rolls.len() {
            return None;
        }
        
        // For 10th frame, just sum all the rolls
        for j in tenth_frame_start..self.rolls.len() {
            total += self.rolls[j];
        }

        Some(total)
    }
}
