class BowlingGame:
    def __init__(self):
        self.rolls = []
        self.game_over = False

    def roll(self, pins):
        if pins < 0 or pins > 10:
            raise ValueError("Invalid roll: pins must be between 0 and 10")
        if self.game_over:
            raise ValueError("Cannot roll after game is over")
        
        # Check for valid frame sums in frames 1-9
        temp_rolls = self.rolls + [pins]
        current_roll = 0
        for frame in range(9):
            if current_roll >= len(temp_rolls):
                break
            first = temp_rolls[current_roll]
            if first == 10:  # Strike
                current_roll += 1
            else:
                if current_roll + 1 >= len(temp_rolls):
                    break
                second = temp_rolls[current_roll + 1]
                if first + second > 10:
                    raise ValueError("Invalid frame: sum exceeds 10 in frame {}".format(frame + 1))
                current_roll += 2
        
        # Check 10th frame fill balls after strike
        if current_roll < len(temp_rolls):
            if temp_rolls[current_roll] == 10:  # Strike in 10th frame
                if current_roll + 2 < len(temp_rolls):
                    fill1 = temp_rolls[current_roll + 1]
                    fill2 = temp_rolls[current_roll + 2]
                    if fill1 != 10 and fill1 + fill2 > 10:
                        raise ValueError("invalid fill balls")
        
        self.rolls.append(pins)
        
        # Check if game is over
        current_roll = 0
        for frame in range(10):
            if current_roll >= len(self.rolls):
                break
            if frame < 9:
                if self.rolls[current_roll] == 10:  # Strike
                    current_roll += 1
                else:
                    if current_roll + 1 >= len(self.rolls):
                        break
                    current_roll += 2
            else:
                # Frame 10 logic
                if self.rolls[current_roll] == 10:  # Strike
                    # Need two more rolls
                    if current_roll + 2 < len(self.rolls):
                        self.game_over = True
                    break
                else:
                    if current_roll + 1 >= len(self.rolls):
                        break
                    if self.rolls[current_roll] + self.rolls[current_roll + 1] == 10:  # Spare
                        # Need one more roll
                        if current_roll + 2 < len(self.rolls):
                            self.game_over = True
                        break
                    else:  # Open frame
                        self.game_over = True
                        break

    def score(self):
        if not self.game_over:
            raise ValueError("Game is not over yet")
        
        total = 0
        current_roll = 0
        
        for frame in range(10):
            if current_roll >= len(self.rolls):
                raise ValueError("Incomplete game")
                
            if self.rolls[current_roll] == 10:  # Strike
                total += 10
                
                # Add bonus rolls
                if current_roll + 1 >= len(self.rolls):
                    raise ValueError("Incomplete game")
                if current_roll + 2 >= len(self.rolls):
                    raise ValueError("Incomplete game")
                    
                total += self.rolls[current_roll + 1] + self.rolls[current_roll + 2]
                current_roll += 1
            elif current_roll + 1 < len(self.rolls) and self.rolls[current_roll] + self.rolls[current_roll + 1] == 10:  # Spare
                total += 10
                
                # Add bonus roll
                if current_roll + 2 >= len(self.rolls):
                    raise ValueError("Incomplete game")
                    
                total += self.rolls[current_roll + 2]
                current_roll += 2
            else:  # Open frame
                if current_roll + 1 >= len(self.rolls):
                    raise ValueError("Incomplete game")
                    
                total += self.rolls[current_roll] + self.rolls[current_roll + 1]
                current_roll += 2
                
            # Special handling for 10th frame
            if frame == 9:
                # For a strike or spare in 10th frame, we've already counted bonus rolls in the frame total
                # No additional action needed as they're included in the running total
                pass
                
        return total
