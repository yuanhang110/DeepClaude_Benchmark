def transpose(text):
    # Handle empty string case
    if not text:
        return ""
        
    # Split the input text into lines
    lines = text.split('\n')
    
    # Find the maximum line length
    max_length = max(len(line) for line in lines)
    
    # Initialize result
    result = []
    
    # Transpose the matrix
    for i in range(max_length):
        transposed_line = ""
        for j in range(len(lines)):
            # If the current position exists in the line, add the character
            # Otherwise add a space (padding to the left)
            if i < len(lines[j]):
                transposed_line += lines[j][i]
            else:
                transposed_line += " "
                
        # Remove trailing spaces (don't pad to the right)
        # But preserve spaces that are part of the original matrix
        result.append(transposed_line.rstrip())
    
    # Join the transposed lines with newlines
    return "\n".join(result)
