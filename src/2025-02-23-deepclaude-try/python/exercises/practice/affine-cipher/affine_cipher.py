import math

def _is_coprime(a):
    return math.gcd(a, 26) == 1

def _mod_inverse(a):
    for x in range(1, 26):
        if (a * x) % 26 == 1:
            return x
    raise ValueError("No inverse exists for a mod 26")

def encode(plain_text, a, b):
    if not _is_coprime(a):
        raise ValueError("a and m must be coprime.")
    processed = [c.lower() for c in plain_text if c.isalnum()]
    ciphered = []
    for c in processed:
        if c.isdigit():
            ciphered.append(c)
        else:
            x = ord(c) - ord('a')
            encrypted_x = (a * x + b) % 26
            encrypted_c = chr(encrypted_x + ord('a'))
            ciphered.append(encrypted_c)
    ciphered_str = ''.join(ciphered)
    grouped = ' '.join([ciphered_str[i:i+5] for i in range(0, len(ciphered_str), 5)])
    return grouped

def decode(ciphered_text, a, b):
    if not _is_coprime(a):
        raise ValueError("a and m must be coprime.")
    a_inv = _mod_inverse(a)
    processed = [c.lower() for c in ciphered_text if c.isalnum()]
    decrypted = []
    for c in processed:
        if c.isdigit():
            decrypted.append(c)
        else:
            y = ord(c) - ord('a')
            decrypted_x = (a_inv * (y - b)) % 26
            decrypted_c = chr(decrypted_x + ord('a'))
            decrypted.append(decrypted_c)
    return ''.join(decrypted)
