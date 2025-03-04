def answer(question):
    question = question.strip()
    if not question.startswith("What is ") or not question.endswith("?"):
        raise ValueError("syntax error")
    content = question[8:-1].strip()
    if not content:
        raise ValueError("syntax error")
    
    content = content.replace("multiplied by", "*").replace("divided by", "/")
    tokens = content.split()
    tokens = [t for t in tokens if t]
    
    if not tokens:
        raise ValueError("syntax error")
    
    # Check structure: alternating numbers and operations
    for i in range(len(tokens)):
        if i % 2 == 0:
            # Even index: must be a number
            try:
                int(tokens[i])
            except ValueError:
                raise ValueError("syntax error")
        else:
            # Odd index: must be a valid operation
            op = tokens[i]
            if op not in ('plus', 'minus', '*', '/'):
                # Check if the token is a number, which is invalid here
                try:
                    int(op)
                    # It's a number in an operation position → syntax error
                    raise ValueError("syntax error")
                except ValueError:
                    # Not a number → unknown operation
                    raise ValueError("unknown operation")
    
    # Now evaluate the operations
    result = int(tokens[0])
    operations = {
        "plus": lambda a, b: a + b,
        "minus": lambda a, b: a - b,
        "*": lambda a, b: a * b,
        "/": lambda a, b: a // b
    }
    
    for i in range(1, len(tokens), 2):
        op = tokens[i]
        num = int(tokens[i + 1])
        result = operations[op](result, num)
    
    return result
