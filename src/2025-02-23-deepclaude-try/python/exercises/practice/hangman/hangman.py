# Game status categories
# Change the values as you see fit
STATUS_WIN = 'win'
STATUS_LOSE = 'lose'
STATUS_ONGOING = 'ongoing'


class Hangman:
    def __init__(self, word):
        self.remaining_guesses = 10
        self.status = STATUS_ONGOING
        self.word = word
        self.guessed_letters = set()
        self.correct_guesses = set()

    def guess(self, char):
        if self.status != STATUS_ONGOING:
            raise ValueError("The game has already ended.")
        if char in self.guessed_letters:
            self.remaining_guesses -= 1
        else:
            self.guessed_letters.add(char)
            if char not in self.word:
                self.remaining_guesses -= 1
        if char not in self.word:
            self.remaining_guesses -= 1
        self.guessed_letters.add(char)
        if self.remaining_guesses < 0:
            self.status = STATUS_LOSE
        elif all(letter in self.guessed_letters for letter in self.word):
            self.status = STATUS_WIN

    def get_masked_word(self):
        return ''.join(char if char in self.guessed_letters else '_' for char in self.word)

    def get_status(self):
        return self.status
