# Game status categories
# Change the values as you see fit
STATUS_WIN = 'win'
STATUS_LOSE = 'lose'
STATUS_ONGOING = 'ongoing'


class Hangman:
    def __init__(self, word):
        self.remaining_guesses = 9
        self.status = STATUS_ONGOING
        self.word = word.lower()
        self.guessed = set()

    def guess(self, char):
        if self.status != STATUS_ONGOING:
            raise ValueError("The game has already ended.")
        char = char.lower()
        if char in self.guessed:
            return
        self.guessed.add(char)
        if char not in self.word:
            self.remaining_guesses -= 1
        # Check win condition
        all_guessed = True
        for c in self.word:
            if c.isalpha() and c not in self.guessed:
                all_guessed = False
                break
        if all_guessed:
            self.status = STATUS_WIN
        elif self.remaining_guesses <= 0:
            self.status = STATUS_LOSE

    def get_masked_word(self):
        masked = []
        for c in self.word:
            if c in self.guessed or not c.isalpha():
                masked.append(c)
            else:
                masked.append('_')
        return ''.join(masked)

    def get_status(self):
        return self.status
