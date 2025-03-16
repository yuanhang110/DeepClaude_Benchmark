def encode(numbers):
    """
    Encode a list of integers using Variable Length Quantity encoding.
    
    Each byte uses 7 bits for data and 1 bit (MSB) to indicate if more bytes follow.
    """
    result = []
    for number in numbers:
        # Special case for zero
        if number == 0:
            result.append(0)
            continue
            
        # Break the number into 7-bit chunks
        chunks = []
        while number > 0:
            chunks.insert(0, number & 0x7F)
            number >>= 7
            
        # Set continuation bit (bit 7) on all but the last byte
        for i in range(len(chunks) - 1):
            chunks[i] |= 0x80
            
        result.extend(chunks)
    
    return result


def decode(bytes_):
    """
    Decode a Variable Length Quantity encoded sequence of bytes.
    
    Raises ValueError if the sequence is incomplete.
    """
    result = []
    current = 0
    
    for i, byte in enumerate(bytes_):
        # Add the 7 bits of data to our accumulator
        current = (current << 7) | (byte & 0x7F)
        
        # If MSB is clear, we've reached the end of a number
        if not (byte & 0x80):
            result.append(current)
            current = 0
        # Check if we're at the end of the bytes with an incomplete sequence
        elif i == len(bytes_) - 1:
            raise ValueError("incomplete sequence")
            
    # If we've processed all bytes but still have bits in current,
    # the sequence was incomplete
    if current != 0:
        raise ValueError("incomplete sequence")
            
    return result
