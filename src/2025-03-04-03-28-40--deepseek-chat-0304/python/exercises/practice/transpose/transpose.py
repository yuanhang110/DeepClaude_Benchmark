def transpose(text):
    """Transpose text, handling rows of different lengths by left-padding with spaces."""
    if not text:
        return ""
    
    # Split into lines and find max length
    lines = text.split('\n')
    max_len = max(len(line) for line in lines)
    
    # Pad lines with spaces on the right to make them equal length
    padded_lines = [line.ljust(max_len) for line in lines]
    
    # Transpose the padded lines
    transposed = []
    for i in range(max_len):
        # Build each column as a new row
        new_row = ''.join(line[i] for line in padded_lines)
        transposed.append(new_row)
    
    # Join with newlines
    result = '\n'.join(transposed)
    return result
