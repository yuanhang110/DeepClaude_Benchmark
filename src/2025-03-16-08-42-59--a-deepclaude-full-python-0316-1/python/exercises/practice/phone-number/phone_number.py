class PhoneNumber:
    def __init__(self, number):
        # Clean the number by removing allowed punctuation
        cleaned = []
        for char in number:
            if char.isdigit():
                cleaned.append(char)
            elif char in ['+', ' ', '(', ')', '-', '.']:
                continue
            elif char.isalpha():
                raise ValueError("letters not permitted")
            else:
                raise ValueError("punctuations not permitted")
        
        cleaned_number = ''.join(cleaned)
        
        # Check length requirements
        if len(cleaned_number) < 10:
            raise ValueError("must not be fewer than 10 digits")
        if len(cleaned_number) > 11:
            raise ValueError("must not be greater than 11 digits")
        
        # Check if 11 digits starts with 1
        if len(cleaned_number) == 11:
            if cleaned_number[0] != '1':
                raise ValueError("11 digits must start with 1")
            cleaned_number = cleaned_number[1:]
        
        # Now we have a 10-digit number, validate area code and exchange code
        if cleaned_number[0] == '0':
            raise ValueError("area code cannot start with zero")
        if cleaned_number[0] == '1':
            raise ValueError("area code cannot start with one")
        
        if cleaned_number[3] == '0':
            raise ValueError("exchange code cannot start with zero")
        if cleaned_number[3] == '1':
            raise ValueError("exchange code cannot start with one")
        
        # Store the cleaned number and area code
        self.number = cleaned_number
        self.area_code = cleaned_number[:3]
    
    def pretty(self):
        """Format the phone number in a pretty format: (XXX)-XXX-XXXX"""
        return f"({self.area_code})-{self.number[3:6]}-{self.number[6:]}"
