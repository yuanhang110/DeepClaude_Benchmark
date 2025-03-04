import math

def encode(plain_text, a, b):
    if math.gcd(a, 26) != 1:
        raise ValueError("a and m must be coprime.")
    filtered = []
    for c in plain_text:
        if c.isalnum():
            filtered.append(c.lower() if c.isalpha() else c)
    encrypted = []
    for char in filtered:
        if char.isalpha():
            i = ord(char) - ord('a')
            encrypted_char = chr((a * i + b) % 26 + ord('a'))
            encrypted.append(encrypted_char)
        else:
            encrypted.append(char)
    encrypted_str = ''.join(encrypted)
    groups = [encrypted_str[i:i+5] for i in range(0, len(encrypted_str), 5)]
    return ' '.join(groups)

def decode(ciphered_text, a, b):
    if math.gcd(a, 26) != 1:
        raise ValueError("a and m must be coprime.")
    a_inv = pow(a, -1, 26)
    ciphered = ciphered_text.replace(' ', '')
    decrypted = []
    for char in ciphered:
        if char.isalpha():
            y = ord(char) - ord('a')
            decrypted_char = chr((a_inv * (y - b)) % 26 + ord('a'))
            decrypted.append(decrypted_char)
        else:
            decrypted.append(char)
    return ''.join(decrypted)
