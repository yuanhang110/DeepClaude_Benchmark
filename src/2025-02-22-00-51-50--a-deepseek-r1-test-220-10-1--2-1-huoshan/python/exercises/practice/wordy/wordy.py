import re

def answer(question):
    # Validate question structure and extract expression
    question = question.strip()
    if not question.startswith('What is ') or not question.endswith('?'):
        raise ValueError("syntax error")
    
    expr = question[8:-1].strip()
    if not expr:
        raise ValueError("syntax error")
    
    # Normalize operations to single words
    expr = expr.replace('multiplied by', 'multiply').replace('divided by', 'divide')
    tokens = expr.split()
    
    # Validate we have at least a number
    if not tokens:
        raise ValueError("syntax error")
    
    # Parse first number
    try:
        result = int(tokens[0])
    except ValueError:
        raise ValueError("syntax error")
    
    # Process subsequent operations
    i = 1
    while i < len(tokens):
        # Check for operation-number pairs
        if i + 1 >= len(tokens):
            raise ValueError("syntax error")
        
        op = tokens[i]
        number_str = tokens[i+1]
        
        # Validate operation (check if it's a number first)
        try:
            int(op)
            raise ValueError("syntax error")
        except ValueError:
            if op not in ('plus', 'minus', 'multiply', 'divide'):
                raise ValueError("unknown operation")
        
        # Parse number
        try:
            num = int(number_str)
        except ValueError:
            raise ValueError("syntax error")
        
        # Perform calculation
        if op == 'plus':
            result += num
        elif op == 'minus':
            result -= num
        elif op == 'multiply':
            result *= num
        elif op == 'divide':
            if num == 0:
                raise ValueError("syntax error")
            result //= num
        
        i += 2
    
    return result
