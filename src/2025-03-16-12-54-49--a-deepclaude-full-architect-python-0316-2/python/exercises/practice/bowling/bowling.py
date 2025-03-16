class BowlingGame:
    def __init__(self):
        self.rolls = []
        self.current_frame = 1
        self.is_game_finished = False

    def roll(self, pins):
        # Validate the pins value
        if pins < 0:
            raise ValueError("pins cannot be negative")
        if pins > 10:
            raise ValueError("pins cannot exceed 10")
            
        # Check if game is already finished
        if self.is_game_finished:
            raise IndexError("cannot roll after game is done")
            
        # Add roll to the list
        self.rolls.append(pins)
        
        # Validate frame total doesn't exceed 10 (except in 10th frame with bonus)
        self._validate_current_roll()
        
        # Update game status
        self._update_game_status()

    def score(self):
        # Cannot score an incomplete game
        if not self._is_game_complete():
            raise ValueError("score cannot be calculated until the game is complete")
            
        score = 0
        roll_index = 0
        
        # Score for all 10 frames
        for frame in range(1, 11):
            if self._is_strike(roll_index):
                # Strike: 10 + next two rolls
                score += 10 + self.rolls[roll_index + 1] + self.rolls[roll_index + 2]
                roll_index += 1
            elif self._is_spare(roll_index):
                # Spare: 10 + next roll
                score += 10 + self.rolls[roll_index + 2]
                roll_index += 2
            else:
                # Open frame: sum of two rolls
                score += self.rolls[roll_index] + self.rolls[roll_index + 1]
                roll_index += 2
                
        return score
        
    def _is_strike(self, roll_index):
        return self.rolls[roll_index] == 10
        
    def _is_spare(self, roll_index):
        return self.rolls[roll_index] + self.rolls[roll_index + 1] == 10
        
    def _is_game_complete(self):
        # Game is incomplete if no rolls
        if not self.rolls:
            return False
            
        frames = 0
        rolls_needed = 0
        roll_index = 0
        
        while frames < 10 and roll_index < len(self.rolls):
            # Handle strike
            if self.rolls[roll_index] == 10:
                frames += 1
                roll_index += 1
                # In 10th frame, need 2 more rolls after strike
                if frames == 10:
                    rolls_needed = 2
            else:
                # Need at least 2 rolls for this frame
                if roll_index + 1 >= len(self.rolls):
                    return False
                    
                # Handle spare
                if self.rolls[roll_index] + self.rolls[roll_index + 1] == 10:
                    # In 10th frame, need 1 more roll after spare
                    if frames == 9:
                        rolls_needed = 1
                
                frames += 1
                roll_index += 2
        
        # Check if we have all the needed rolls for the 10th frame
        return frames == 10 and len(self.rolls) >= roll_index + rolls_needed
    
    def _validate_current_roll(self):
        roll_index = 0
        frame = 1
        
        while frame < 10 and roll_index < len(self.rolls):
            # Strike
            if self.rolls[roll_index] == 10:
                roll_index += 1
                frame += 1
            else:
                # Need two rolls for a frame
                if roll_index + 1 < len(self.rolls):
                    # Check if frame total exceeds 10
                    if self.rolls[roll_index] + self.rolls[roll_index + 1] > 10:
                        raise ValueError("two rolls in a frame cannot score more than 10 points")
                roll_index += 2
                frame += 1
        
        # Special handling for 10th frame
        if frame == 10 and roll_index < len(self.rolls):
            # First roll in 10th frame
            if self.rolls[roll_index] == 10:  # Strike in 10th
                if roll_index + 1 < len(self.rolls):  # First bonus
                    if self.rolls[roll_index + 1] > 10:
                        raise ValueError("bonus roll cannot score more than 10 points")
                    
                    if roll_index + 2 < len(self.rolls):  # Second bonus
                        # If first bonus is strike, second can be up to 10
                        # Otherwise second bonus + first bonus cannot exceed 10
                        if self.rolls[roll_index + 1] == 10:
                            if self.rolls[roll_index + 2] > 10:
                                raise ValueError("bonus roll cannot score more than 10 points")
                        else:
                            if self.rolls[roll_index + 1] + self.rolls[roll_index + 2] > 10:
                                raise ValueError("two rolls in a frame cannot score more than 10 points")
            else:
                # First two rolls in 10th frame (not starting with strike)
                if roll_index + 1 < len(self.rolls):
                    # If it's a spare, one bonus roll allowed
                    if self.rolls[roll_index] + self.rolls[roll_index + 1] == 10:
                        if roll_index + 2 < len(self.rolls) and self.rolls[roll_index + 2] > 10:
                            raise ValueError("bonus roll cannot score more than 10 points")
                    # If not a spare, no bonus rolls allowed
                    elif self.rolls[roll_index] + self.rolls[roll_index + 1] < 10:
                        if roll_index + 2 < len(self.rolls):
                            raise IndexError("cannot roll after game is done")
                    else:
                        raise ValueError("two rolls in a frame cannot score more than 10 points")
    
    def _update_game_status(self):
        # Check if game is complete after this roll
        roll_index = 0
        frame = 1
        
        while frame < 10 and roll_index < len(self.rolls):
            # Strike
            if self.rolls[roll_index] == 10:
                roll_index += 1
                frame += 1
            else:
                # Need two rolls for a frame
                if roll_index + 1 >= len(self.rolls):
                    return  # Need more rolls
                roll_index += 2
                frame += 1
        
        # Handle 10th frame
        if frame == 10 and roll_index < len(self.rolls):
            # Strike in 10th frame - need 2 more rolls
            if roll_index < len(self.rolls) and self.rolls[roll_index] == 10:
                if roll_index + 2 >= len(self.rolls):
                    return  # Need more rolls
                self.is_game_finished = True
            # Spare in 10th frame - need 1 more roll
            elif roll_index + 1 < len(self.rolls) and self.rolls[roll_index] + self.rolls[roll_index + 1] == 10:
                if roll_index + 2 >= len(self.rolls):
                    return  # Need more rolls
                self.is_game_finished = True
            # Open 10th frame - no extra rolls
            elif roll_index + 1 < len(self.rolls):
                self.is_game_finished = True
