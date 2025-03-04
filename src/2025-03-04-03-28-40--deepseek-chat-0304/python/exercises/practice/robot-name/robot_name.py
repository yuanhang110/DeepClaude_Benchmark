import random
import string

class Robot:
    _used_names = set()

    def __init__(self):
        self._name = None

    def generate_name(self):
        while True:
            letters = ''.join(random.choices(string.ascii_uppercase, k=2))
            digits = ''.join(random.choices(string.digits, k=3))
            name = letters + digits
            if name not in self._used_names:
                self._used_names.add(name)
                return name

    @property
    def name(self):
        if not self._name:
            self._name = self.generate_name()
        return self._name

    def reset(self):
        if self._name:
            self._used_names.remove(self._name)
            self._name = None
            self._name = self.generate_name()
