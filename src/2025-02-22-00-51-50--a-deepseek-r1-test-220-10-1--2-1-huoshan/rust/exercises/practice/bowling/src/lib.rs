#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    NotEnoughPinsLeft,
    GameComplete,
}

pub struct BowlingGame {
    rolls: Vec<u16>,
    current_frame: u8,
    current_roll: u8,
    game_complete: bool,
}

impl BowlingGame {
    pub fn new() -> Self {
        BowlingGame {
            rolls: Vec::new(),
            current_frame: 1,
            current_roll: 1,
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

        // Handle frame validation (except for 10th frame)
        if self.current_frame < 10 {
            let frame_start = (self.current_frame - 1) as usize * 2;
            let frame_sum: u16 = self.rolls.iter().skip(frame_start).take(2).sum();
            
            if self.current_roll == 1 {
                if pins == 10 { // Strike
                    self.rolls.push(pins);
                    self.current_frame += 1;
                    return Ok(());
                }
                self.current_roll = 2;
                self.rolls.push(pins);
            } else {
                if frame_sum + pins > 10 {
                    return Err(Error::NotEnoughPinsLeft);
                }
                self.current_frame += 1;
                self.current_roll = 1;
            }
        } else {
            // Handle 10th frame special cases
            let tenth_frame_start = self.rolls.len();
            match (self.current_roll, pins) {
                (1, 10) => { // Strike
                    self.current_roll = 2;
                }
                (2, 10) if self.rolls[tenth_frame_start] == 10 => { // Two strikes
                    self.current_roll = 3;
                }
                (2, pins) if self.rolls[tenth_frame_start] + pins >= 10 => { // Spare
                    self.current_roll = 3;
                }
                (3, _) => {
                    self.game_complete = true;
                }
                _ => {}
            }
        }

        // Update game completion status
        if self.current_frame > 10 {
            self.game_complete = true;
        } else if self.current_frame == 10 {
            let frame_start = (self.current_frame - 1) as usize * 2;
            let frame_rolls = &self.rolls[frame_start..];
            
            match frame_rolls {
                // Strike followed by 2 bonus rolls
                [10, a, b] if frame_rolls.len() >= 3 => self.game_complete = true,
                // Spare followed by 1 bonus roll
                [a, b, c] if a + b == 10 => self.game_complete = true,
                // Open frame
                [a, b] if a + b < 10 => self.game_complete = true,
                _ => {}
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
        
        for frame in 1..=10 {
            if roll_index >= self.rolls.len() {
                return None;
            }

            let first_roll = self.rolls[roll_index];
            
            // Strike
            if first_roll == 10 {
                score += 10;
                // Add next two rolls (might be in next frames)
                if roll_index + 1 < self.rolls.len() {
                    score += self.rolls[roll_index + 1];
                }
                if roll_index + 2 < self.rolls.len() {
                    score += self.rolls[roll_index + 2];
                }
                roll_index += 1; // Strike only consumes 1 roll
            } else {
                // Spare or open frame
                let second_roll = *self.rolls.get(roll_index + 1).unwrap_or(&0);
                score += first_roll + second_roll;
                
                // Spare handling
                if first_roll + second_roll == 10 && frame != 10 {
                    if let Some(next_roll) = self.rolls.get(roll_index + 2) {
                        score += next_roll;
                    }
                }
                
                roll_index += 2;
            }
            
            // Handle 10th frame special case - no bonus points
            if frame == 10 {
                let frame_start = (frame - 1) as usize * 2;
                score += self.rolls[frame_start..].iter().take(3).sum::<u16>();
            }
        }

        Some(score)
    }
}
