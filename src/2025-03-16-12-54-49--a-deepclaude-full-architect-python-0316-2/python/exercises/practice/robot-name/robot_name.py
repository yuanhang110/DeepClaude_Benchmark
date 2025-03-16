import random
import string

class Robot:
    used_names = set()
    
    def __init__(self):
        self._name = None
        self.reset()
    
    def reset(self):
        """Reset the robot to factory settings, wiping its name."""
        old_name = self._name
        if self._name is not None:
            Robot.used_names.discard(self._name)
        
        # Generate a new name that's different from the old one
        while True:
            new_name = self._generate_name()
            if new_name != old_name:
                self._name = new_name
                break
    
    def _generate_name(self):
        """Generate a unique random name in the format of two uppercase letters followed by three digits."""
        while True:
            # Generate two random uppercase letters
            letters = ''.join(random.choices(string.ascii_uppercase, k=2))
            # Generate three random digits
            digits = ''.join(random.choices(string.digits, k=3))
            # Combine to form the name
            name = letters + digits
            
            # Ensure the name is unique
            if name not in Robot.used_names:
                Robot.used_names.add(name)
                return name
    
    @property
    def name(self):
        """Get the robot's name."""
        return self._name
