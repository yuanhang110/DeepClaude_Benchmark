def answer(question):
    # Check if the question has the correct format
    if not question.startswith("What is ") or not question.endswith("?"):
        raise ValueError("syntax error")
    
    # Extract the math expression part
    expression = question[8:-1].strip()
    if not expression:
        raise ValueError("syntax error")
    
    # Tokenize the expression
    tokens = []
    parts = expression.split()
    i = 0
    while i < len(parts):
        if i < len(parts) - 1 and parts[i] == "multiplied" and parts[i + 1] == "by":
            tokens.append("multiplied by")
            i += 2
        elif i < len(parts) - 1 and parts[i] == "divided" and parts[i + 1] == "by":
            tokens.append("divided by")
            i += 2
        else:
            tokens.append(parts[i])
            i += 1
    
    # Process the tokens
    equation = []
    for token in tokens:
        if token in ["plus", "minus", "multiplied by", "divided by"]:
            equation.append(token)
        else:
            try:
                equation.append(int(token))
            except ValueError:
                raise ValueError("syntax error")
    
    # Validate equation structure
    if not equation:
        raise ValueError("syntax error")
    
    if not isinstance(equation[0], int):
        raise ValueError("syntax error")
    
    # Ensure alternating number-operation pattern
    for i in range(len(equation)):
        if i % 2 == 0 and not isinstance(equation[i], int):
            raise ValueError("syntax error")
        if i % 2 == 1 and isinstance(equation[i], int):
            raise ValueError("syntax error")
    
    # Evaluate the expression from left to right
    result = equation[0]
    i = 1
    
    while i < len(equation):
        if i + 1 >= len(equation):
            raise ValueError("syntax error")
            
        operation = equation[i]
        operand = equation[i + 1]
        
        if operation == "plus":
            result += operand
        elif operation == "minus":
            result -= operand
        elif operation == "multiplied by":
            result *= operand
        elif operation == "divided by":
            result //= operand
        else:
            raise ValueError("unknown operation")
        
        i += 2
    
    return result
