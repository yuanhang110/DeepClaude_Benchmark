class StackUnderflowError(Exception):
    """Exception raised when Stack is not full.
       message: explanation of the error.
    """
    def __init__(self, message):
        self.message = message
        super().__init__(message)


def evaluate(input_data):
    stack = []
    definitions = {}
    
    # Process each line of input
    for line in input_data:
        # Skip empty lines
        if not line.strip():
            continue
            
        # Handle word definitions
        if line.strip().startswith(':'):
            parts = line.strip().split()
            if len(parts) < 3 or parts[-1] != ';':
                raise ValueError("Invalid definition format")
            
            word_name = parts[1].lower()
            # Numbers can't be used as word names
            if word_name.isdigit() or (word_name[0] == '-' and word_name[1:].isdigit()):
                raise ValueError("Cannot redefine numbers")
                
            definition = parts[2:-1]
            definitions[word_name] = definition
            continue
        
        # Process the tokens in the line
        tokens = line.strip().split()
        i = 0
        while i < len(tokens):
            token = tokens[i].lower()
            i += 1
            
            # If token is a number, push it onto the stack
            if token.isdigit() or (token[0] == '-' and token[1:].isdigit()):
                stack.append(int(token))
                
            # If token is a defined word, replace it with its definition
            elif token in definitions:
                # Insert the definition tokens at the current position
                tokens = tokens[:i-1] + definitions[token] + tokens[i:]
                i = i - 1  # Reprocess from the first token of the definition
                
            # Handle built-in operations
            elif token == '+':
                if len(stack) < 2:
                    raise StackUnderflowError("Insufficient number of items in stack")
                b = stack.pop()
                a = stack.pop()
                stack.append(a + b)
                
            elif token == '-':
                if len(stack) < 2:
                    raise StackUnderflowError("Insufficient number of items in stack")
                b = stack.pop()
                a = stack.pop()
                stack.append(a - b)
                
            elif token == '*':
                if len(stack) < 2:
                    raise StackUnderflowError("Insufficient number of items in stack")
                b = stack.pop()
                a = stack.pop()
                stack.append(a * b)
                
            elif token == '/':
                if len(stack) < 2:
                    raise StackUnderflowError("Insufficient number of items in stack")
                b = stack.pop()
                if b == 0:
                    raise ZeroDivisionError("divide by zero")
                a = stack.pop()
                stack.append(a // b)  # Integer division
                
            elif token == 'dup':
                if len(stack) < 1:
                    raise StackUnderflowError("Insufficient number of items in stack")
                stack.append(stack[-1])
                
            elif token == 'drop':
                if len(stack) < 1:
                    raise StackUnderflowError("Insufficient number of items in stack")
                stack.pop()
                
            elif token == 'swap':
                if len(stack) < 2:
                    raise StackUnderflowError("Insufficient number of items in stack")
                stack[-1], stack[-2] = stack[-2], stack[-1]
                
            elif token == 'over':
                if len(stack) < 2:
                    raise StackUnderflowError("Insufficient number of items in stack")
                stack.append(stack[-2])
                
            else:
                raise ValueError(f"undefined operation")
    
    return stack
