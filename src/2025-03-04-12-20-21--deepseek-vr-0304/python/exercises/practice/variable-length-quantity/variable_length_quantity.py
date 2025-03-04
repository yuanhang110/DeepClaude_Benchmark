def encode(numbers):
    encoded = []
    for number in numbers:
        if number < 0:
            raise ValueError("value out of range")
        chunks = []
        if number == 0:
            chunks.append(0)
        else:
            while number > 0:
                chunks.append(number & 0x7F)
                number >>= 7
            chunks.reverse()
            for i in range(len(chunks) - 1):
                chunks[i] |= 0x80
        encoded.extend(chunks)
    return encoded



def decode(bytes_):
    result = []
    current_value = 0
    in_sequence = False
    for byte in bytes_:
        if byte < 0 or byte > 255:
            raise ValueError("invalid byte value")
        current_value = (current_value << 7) | (byte & 0x7F)
        if byte & 0x80:
            in_sequence = True
        else:
            result.append(current_value)
            current_value = 0
            in_sequence = False
    if in_sequence:
        raise ValueError("incomplete sequence")
    return result
