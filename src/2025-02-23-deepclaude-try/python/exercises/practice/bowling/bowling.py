class BowlingGame:
    def __init__(self):
        self.rolls = []

    def roll(self, pins):
        if self._is_game_over():
            raise ValueError("Cannot roll after game is over")
        if not 0 <= pins <= 10:
            raise ValueError("Invalid pins value, must be between 0 and 10")
        if not self._is_valid_roll(pins):
            raise ValueError("Invalid roll: invalid number of pins for current frame")
        self.rolls.append(pins)

    def score(self):
        if not self._is_game_over():
            raise ValueError("Game is not yet complete")
        
        total = 0
        i = 0
        for frame in range(10):
            if frame == 9:  # Tenth frame
                total += sum(self.rolls[i:])
                break
            if self.rolls[i] == 10:  # Strike
                total += 10 + self._strike_bonus(i)
                i += 1
            else:
                if self._is_spare(i):
                    total += 10 + self._spare_bonus(i)
                else:
                    total += self.rolls[i] + self.rolls[i + 1]
                i += 2
        return total

    def _is_game_over(self):
        rolls = self.rolls
        frame = 0
        i = 0
        n = len(rolls)
        while frame < 10 and i < n:
            if frame == 9:  # Tenth frame
                if rolls[i] == 10:  # Strike
                    return i + 2 < n
                else:
                    if i + 1 < n:
                        total = rolls[i] + rolls[i + 1]
                        if total == 10:  # Spare
                            return i + 2 < n
                        else:  # Open
                            return i + 1 < n
                    else:
                        return False
            else:  # Frames 1-9
                if rolls[i] == 10:  # Strike
                    frame += 1
                    i += 1
                else:
                    if i + 1 < n:
                        frame += 1
                        i += 2
                    else:
                        return False
        return frame >= 10

    def _is_valid_roll(self, pins):
        temp_rolls = self.rolls.copy()
        temp_rolls.append(pins)
        frame = 0
        i = 0
        
        # Process frames 1-9
        while frame < 9 and i < len(temp_rolls):
            if temp_rolls[i] == 10:  # Strike
                frame += 1
                i += 1
            else:
                if i + 1 < len(temp_rolls):
                    first = temp_rolls[i]
                    second = temp_rolls[i + 1]
                    if first + second > 10:
                        return False
                    frame += 1
                    i += 2
                else:
                    break
        
        # Handle 10th frame special cases
        if frame == 9 and i < len(temp_rolls):
            if temp_rolls[i] == 10:  # Strike in 10th frame
                if i + 1 < len(temp_rolls):  # First bonus roll
                    first_bonus = temp_rolls[i + 1]
                    if i + 2 < len(temp_rolls):  # Second bonus roll
                        second_bonus = temp_rolls[i + 2]
                        # If first bonus isn't strike, second bonus + first bonus must be <= 10
                        if first_bonus < 10 and (first_bonus + second_bonus > 10 or second_bonus == 10):
                            return False
            else:  # Non-strike in 10th frame
                if i + 1 < len(temp_rolls):
                    if temp_rolls[i] + temp_rolls[i + 1] > 10:
                        return False
        
        return True

    def _strike_bonus(self, i):
        bonus = 0
        next_rolls = 2
        for j in range(i + 1, min(i + 3, len(self.rolls))):
            bonus += self.rolls[j]
            next_rolls -= 1
            if next_rolls == 0:
                break
        return bonus

    def _is_spare(self, i):
        return self.rolls[i] + self.rolls[i + 1] == 10

    def _spare_bonus(self, i):
        return self.rolls[i + 2] if i + 2 < len(self.rolls) else 0
