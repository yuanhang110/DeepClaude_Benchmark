def encode(numbers):
    encoded = []
    for num in numbers:
        if num < 0:
            raise ValueError("Negative number not allowed")
        if num > 0xFFFFFFFF:
            raise ValueError("Number exceeds 32-bit unsigned range")
        chunks = []
        if num == 0:
            chunks.append(0)
        else:
            while num > 0:
                chunks.append(num & 0x7F)
                num >>= 7
        chunks.reverse()
        for i in range(len(chunks)):
            if i != len(chunks) - 1:
                chunks[i] |= 0x80
        encoded.extend(chunks)
    return encoded


def decode(bytes_):
    numbers = []
    current = []
    for byte in bytes_:
        current.append(byte)
        if (byte & 0x80) == 0:
            num = 0
            for b in current:
                num = (num << 7) | (b & 0x7F)
                if num > 0xFFFFFFFF:
                    raise ValueError("Number exceeds 32-bit unsigned range")
            numbers.append(num)
            current = []
    if current:
        raise ValueError("incomplete sequence")
    return numbers
