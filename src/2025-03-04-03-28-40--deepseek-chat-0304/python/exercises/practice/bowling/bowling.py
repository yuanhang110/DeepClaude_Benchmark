class BowlingGame:
    def __init__(self):
        self.rolls = []

    def roll(self, pins):
        if pins < 0 or pins > 10:
            raise ValueError("Invalid number of pins")
            
        # Check for invalid frame totals (except 10th frame)
        if len(self.rolls) % 2 == 1 and self.rolls[-1] + pins > 10 and len(self.rolls) < 18:
            raise ValueError("Frame total cannot exceed 10")
            
        self.rolls.append(pins)
        
        # Special 10th frame validation
        if len(self.rolls) >= 19:
            # After first roll in 10th frame
            if len(self.rolls) == 19 and self.rolls[18] != 10 and self.rolls[18] + pins > 10:
                raise ValueError("Frame total cannot exceed 10")
            # After second roll in 10th frame if no strike/spare
            if len(self.rolls) == 20 and sum(self.rolls[18:20]) < 10:
                raise IndexError("Cannot roll after game is complete")

    def score(self):
        if len(self.rolls) < 20:
            raise IndexError("Game is not complete")
        if len(self.rolls) == 20 and sum(self.rolls[18:20]) >= 10:
            raise IndexError("Game is not complete")
            
        total = 0
        roll_index = 0
        
        for frame in range(10):
            if self.is_strike(roll_index):
                total += 10 + self.strike_bonus(roll_index)
                roll_index += 1
            elif self.is_spare(roll_index):
                total += 10 + self.spare_bonus(roll_index)
                roll_index += 2
            else:
                total += self.sum_of_balls_in_frame(roll_index)
                roll_index += 2
                
        return total

    def is_strike(self, roll_index):
        return self.rolls[roll_index] == 10

    def is_spare(self, roll_index):
        return self.rolls[roll_index] + self.rolls[roll_index + 1] == 10

    def sum_of_balls_in_frame(self, roll_index):
        return self.rolls[roll_index] + self.rolls[roll_index + 1]

    def strike_bonus(self, roll_index):
        return self.rolls[roll_index + 1] + self.rolls[roll_index + 2]

    def spare_bonus(self, roll_index):
        return self.rolls[roll_index + 2]
