#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    NotEnoughPinsLeft,
    GameComplete,
}

pub struct BowlingGame {
    rolls: Vec<u16>,
    current_frame_pins: u16,
    game_complete: bool,
}

impl BowlingGame {
    fn current_frame(&self) -> usize {
        let mut roll_index = 0;
        let mut frame = 0;
        
        while roll_index < self.rolls.len() && frame < 10 {
            if frame < 9 {
                if self.rolls[roll_index] == 10 {
                    roll_index += 1; // Move to next roll
                } else {
                    roll_index += 2; // Move past both rolls of the frame
                }
                frame += 1;
            } else {
                return 10; // Return 10 if in the 10th frame
            }
        }
        frame
    }
    
    fn rolls_up_to_frame(&self, frame: usize) -> usize {
        let mut roll_index = 0;
        let mut current_frame = 0;
        
        while current_frame < frame && roll_index < self.rolls.len() {
            if self.rolls[roll_index] == 10 {
                roll_index += 1;
            } else {
                roll_index += 2;
            }
            current_frame += 1;
        }
        roll_index
    }
    pub fn new() -> Self {
        BowlingGame {
            rolls: Vec::new(),
            current_frame_pins: 0,
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
        
        let frame_index = self.current_frame();
        if frame_index < 10 {
            if self.current_frame_pins + pins > 10 {
                return Err(Error::NotEnoughPinsLeft);
            }
            
            self.rolls.push(pins);
            if pins == 10 || self.current_frame_pins + pins == 10 {
                self.current_frame_pins = 0; // Reset for next frame
            } else if self.current_frame_pins == 0 {
                self.current_frame_pins = pins; // Store first roll of frame
            } else {
                self.current_frame_pins = 0; // Reset for open frames
            }
        } else if frame_index == 10 {
            // Special handling for 10th frame
            self.rolls.push(pins);
            let rolls_in_last_frame = self.rolls.len() - self.rolls_up_to_frame(9);
            
            // Check if the game is complete
            if rolls_in_last_frame >= 3 {
                self.game_complete = true;
            } else if rolls_in_last_frame == 2 {
                let first = self.rolls[self.rolls_up_to_frame(9)];
                let second = self.rolls[self.rolls_up_to_frame(9) + 1];
                if first != 10 && (first + second) < 10 {
                    self.game_complete = true;
                }
            }
        }
        
        Ok(())
    }

    pub fn score(&self) -> Option<u16> {
        if !self.game_complete {
            return None;
        }
        
        let mut score = 0;
        let mut roll_index = 0;
        
        // Score first 9 frames
        for _ in 0..9 {
            if self.rolls[roll_index] == 10 {
                // Strike
                score += 10 + self.rolls[roll_index + 1] + self.rolls[roll_index + 2];
                roll_index += 1;
            } else {
                let frame_score = self.rolls[roll_index] + self.rolls[roll_index + 1];
                if frame_score == 10 {
                    // Spare
                    score += 10 + self.rolls[roll_index + 2];
                } else {
                    score += frame_score;
                }
                roll_index += 2;
            }
        }
        
        // Score 10th frame
        score += self.rolls[roll_index..].iter().sum::<u16>();
        
        Some(score)
    }
}
