class PhoneNumber:
    def __init__(self, number):
        # Convert to string in case it's passed as a number
        number_str = str(number)
        
        # Check for letters
        if any(c.isalpha() for c in number_str):
            raise ValueError("letters not permitted")
        
        # Extract only digits
        digits = ''.join(c for c in number_str if c.isdigit())
        
        # Check if we lost any characters that weren't spaces or valid punctuation
        valid_non_digits = set('+-().  ')
        if any(c not in valid_non_digits and not c.isdigit() for c in number_str):
            raise ValueError("punctuations not permitted")
        
        # Validate digit count
        if len(digits) < 10:
            raise ValueError("must not be fewer than 10 digits")
        if len(digits) > 11:
            raise ValueError("must not be greater than 11 digits")
        
        # Handle country code
        if len(digits) == 11:
            if digits[0] != '1':
                raise ValueError("11 digits must start with 1")
            digits = digits[1:]
        
        # At this point, digits should be exactly 10 digits
        
        # Validate area code (first 3 digits)
        if digits[0] == '0':
            raise ValueError("area code cannot start with zero")
        if digits[0] == '1':
            raise ValueError("area code cannot start with one")
        
        # Validate exchange code (next 3 digits)
        if digits[3] == '0':
            raise ValueError("exchange code cannot start with zero")
        if digits[3] == '1':
            raise ValueError("exchange code cannot start with one")
        
        # Store the clean number
        self.number = digits
        
    def pretty(self):
        """Return the phone number in a more readable format."""
        return f"({self.number[:3]})-{self.number[3:6]}-{self.number[6:]}"
        
    @property
    def area_code(self):
        """Return the area code."""
        return self.number[:3]
        
    def __str__(self):
        """Return the phone number as a string."""
        return self.number
