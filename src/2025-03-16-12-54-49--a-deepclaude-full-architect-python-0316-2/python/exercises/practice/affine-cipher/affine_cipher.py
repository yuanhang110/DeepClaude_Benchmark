import math

def encode(plain_text, a, b):
    # Check if a and m are coprime
    m = 26
    if math.gcd(a, m) != 1:
        raise ValueError("a and m must be coprime.")
    
    # Process the plain text - keep only alphanumeric characters and convert to lowercase
    processed = ''.join(char.lower() for char in plain_text if char.isalnum())
    
    # Encrypt each character
    encrypted = []
    for char in processed:
        if char.isalpha():
            # Convert letter to index (0-25)
            i = ord(char) - ord('a')
            # Apply encryption formula: E(x) = (ax + b) mod m
            encrypted_index = (a * i + b) % m
            # Convert back to letter
            encrypted.append(chr(encrypted_index + ord('a')))
        else:
            # Keep digits as is
            encrypted.append(char)
    
    # Group into chunks of 5
    encrypted_text = ''.join(encrypted)
    groups = [encrypted_text[i:i+5] for i in range(0, len(encrypted_text), 5)]
    return ' '.join(groups)


def decode(ciphered_text, a, b):
    # Check if a and m are coprime
    m = 26
    if math.gcd(a, m) != 1:
        raise ValueError("a and m must be coprime.")
    
    # Find modular multiplicative inverse of a
    a_inv = None
    for i in range(1, m):
        if (a * i) % m == 1:
            a_inv = i
            break
    
    # Remove spaces and non-alphanumeric characters
    processed = ''.join(char.lower() for char in ciphered_text if char.isalnum())
    
    # Decrypt each character
    decrypted = []
    for char in processed:
        if char.isalpha():
            # Convert letter to index (0-25)
            y = ord(char) - ord('a')
            # Apply decryption formula: D(y) = a^-1 * (y - b) mod m
            decrypted_index = (a_inv * (y - b)) % m
            # Convert back to letter
            decrypted.append(chr(decrypted_index + ord('a')))
        else:
            # Keep digits as is
            decrypted.append(char)
    
    return ''.join(decrypted)
