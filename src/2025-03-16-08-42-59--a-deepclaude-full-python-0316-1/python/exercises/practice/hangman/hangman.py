# Game status categories
# Change the values as you see fit
STATUS_WIN = 'win'
STATUS_LOSE = 'lose'
STATUS_ONGOING = 'ongoing'


class Hangman:
    def __init__(self, word):
        self.word = word
        self.guessed = set()
        self.remaining_guesses = 9
        self.status = STATUS_ONGOING

    def guess(self, char):
        if self.status != STATUS_ONGOING:
            raise ValueError("The game has already ended.")
            
        if char in self.guessed:
            self.remaining_guesses -= 1
        else:
            self.guessed.add(char)
            if char not in self.word:
                self.remaining_guesses -= 1
            
        # Check win condition - all letters guessed
        if all(c in self.guessed for c in self.word):
            self.status = STATUS_WIN
            
        # Check lose condition - no more guesses
        if self.remaining_guesses < 0:
            self.status = STATUS_LOSE

    def get_masked_word(self):
        return ''.join(c if c in self.guessed else '_' for c in self.word)

    def get_status(self):
        return self.status
