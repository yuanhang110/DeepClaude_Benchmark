# Game status categories
STATUS_WIN = 'win'
STATUS_LOSE = 'lose'
STATUS_ONGOING = 'ongoing'


class Hangman:
    def __init__(self, word):
        self.word = word
        self.remaining_guesses = 9
        self.guessed_letters = set()
        self.status = STATUS_ONGOING

    def guess(self, char):
        if self.status != STATUS_ONGOING:
            raise ValueError("The game has already ended.")
            
        if char not in self.guessed_letters:
            self.guessed_letters.add(char)
            if char not in self.word or char in self.guessed_letters:
                self.remaining_guesses -= 1
                
        self._update_status()

    def get_masked_word(self):
        return ''.join(
            letter if letter in self.guessed_letters else '_'
            for letter in self.word
        )

    def get_status(self):
        return self.status

    def _update_status(self):
        if all(letter in self.guessed_letters for letter in self.word):
            self.status = STATUS_WIN
        elif self.remaining_guesses <= 0:
            self.status = STATUS_LOSE
