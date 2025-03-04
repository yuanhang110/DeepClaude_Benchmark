def _gcd(a, b):
    """Calculate the greatest common divisor of a and b"""
    while b:
        a, b = b, a % b
    return a

def _mmi(a, m):
    """Find the modular multiplicative inverse of a mod m"""
    if _gcd(a, m) != 1:
        return None
    for x in range(1, m):
        if (a * x) % m == 1:
            return x
    return None

def _prepare_text(text):
    """Normalize text - keep only letters and digits, convert to lowercase"""
    return ''.join(c.lower() for c in text if c.isalnum())

def _transform_char(c, a, b, mode):
    """Transform a single character using affine cipher"""
    if c.isdigit():
        return c
    if c.isalpha():
        m = 26
        i = ord(c) - ord('a')
        if mode == 'encode':
            result = (a * i + b) % m
        else:  # decode
            mmi = _mmi(a, m)
            if mmi is None:
                raise ValueError("a and m must be coprime.")
            result = mmi * (i - b) % m
        return chr(result + ord('a'))
    return ''

def _group_text(text, size=5):
    """Group text into chunks of specified size separated by spaces"""
    return ' '.join(text[i:i+size] for i in range(0, len(text), size))

def encode(plain_text, a, b):
    """Encrypt plaintext using affine cipher"""
    if _gcd(a, 26) != 1:
        raise ValueError("a and m must be coprime.")
    prepared_text = _prepare_text(plain_text)
    cipher_text = ''.join(_transform_char(c, a, b, 'encode') for c in prepared_text)
    return _group_text(cipher_text)

def decode(ciphered_text, a, b):
    """Decrypt ciphertext using affine cipher"""
    if _gcd(a, 26) != 1:
        raise ValueError("a and m must be coprime.")
    prepared_text = _prepare_text(ciphered_text)
    plain_text = ''.join(_transform_char(c, a, b, 'decode') for c in prepared_text)
    return plain_text
