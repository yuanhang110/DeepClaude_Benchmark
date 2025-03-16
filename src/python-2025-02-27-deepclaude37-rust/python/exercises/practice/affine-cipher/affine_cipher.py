import math

def encode(plain_text, a, b):
    m = 26
    if math.gcd(a, m) != 1:
        raise ValueError("a and m must be coprime.")
    
    processed = []
    for c in plain_text:
        if c.isalpha():
            # Convert to lowercase, then encode
            i = ord(c.lower()) - ord('a')
            encrypted = (a * i + b) % m
            processed.append(chr(encrypted + ord('a')))
        elif c.isdigit():
            # Digits are kept as is
            processed.append(c)
        # Spaces and punctuation are excluded
    
    # Group the result into chunks of 5 characters
    result = ''.join(processed)
    return ' '.join([result[i:i+5] for i in range(0, len(result), 5)])


def decode(ciphered_text, a, b):
    m = 26
    if math.gcd(a, m) != 1:
        raise ValueError("a and m must be coprime.")
    
    # Find the modular multiplicative inverse of a
    mmi = None
    for x in range(1, m):
        if (a * x) % m == 1:
            mmi = x
            break
    
    processed = []
    for c in ciphered_text:
        if c.isalpha():
            # Decode each letter
            y = ord(c.lower()) - ord('a')
            decrypted = (mmi * (y - b)) % m
            processed.append(chr(decrypted + ord('a')))
        elif c.isdigit():
            # Digits are kept as is
            processed.append(c)
        # Spaces and other characters are excluded
    
    return ''.join(processed)
