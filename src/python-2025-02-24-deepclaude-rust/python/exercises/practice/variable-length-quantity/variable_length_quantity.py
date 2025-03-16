def encode(numbers):
    encoded = []
    for num in numbers:
        chunks = []
        if num == 0:
            chunks.append(0)
        else:
            while num > 0:
                chunks.append(num & 0x7F)
                num = num >> 7
        chunks.reverse()
        for i in range(len(chunks)):
            if i < len(chunks) - 1:
                chunks[i] |= 0x80
        encoded.extend(chunks)
    return encoded


def decode(bytes_):
    numbers = []
    current_number = 0
    for byte in bytes_:
        current_number = (current_number << 7) | (byte & 0x7F)
        if not (byte & 0x80):
            numbers.append(current_number)
            current_number = 0
    if current_number != 0:
        raise ValueError("incomplete sequence")
    return numbers
