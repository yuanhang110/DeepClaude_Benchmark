import math

def encode(plain_text, a, b):
    if math.gcd(a, 26) != 1:
        raise ValueError("a and m must be coprime.")
    
    processed = []
    for c in plain_text:
        if c.isalpha():
            processed.append(c.lower())
        elif c.isdigit():
            processed.append(c)
    
    encoded = []
    for char in processed:
        if char.isalpha():
            x = ord(char) - ord('a')
            e = (a * x + b) % 26
            encoded.append(chr(e + ord('a')))
        else:
            encoded.append(char)
    
    encoded_str = ''.join(encoded)
    # Split into groups of 5 characters
    groups = [encoded_str[i:i+5] for i in range(0, len(encoded_str), 5)]
    return ' '.join(groups)


def decode(ciphered_text, a, b):
    if math.gcd(a, 26) != 1:
        raise ValueError("a and m must be coprime.")
    
    a_inv = pow(a, -1, 26)
    processed = ciphered_text.replace(' ', '')
    
    decoded = []
    for char in processed:
        if char.isalpha():
            y = ord(char) - ord('a')
            d = (a_inv * (y - b)) % 26
            decoded.append(chr(d + ord('a')))
        elif char.isdigit():
            decoded.append(char)
    
    return ''.join(decoded)
