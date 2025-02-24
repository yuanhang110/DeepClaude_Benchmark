class PhoneNumber:
    def __init__(self, number):
        # Process characters and validate input format
        digits = []
        has_letters = False
        
        for c in number:
            if c.isdigit():
                digits.append(c)
            elif c.isalpha():
                has_letters = True
        
        if has_letters:
            raise ValueError("letters not permitted")
        
        digits_str = ''.join(digits)
        num_digits = len(digits_str)
        
        # Validate digit count
        if num_digits < 10:
            raise ValueError("must not be fewer than 10 digits")
        if num_digits > 11:
            raise ValueError("must not be greater than 11 digits")
        
        # Validate country code
        if num_digits == 11:
            if digits_str[0] != '1':
                raise ValueError("11 digits must start with 1")
            digits_str = digits_str[1:]
        
        # Validate area code (NXX)
        area_code = digits_str[:3]
        if area_code[0] in ('0', '1'):
            raise ValueError(f"area code cannot start with {'zero' if area_code[0] == '0' else 'one'}")
        
        # Validate exchange code (NXX)
        exchange_code = digits_str[3:6]
        if exchange_code[0] in ('0', '1'):
            raise ValueError(f"exchange code cannot start with {'zero' if exchange_code[0] == '0' else 'one'}")
        
        self.number = digits_str
        self.area_code = digits_str[:3]
        self.exchange_code = digits_str[3:6]
        self.subscriber_number = digits_str[6:]
    def pretty(self):
        return f"({self.area_code})-{self.exchange_code}-{self.subscriber_number}"
