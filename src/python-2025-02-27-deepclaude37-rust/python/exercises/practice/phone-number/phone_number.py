class PhoneNumber:
    def __init__(self, number):
        cleaned = []
        allowed_punctuation = {'+', '-', '(', ')', '.', ' '}
        for c in number:
            if c.isalpha():
                raise ValueError("letters not permitted")
            elif c.isdigit():
                cleaned.append(c)
            else:
                if c not in allowed_punctuation:
                    raise ValueError("punctuations not permitted")

        digits = ''.join(cleaned)
        
        if len(digits) < 10:
            raise ValueError("must not be fewer than 10 digits")
        if len(digits) > 11:
            raise ValueError("must not be greater than 11 digits")
        
        if len(digits) == 11:
            if digits[0] != '1':
                raise ValueError("11 digits must start with 1")
            digits = digits[1:]
        
        self.area_code = digits[:3]
        if self.area_code[0] == '0':
            raise ValueError("area code cannot start with zero")
        if self.area_code[0] == '1':
            raise ValueError("area code cannot start with one")
        
        self.exchange_code = digits[3:6]
        if self.exchange_code[0] == '0':
            raise ValueError("exchange code cannot start with zero")
        if self.exchange_code[0] == '1':
            raise ValueError("exchange code cannot start with one")
        
        self.number = digits

    def pretty(self):
        return f"({self.area_code})-{self.exchange_code}-{self.number[6:]}"
