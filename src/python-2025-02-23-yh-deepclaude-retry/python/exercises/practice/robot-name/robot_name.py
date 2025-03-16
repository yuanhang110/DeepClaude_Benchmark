import random

class Robot:
    _used_names = set()

    def __init__(self):
        self._name = None
        self._generate_name()

    @property
    def name(self):
        return self._name

    def _generate_name(self):
        while True:
            letters = ''.join(random.choices('ABCDEFGHIJKLMNOPQRSTUVWXYZ', k=2))
            digits = ''.join(random.choices('0123456789', k=3))
            new_name = letters + digits
            if new_name not in Robot._used_names:
                Robot._used_names.add(new_name)
                self._name = new_name
                break

    def reset(self):
        if self._name in Robot._used_names:
            Robot._used_names.remove(self._name)
        self._generate_name()
