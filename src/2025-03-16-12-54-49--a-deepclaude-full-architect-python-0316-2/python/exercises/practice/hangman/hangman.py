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
            
        # If letter was already guessed, count as a wrong guess
        if char in self.guessed_letters:
            self.remaining_guesses -= 1
            self._update_status()
            return
            
        # Add to guessed letters
        self.guessed_letters.add(char)
        
        # If letter is not in the word, decrease remaining guesses
        if char not in self.word:
            self.remaining_guesses -= 1
            
        # Update game status
        self._update_status()

    def _update_status(self):
        # Win condition: all letters in word have been guessed
        if all(letter in self.guessed_letters for letter in self.word):
            self.status = STATUS_WIN
        # Lose condition: no more guesses remaining
        elif self.remaining_guesses <= 0:
            self.status = STATUS_LOSE
            
    def get_masked_word(self):
        # Return the word with unguessed letters masked as '_'
        return ''.join(letter if letter in self.guessed_letters else '_' 
                      for letter in self.word)

    def get_status(self):
        return self.status
