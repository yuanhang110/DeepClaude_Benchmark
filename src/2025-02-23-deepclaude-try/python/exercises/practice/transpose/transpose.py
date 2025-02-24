def transpose(text):
    # Handle empty string
    if not text:
        return ""
        
    # Split into rows
    rows = text.split('\n')
    
    # Handle single line without newlines
    if len(rows) == 1:
        return '\n'.join(text)
        
    # Get length of each row
    row_lengths = [len(row) for row in rows]
    max_len = max(row_lengths)
    
    # Pre-pad shorter rows with spaces to match max length
    padded_rows = [row.ljust(max_len) for row in rows]
    
    # Build transposed string
    result = []
    for col in range(max_len):
        transposed_col = ''
        for row in padded_rows:
            transposed_col += row[col]
        # Only strip trailing spaces, keep leading spaces
        result.append(transposed_col.rstrip())
    
    return '\n'.join(result)
