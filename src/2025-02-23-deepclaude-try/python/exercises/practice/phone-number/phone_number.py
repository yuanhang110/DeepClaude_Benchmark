class PhoneNumber:
    def __init__(self, number):
        # Clean the input number by removing any non-digit characters
        cleaned = ''.join(char for char in number if char.isdigit())
        
        # Check for letters
        if any(char.isalpha() for char in number):
            raise ValueError("letters not permitted")
            
        # Check for punctuation that's not in place of digits
        if any(char not in "0123456789-().+ " and not char.isalpha() for char in number):
            raise ValueError("punctuations not permitted")

        # Validate length
        if len(cleaned) < 10:
            raise ValueError("must not be fewer than 10 digits")
        if len(cleaned) > 11:
            raise ValueError("must not be greater than 11 digits")
        
        # Handle 11-digit numbers
        if len(cleaned) == 11:
            if cleaned[0] != '1':
                raise ValueError("11 digits must start with 1")
            cleaned = cleaned[1:]
            
        # Now we have a 10-digit number
        # Extract area code and exchange code
        area_code = cleaned[:3]
        exchange_code = cleaned[3:6]
        
        # Validate area code
        if area_code[0] == '0':
            raise ValueError("area code cannot start with zero")
        if area_code[0] == '1':
            raise ValueError("area code cannot start with one")
            
        # Validate exchange code
        if exchange_code[0] == '0':
            raise ValueError("exchange code cannot start with zero")
        if exchange_code[0] == '1':
            raise ValueError("exchange code cannot start with one")
            
        # Store the cleaned number
        self.number = cleaned
        
    def pretty(self):
        """Return the phone number formatted as (XXX)-XXX-XXXX."""
        return f"({self.number[:3]})-{self.number[3:6]}-{self.number[6:]}"
        
    @property
    def area_code(self):
        """Return the area code."""
        return self.number[:3]
        
    def __str__(self):
        """Return the phone number as a string."""
        return self.number
