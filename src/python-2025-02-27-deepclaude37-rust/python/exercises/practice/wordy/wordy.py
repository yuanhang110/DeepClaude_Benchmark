def answer(question):
    # Check if it ends with "?"
    if not question.endswith("?"):
        raise ValueError("syntax error")
        
    # If it's not a math question (doesn't start with "What is")
    if not question.startswith("What is "):
        raise ValueError("unknown operation")
        
    content = question[8:-1].strip()
    
    # Empty content is invalid
    if not content:
        raise ValueError("syntax error")
    
    # Tokenize the content into numbers and operations
    words = content.split()
    tokens = []
    i = 0
    while i < len(words):
        # Check for multi-word operations first
        if i < len(words) - 1 and words[i] in ('multiplied', 'divided') and words[i+1] == 'by':
            tokens.append(f"{words[i]} by")
            i += 2
        elif words[i] in ('plus', 'minus'):
            tokens.append(words[i])
            i += 1
        else:
            # Try to parse as number
            try:
                int(words[i])  # Check if it's a valid integer
                tokens.append(words[i])
                i += 1
            except ValueError:
                # Check if it's an unknown operation
                if any(c.isalpha() for c in words[i]):
                    raise ValueError("unknown operation")
                else:
                    raise ValueError("syntax error")
    
    # Validate token structure
    if not tokens:
        raise ValueError("syntax error")
    
    try:
        result = int(tokens[0])
    except ValueError:
        raise ValueError("syntax error")
    
    # Process operations
    i = 1
    while i < len(tokens):
        if i + 1 >= len(tokens):
            raise ValueError("syntax error")
        
        op = tokens[i]
        next_num = tokens[i+1]
        
        if op not in ('plus', 'minus', 'multiplied by', 'divided by'):
            # Check if it's a number in operation position (syntax error)
            try:
                int(op)
                raise ValueError("syntax error")
            except ValueError:
                # If it contains letters, it's an unknown operation
                if any(c.isalpha() for c in op):
                    raise ValueError("unknown operation")
                else:
                    raise ValueError("syntax error")
        
        try:
            num = int(next_num)
        except ValueError:
            raise ValueError("syntax error")
        
        if op == 'plus':
            result += num
        elif op == 'minus':
            result -= num
        elif op == 'multiplied by':
            result *= num
        elif op == 'divided by':
            result //= num  # Integer division as per examples
        
        i += 2
    
    # Check if there were any unexpected tokens
    if i != len(tokens):
        raise ValueError("syntax error")
    
    return result
