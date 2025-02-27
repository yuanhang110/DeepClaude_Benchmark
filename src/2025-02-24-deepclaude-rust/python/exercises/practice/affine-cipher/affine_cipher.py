def encode(plain_text, a, b):
    import math
    if math.gcd(a, 26) != 1:
        raise ValueError("a and m must be coprime.")
    
    # Process the text: lowercase and filter out non-alphanumeric
    processed = [char for char in plain_text.lower() if char.isalpha() or char.isdigit()]
    
    # Encrypt each character
    encrypted = []
    for char in processed:
        if char.isalpha():
            # Convert letter to number (0-25), apply formula, convert back to letter
            x = ord(char) - ord('a')
            e = (a * x + b) % 26
            encrypted.append(chr(e + ord('a')))
        else:
            # Keep digits as is
            encrypted.append(char)
    
    # Group into chunks of 5
    result = ""
    for i in range(0, len(encrypted), 5):
        if i > 0:
            result += " "
        result += "".join(encrypted[i:i+5])
    
    return result


def decode(ciphered_text, a, b):
    import math
    if math.gcd(a, 26) != 1:
        raise ValueError("a and m must be coprime.")
    
    # Find modular multiplicative inverse of a mod 26
    a_inv = 0
    for i in range(1, 26):
        if (a * i) % 26 == 1:
            a_inv = i
            break
    
    # Remove spaces from ciphered text
    processed = ciphered_text.replace(" ", "")
    
    # Decrypt each character
    decrypted = []
    for char in processed:
        if char.isalpha():
            # Convert letter to number, apply inverse formula, convert back to letter
            y = ord(char) - ord('a')
            d = (a_inv * (y - b)) % 26
            decrypted.append(chr(d + ord('a')))
        else:
            # Keep digits as is
            decrypted.append(char)
    
    return "".join(decrypted)
