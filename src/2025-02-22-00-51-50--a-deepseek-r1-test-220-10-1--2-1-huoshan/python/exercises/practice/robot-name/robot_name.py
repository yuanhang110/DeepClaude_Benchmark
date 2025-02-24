import random
import string

class Robot:
    _used_names = set()

    def __init__(self):
        self._name = None
        self._previous_names = set()

    def reset(self):
        if self._name is not None:
            Robot._used_names.discard(self._name)
            self._previous_names.add(self._name)
            self._name = None

    @property
    def name(self):
        if self._name is None:
            while True:
                letters = ''.join(random.choices(string.ascii_uppercase, k=2))
                numbers = ''.join(random.choices(string.digits, k=3))
                new_name = f"{letters}{numbers}"
                if new_name not in Robot._used_names and new_name not in self._previous_names:
                    Robot._used_names.add(new_name)
                    self._name = new_name
                    break
        return self._name
