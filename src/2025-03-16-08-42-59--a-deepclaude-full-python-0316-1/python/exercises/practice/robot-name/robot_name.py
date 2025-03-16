import random
import string

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
            # Generate a name with 2 uppercase letters followed by 3 digits
            letters = ''.join(random.choices(string.ascii_uppercase, k=2))
            digits = ''.join(random.choices(string.digits, k=3))
            new_name = letters + digits
            
            # Ensure the name is unique
            if new_name not in Robot._used_names:
                Robot._used_names.add(new_name)
                self._name = new_name
                break
    
    def reset(self):
        # Remove the current name from used names
        if self._name:
            Robot._used_names.remove(self._name)
        
        # Generate a new name
        self._generate_name()
