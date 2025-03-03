def transpose(text):
    if not text:
        return ""
    
    # Split the input text into lines
    lines = text.split('\n')
    
    # Find the maximum line length
    max_len = max(len(line) for line in lines) if lines else 0
    
    result = []
    # For each column in the original matrix
    for col_idx in range(max_len):
        new_row = []
        # For each row in the original matrix
        for row_idx, line in enumerate(lines):
            # If this position exists in the original row, use it
            if col_idx < len(line):
                new_row.append(line[col_idx])
            # Otherwise, add a space (padding to the left)
            # But only if this line's length is STRICTLY less than the column index
            # This handles the special case where a line ends exactly at this column
            elif any(len(r) > col_idx for r in lines) and len(line) < col_idx:
                new_row.append(' ')
        
        # Join the characters without stripping spaces (which could be part of the input)
        result.append(''.join(new_row))
    
    return '\n'.join(result)
