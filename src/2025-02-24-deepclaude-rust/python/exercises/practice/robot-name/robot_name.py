import random
import string

class Robot:
    used_names = set()

    def __init__(self):
        self._name = None
        self.rng = random.Random()

    @property
    def name(self):
        if self._name is None:
            while True:
                new_name = self._generate_random_name()
                if new_name not in Robot.used_names:
                    Robot.used_names.add(new_name)
                    self._name = new_name
                    break
        return self._name

    def _generate_random_name(self):
        letters = ''.join(self.rng.choices(string.ascii_uppercase, k=2))
        number = self.rng.randint(0, 999)
        return f"{letters}{number:03d}"

    def reset(self):
        if self._name is not None:
            Robot.used_names.discard(self._name)
            self._name = None
