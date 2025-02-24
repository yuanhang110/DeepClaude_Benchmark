class BowlingGame:
    def __init__(self):
        self.rolls = []

    def roll(self, pins):
        if not 0 <= pins <= 10:
            raise ValueError("Invalid pins value, must be between 0 and 10")
        if self._is_game_over():
            raise ValueError("Game is already over")
        self.rolls.append(pins)
        self._validate_frames()

    def score(self):
        if not self._is_game_over():
            raise ValueError("Game is not yet over")
        total = 0
        pos = 0
        for frame in range(10):
            if pos >= len(self.rolls):
                break
            if frame == 9:
                total += sum(self.rolls[pos:])
                break
            current = self.rolls[pos]
            if current == 10:
                total += 10 + self._strike_bonus(pos)
                pos += 1
            else:
                if pos + 1 >= len(self.rolls):
                    total += current
                    break
                next_roll = self.rolls[pos + 1]
                if current + next_roll == 10:
                    total += 10 + self._spare_bonus(pos + 1)
                else:
                    total += current + next_roll
                pos += 2
        return total

    def _strike_bonus(self, pos):
        bonus = 0
        if pos + 1 < len(self.rolls):
            bonus += self.rolls[pos + 1]
        if pos + 2 < len(self.rolls):
            bonus += self.rolls[pos + 2]
        return bonus

    def _spare_bonus(self, pos):
        if pos + 1 < len(self.rolls):
            return self.rolls[pos + 1]
        return 0

    def _is_game_over(self):
        pos = 0
        frames = 0
        while pos < len(self.rolls) and frames < 10:
            if self.rolls[pos] == 10:
                frames += 1
                pos += 1
            else:
                if pos + 1 >= len(self.rolls):
                    break
                if self.rolls[pos] + self.rolls[pos + 1] > 10:
                    return False
                frames += 1
                pos += 2
        if frames < 10:
            return False
        pos = 0
        for _ in range(9):
            if pos >= len(self.rolls):
                return False
            if self.rolls[pos] == 10:
                pos += 1
            else:
                pos += 2
        if pos >= len(self.rolls):
            return False
        first = self.rolls[pos]
        if first == 10:
            return pos + 2 < len(self.rolls)
        else:
            if pos + 1 >= len(self.rolls):
                return False
            second = self.rolls[pos + 1]
            if first + second == 10:
                return pos + 2 < len(self.rolls)
            else:
                return True

    def _validate_frames(self):
        pos = 0
        for frame in range(9):
            if pos >= len(self.rolls):
                break
            first = self.rolls[pos]
            if first == 10:
                pos += 1
            else:
                if pos + 1 >= len(self.rolls):
                    break
                second = self.rolls[pos + 1]
                if first + second > 10:
                    raise ValueError(f"Frame {frame + 1} has invalid sum of {first} and {second}")
                pos += 2

        # Validate 10th frame rules
        if pos < len(self.rolls):
            first = self.rolls[pos]
            if first == 10:  # Strike in 10th frame
                if pos + 1 < len(self.rolls):
                    second = self.rolls[pos + 1]
                    if pos + 2 < len(self.rolls):
                        third = self.rolls[pos + 2]
                        if second != 10 and second + third > 10:
                            raise ValueError("Two bonus rolls after strike in 10th frame cannot exceed 10 pins")
            elif pos + 1 < len(self.rolls):
                second = self.rolls[pos + 1]
                if first + second == 10:  # Spare in 10th frame
                    if pos + 2 < len(self.rolls):
                        third = self.rolls[pos + 2]
                        if not 0 <= third <= 10:
                            raise ValueError("Invalid fill ball after spare in 10th frame")
                else:  # Open frame
                    if pos + 2 < len(self.rolls):
                        raise ValueError("Extra roll(s) present after open 10th frame")
