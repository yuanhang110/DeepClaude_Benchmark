#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    NotEnoughPinsLeft,
    GameComplete,
}

pub struct BowlingGame {
    rolls: Vec<u16>,
    current_frame: usize,
    in_frame_roll: usize,
    game_complete: bool,
}

impl BowlingGame {
    pub fn new() -> Self {
        Self {
            rolls: Vec::new(),
            current_frame: 1,
            in_frame_roll: 1,
            game_complete: false,
        }
    }

    pub fn roll(&mut self, pins: u16) -> Result<(), Error> {
        if self.game_complete {
            return Err(Error::GameComplete);
        }

        // Validate pin count
        let max_pins = if self.current_frame == 10 {
            // 10th frame has special rules
            if self.in_frame_roll == 1 {
                10
            } else if self.in_frame_roll == 2 && self.rolls.last() == Some(&10) {
                10
            } else {
                10 - self.rolls.last().unwrap_or(&0)
            }
        } else {
            // Normal frame
            if self.in_frame_roll == 1 {
                10
            } else {
                10 - self.rolls.last().unwrap_or(&0)
            }
        };

        if pins > max_pins {
            return Err(Error::NotEnoughPinsLeft);
        }

        self.rolls.push(pins);

        // Update frame state
        if self.current_frame == 10 {
            // 10th frame rules
            if self.in_frame_roll == 1 {
                if pins == 10 {
                    // Strike - get two more rolls
                    self.in_frame_roll += 1;
                } else {
                    self.in_frame_roll += 1;
                }
            } else if self.in_frame_roll == 2 {
                if self.rolls[self.rolls.len() - 2] + pins >= 10 {
                    // Spare or double strike - get one more roll
                    self.in_frame_roll += 1;
                } else {
                    self.game_complete = true;
                }
            } else if self.in_frame_roll == 3 {
                self.game_complete = true;
            }
        } else {
            // Normal frame rules
            if pins == 10 || self.in_frame_roll == 2 {
                // Strike or second roll - move to next frame
                self.current_frame += 1;
                self.in_frame_roll = 1;
            } else {
                self.in_frame_roll += 1;
            }
        }

        Ok(())
    }

    pub fn score(&self) -> Option<u16> {
        if !self.game_complete || self.rolls.len() < 12 {
            return None;
        }

        let mut score = 0;
        let mut roll_index = 0;

        for _ in 0..10 {
            if self.rolls[roll_index] == 10 {
                // Strike
                score += 10 + self.rolls[roll_index + 1] + self.rolls[roll_index + 2];
                roll_index += 1;
            } else if self.rolls[roll_index] + self.rolls[roll_index + 1] == 10 {
                // Spare
                score += 10 + self.rolls[roll_index + 2];
                roll_index += 2;
            } else {
                // Open frame
                score += self.rolls[roll_index] + self.rolls[roll_index + 1];
                roll_index += 2;
            }
        }

        Some(score)
    }
}
