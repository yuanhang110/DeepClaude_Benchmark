#[derive(Debug, PartialEq, Eq)]
pub enum Error {
    NotEnoughPinsLeft,
    GameComplete,
}

pub struct BowlingGame {
    rolls: Vec<u16>,
    current_frame: usize,
    current_roll_in_frame: usize,
    game_complete: bool,
}

impl BowlingGame {
    pub fn new() -> Self {
        Self {
            rolls: Vec::new(),
            current_frame: 0,
            current_roll_in_frame: 0,
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

        if self.current_frame < 9 {
            // Handle frames 0-8
            if self.current_roll_in_frame == 0 {
                // First roll in frame
                if pins == 10 {
                    // Strike
                    self.rolls.push(pins);
                    self.current_frame += 1;
                    // Reset roll for next frame
                    self.current_roll_in_frame = 0;
                } else {
                    self.rolls.push(pins);
                    self.current_roll_in_frame = 1;
                }
            } else {
                // Second roll in frame
                let previous = *self.rolls.last().unwrap();
                if previous + pins > 10 {
                    return Err(Error::NotEnoughPinsLeft);
                }
                self.rolls.push(pins);
                self.current_frame += 1;
                self.current_roll_in_frame = 0;
            }
        } else {
            // Handle 10th frame
            match self.current_roll_in_frame {
                0 => {
                    self.rolls.push(pins);
                    if pins == 10 {
                        // Strike - get two fill balls
                        self.current_roll_in_frame = 1;
                    } else {
                        self.current_roll_in_frame = 1;
                    }
                }
                1 => {
                    let first = *self.rolls.last().unwrap();
                    if first == 10 {
                        // Previous was strike
                        if pins > 10 {
                            return Err(Error::NotEnoughPinsLeft);
                        }
                        self.rolls.push(pins);
                        self.current_roll_in_frame = 2;
                    } else {
                        // Not following a strike
                        if first + pins > 10 {
                            return Err(Error::NotEnoughPinsLeft);
                        }
                        self.rolls.push(pins);
                        self.rolls.push(pins);
                        if first + pins == 10 {
                            // Spare - get one fill ball
                            self.current_roll_in_frame = 2;
                        } else {
                            // Open frame - game complete
                            self.game_complete = true;
                        }
                    }
                }
                2 => {
                    if pins > 10 {
                        return Err(Error::NotEnoughPinsLeft);
                    }
                    self.rolls.push(pins);
                    self.game_complete = true;
                }
                _ => unreachable!(),
            }
        }


        Ok(())
    }

    pub fn score(&self) -> Option<u16> {
        if !self.game_complete {
            return None;
        }

        let mut total = 0;
        let mut index = 0;

        for _ in 0..9 {
            if self.rolls.get(index).copied() == Some(10) {
                // Strike
                total += 10;
                total += self.rolls.get(index + 1).unwrap_or(&0);
                total += self.rolls.get(index + 2).unwrap_or(&0);
                index += 1;
            } else {
                let first = self.rolls[index];
                let second = self.rolls.get(index + 1).copied().unwrap_or(0);
                total += first + second;

                if first + second == 10 {
                    // Spare
                    total += self.rolls.get(index + 2).copied().unwrap_or(0);
                }

                index += 2;
            }
        }

        // Add 10th frame score (sum of all remaining rolls)
        total += self.rolls.iter().skip(index).sum::<u16>();

        Some(total)
    }
}
