def answer(question):
    # Check if it's a valid math question
    if not question.startswith("What is ") or not question.endswith("?"):
        raise ValueError("syntax error")
    
    # Extract the math expression
    expression = question[8:-1].strip()
    if not expression:
        raise ValueError("syntax error")
    
    # Handle the case of just a number
    if expression.isdigit() or (expression.startswith('-') and expression[1:].isdigit()):
        return int(expression)
    
    # Tokenize the expression
    tokens = []
    current_token = ""
    for char in expression + " ":  # Add space at end to process the last token
        if char.isdigit() or char == '-' and not current_token:
            current_token += char
        elif char.isalpha() or char == ' ':
            current_token += char
            if char == ' ' and current_token.strip():
                tokens.append(current_token.strip())
                current_token = ""
        else:
            raise ValueError("syntax error")
    
    # Process the tokens
    if not tokens:
        raise ValueError("syntax error")
    
    # Convert words to operations
    i = 0
    equation = []
    
    try:
        # First token should be a number
        equation.append(int(tokens[0]))
        i += 1
        
        while i < len(tokens):
            # Process operation
            operation = tokens[i]
            
            if operation == "plus":
                op = "+"
            elif operation == "minus":
                op = "-"
            elif operation == "multiplied" and i + 1 < len(tokens) and tokens[i + 1] == "by":
                op = "*"
                i += 1  # Skip "by"
            elif operation == "divided" and i + 1 < len(tokens) and tokens[i + 1] == "by":
                op = "/"
                i += 1  # Skip "by"
            else:
                raise ValueError("unknown operation")
            
            equation.append(op)
            i += 1
            
            # Next token should be a number
            if i >= len(tokens):
                raise ValueError("syntax error")
            
            try:
                equation.append(int(tokens[i]))
            except ValueError:
                raise ValueError("syntax error")
            
            i += 1
    except ValueError as e:
        if str(e) == "unknown operation":
            raise
        raise ValueError("syntax error")
    
    # Validate equation structure
    if len(equation) < 3 or len(equation) % 2 == 0:
        raise ValueError("syntax error")
    
    # Evaluate the equation from left to right
    result = equation[0]
    i = 1
    
    while i < len(equation):
        operation = equation[i]
        operand = equation[i + 1]
        
        if operation == "+":
            result += operand
        elif operation == "-":
            result -= operand
        elif operation == "*":
            result *= operand
        elif operation == "/":
            result //= operand  # Integer division
        
        i += 2
    
    return result
