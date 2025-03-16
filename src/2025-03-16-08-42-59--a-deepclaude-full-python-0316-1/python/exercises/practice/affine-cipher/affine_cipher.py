import math

def encode(plain_text, a, b):
    """
    Encrypt text using the affine cipher.
    E(x) = (ax + b) mod m where m is 26 for the English alphabet.
    """
    # Check if a and m=26 are coprime
    if math.gcd(a, 26) != 1:
        raise ValueError("a and m must be coprime.")
    
    result = []
    chunk = []
    
    for char in plain_text:
        if char.isalpha():
            # Convert to lowercase and get index (0-25)
            i = ord(char.lower()) - ord('a')
            # Apply encryption formula
            encrypted_index = (a * i + b) % 26
            # Convert back to character
            encrypted_char = chr(encrypted_index + ord('a'))
            chunk.append(encrypted_char)
        elif char.isdigit():
            # Digits are kept as is
            chunk.append(char)
            
        # If we have 5 characters, add to result and reset chunk
        if len(chunk) == 5:
            result.append(''.join(chunk))
            chunk = []
    
    # Add any remaining characters
    if chunk:
        result.append(''.join(chunk))
    
    return ' '.join(result)


def decode(ciphered_text, a, b):
    """
    Decrypt text using the affine cipher.
    D(y) = a^(-1)(y - b) mod m where m is 26 for the English alphabet.
    """
    # Check if a and m=26 are coprime
    if math.gcd(a, 26) != 1:
        raise ValueError("a and m must be coprime.")
    
    # Find modular multiplicative inverse of a
    a_inv = pow(a, -1, 26)
    
    result = []
    
    for char in ciphered_text:
        if char.isalpha():
            # Convert to lowercase and get index (0-25)
            y = ord(char.lower()) - ord('a')
            # Apply decryption formula
            decrypted_index = (a_inv * (y - b)) % 26
            # Convert back to character
            decrypted_char = chr(decrypted_index + ord('a'))
            result.append(decrypted_char)
        elif char.isdigit():
            # Digits are kept as is
            result.append(char)
        # Spaces and punctuation are ignored
    
    return ''.join(result)
