import random
import string

class Robot:
    _used_names = set()

    def __init__(self):
        self._name = None
        self._previous_name = None

    @property
    def name(self):
        """Get the robot's name, generating a new one if needed."""
        if self._name is None:
            self._name = self._generate_name()
        return self._name

    def reset(self):
        """Reset the robot to factory settings, wiping its name."""
        if self._name is not None:
            Robot._used_names.remove(self._name)
            self._previous_name = self._name
            self._name = None

    def _generate_name(self):
        """Generate a unique random name in the format of two uppercase letters followed by three digits."""
        while True:
            letters = ''.join(random.choices(string.ascii_uppercase, k=2))
            digits = ''.join(random.choices(string.digits, k=3))
            new_name = letters + digits
            
            if new_name not in Robot._used_names and new_name != self._previous_name:
                Robot._used_names.add(new_name)
                return new_name
