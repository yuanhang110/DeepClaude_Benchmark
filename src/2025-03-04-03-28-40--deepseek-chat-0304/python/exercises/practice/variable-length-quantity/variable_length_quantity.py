def encode(numbers):
    """Encode a list of numbers into VLQ bytes."""
    result = []
    for number in numbers:
        if number < 0:
            raise ValueError("Only unsigned integers are supported")
        if number > 0xFFFFFFFF:
            raise ValueError("Number too large for 32-bit VLQ")
            
        # Handle zero case
        if number == 0:
            result.append(0)
            continue
            
        # Encode the number
        bytes_ = []
        while number > 0:
            byte = number & 0x7F
            number >>= 7
            if bytes_:
                byte |= 0x80  # Set continuation bit
            bytes_.insert(0, byte)
        result.extend(bytes_)
    return result


def decode(bytes_):
    """Decode VLQ bytes into a list of numbers."""
    if not bytes_:
        return []
        
    numbers = []
    number = 0
    for i, byte in enumerate(bytes_):
        # Check for incomplete sequence
        if i == len(bytes_) - 1 and (byte & 0x80):
            raise ValueError("incomplete sequence")
            
        number = (number << 7) | (byte & 0x7F)
        if not (byte & 0x80):
            numbers.append(number)
            number = 0
    return numbers
