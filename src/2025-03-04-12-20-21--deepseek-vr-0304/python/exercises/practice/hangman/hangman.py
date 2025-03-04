# Game status categories
# Change the values as you see fit
STATUS_WIN = 'win'
STATUS_LOSE = 'lose'
STATUS_ONGOING = 'ongoing'


class Hangman:
    def __init__(self, word):
        self.remaining_guesses = 9
        self.status = STATUS_ONGOING
        self.word = word
        self.guessed_letters = set()

    def guess(self, char):
        if self.status != STATUS_ONGOING:
            raise ValueError("The game has already ended.")
        if char in self.word:
            if char not in self.guessed_letters:
                self.guessed_letters.add(char)
            else:
                self.remaining_guesses -= 1
        else:
            self.remaining_guesses -= 1
        # Check for win condition
        if all(c in self.guessed_letters for c in self.word):
            self.status = STATUS_WIN
        elif self.remaining_guesses < 0:
            self.status = STATUS_LOSE

    def get_masked_word(self):
        return ''.join([c if c in self.guessed_letters else '_' for c in self.word])

    def get_status(self):
        return self.status
