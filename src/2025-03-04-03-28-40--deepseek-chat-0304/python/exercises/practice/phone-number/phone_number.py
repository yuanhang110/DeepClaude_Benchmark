import re

class PhoneNumber:
    def __init__(self, number):
        # Check for letters first
        if re.search(r'[a-zA-Z]', number):
            raise ValueError("letters not permitted")
            
        # Remove all non-digit characters
        cleaned = re.sub(r'[^0-9]', '', number)
        
        # Validate length
        if len(cleaned) < 10:
            raise ValueError("must not be fewer than 10 digits")
        if len(cleaned) > 11:
            raise ValueError("must not be greater than 11 digits")
            
        # Handle 11-digit numbers (must start with 1)
        if len(cleaned) == 11:
            if cleaned[0] != '1':
                raise ValueError("11 digits must start with 1")
            cleaned = cleaned[1:]  # Remove country code
            
        # Validate area code
        if cleaned[0] == '0':
            raise ValueError("area code cannot start with zero")
        if cleaned[0] == '1':
            raise ValueError("area code cannot start with one")
            
        # Validate exchange code
        if cleaned[3] == '0':
            raise ValueError("exchange code cannot start with zero")
        if cleaned[3] == '1':
            raise ValueError("exchange code cannot start with one")
            
        self._number = cleaned
        
    @property
    def number(self):
        return self._number
        
    @property
    def area_code(self):
        return self._number[:3]
        
    def pretty(self):
        return f"({self._number[:3]})-{self._number[3:6]}-{self._number[6:]}"
