def answer(question):
    if not question.startswith("What is ") or not question.endswith("?"):
        raise ValueError("syntax error")
    
    # Remove "What is " prefix and "?" suffix
    expr = question[8:-1].strip()
    
    # Handle simple number case
    if expr.lstrip('-').isdigit():
        return int(expr)
    
    # Split into tokens
    tokens = expr.split()
    
    # Initialize with first number
    if not tokens[0].lstrip('-').isdigit():
        if any(word.isdigit() for word in tokens):
            raise ValueError("syntax error")
        raise ValueError("unknown operation")
    result = int(tokens[0])
    tokens = tokens[1:]
    
    while tokens:
        # Expect operation then number
        if len(tokens) < 2:
            raise ValueError("syntax error")
        
        op = tokens[0]
        num = tokens[1]
        
        # Handle multi-word operations
        if op == "multiplied" or op == "divided":
            if len(tokens) < 3 or tokens[1] != "by":
                raise ValueError("syntax error")
            num = tokens[2]
            tokens = tokens[1:]  # consume "by"
        
        if not num.lstrip('-').isdigit():
            raise ValueError("syntax error")
        
        num = int(num)
        
        if op == "plus":
            result += num
        elif op == "minus":
            result -= num
        elif op == "multiplied":
            result *= num
        elif op == "divided":
            result //= num
        else:
            raise ValueError("unknown operation")
        
        tokens = tokens[2:]
    
    return result
