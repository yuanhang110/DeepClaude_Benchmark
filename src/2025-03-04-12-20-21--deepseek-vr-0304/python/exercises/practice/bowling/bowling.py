class BowlingGame:
    def __init__(self):
        self.rolls = []
        self.current_frame = 1
        self.game_active = True
        self.rolls_before_10th = 0

    def roll(self, pins):
        if not self.game_active:
            raise ValueError("Cannot roll after game is over")
        if pins < 0 or pins > 10:
            raise ValueError("Invalid number of pins (must be 0-10)")
            
        if self.current_frame < 10:
            if not self.rolls or self.rolls[-1] == 10 or len(self.rolls) % 2 == 0:
                # First roll in frame or previous was strike
                if pins == 10:
                    self.rolls.append(pins)
                    self.current_frame += 1
                else:
                    self.rolls.append(pins)
            else:
                # Second roll in frame
                if self.rolls[-1] + pins > 10:
                    raise ValueError("Sum of frame exceeds 10")
                self.rolls.append(pins)
                self.current_frame += 1
        else:
            # Handle 10th frame
            if not hasattr(self, 'rolls_before_10th_set'):
                self.rolls_before_10th = len(self.rolls)
                self.rolls_before_10th_set = True
                
            current_10th_rolls = len(self.rolls) - self.rolls_before_10th
            if current_10th_rolls >= 3:
                self.game_active = False
                raise ValueError("Too many rolls in 10th frame")
                
            if current_10th_rolls == 0:
                self.rolls.append(pins)
            elif current_10th_rolls == 1:
                first = self.rolls[self.rolls_before_10th]
                if first == 10:
                    # Strike, allow any second roll
                    self.rolls.append(pins)
                else:
                    if first + pins > 10:
                        raise ValueError("Invalid 10th frame roll")
                    self.rolls.append(pins)
                    if first + pins < 10:
                        self.game_active = False
            else:  # current_10th_rolls == 2
                first = self.rolls[self.rolls_before_10th]
                if first == 10:
                    # Check second roll (index+1)
                    second = self.rolls[self.rolls_before_10th + 1]
                    if second != 10 and pins > 10 - second:
                        raise ValueError("Invalid third roll value")
                else:
                    # Must have been a spare
                    if self.rolls[self.rolls_before_10th] + self.rolls[self.rolls_before_10th + 1] != 10:
                        raise ValueError("Extra roll in non-spare 10th frame")
                self.rolls.append(pins)
                self.game_active = False

    def score(self):
        if self.game_active:
            raise ValueError("Game not complete")
            
        total = 0
        i = 0
        for frame in range(10):
            if i >= len(self.rolls):
                break
                
            if frame == 9:  # 10th frame
                total += sum(self.rolls[i:])
                break
                
            # Strike
            if self.rolls[i] == 10:
                total += 10
                # Add next two rolls if available
                if i+1 < len(self.rolls):
                    total += self.rolls[i+1]
                if i+2 < len(self.rolls):
                    total += self.rolls[i+2]
                i += 1
            else:
                # Spare or open
                if i+1 >= len(self.rolls):
                    total += self.rolls[i]
                    break
                    
                frame_total = self.rolls[i] + self.rolls[i+1]
                total += frame_total
                
                if frame_total == 10:  # Spare
                    if i+2 < len(self.rolls):
                        total += self.rolls[i+2]
                i += 2
        return total
