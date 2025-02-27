class PhoneNumber:
    def __init__(self, number):
        # Check for letters
        if any(c.isalpha() for c in number):
            raise ValueError("letters not permitted")
        
        # Check for invalid punctuation
        allowed_punctuation = {'+', '-', '.', ' ', '(', ')'}
        for c in number:
            if not c.isdigit() and c not in allowed_punctuation:
                raise ValueError("punctuations not permitted")
        
        # Extract digits
        digits = ''.join(filter(str.isdigit, number))
        
        # Check length
        if len(digits) < 10:
            raise ValueError("must not be fewer than 10 digits")
        if len(digits) > 11:
            raise ValueError("must not be greater than 11 digits")
        
        # Handle country code
        if len(digits) == 11:
            if digits[0] != '1':
                raise ValueError("11 digits must start with 1")
            digits = digits[1:]
        
        # Check area code
        area_code = digits[0:3]
        if area_code[0] == '0':
            raise ValueError("area code cannot start with zero")
        if area_code[0] == '1':
            raise ValueError("area code cannot start with one")
        
        # Check exchange code
        exchange_code = digits[3:6]
        if exchange_code[0] == '0':
            raise ValueError("exchange code cannot start with zero")
        if exchange_code[0] == '1':
            raise ValueError("exchange code cannot start with one")
        
        self.number = digits
        self.area_code = digits[0:3]
        self.exchange_code = digits[3:6]
        self.subscriber_number = digits[6:10]

    def pretty(self):
        return f"({self.area_code})-{self.exchange_code}-{self.subscriber_number}"
