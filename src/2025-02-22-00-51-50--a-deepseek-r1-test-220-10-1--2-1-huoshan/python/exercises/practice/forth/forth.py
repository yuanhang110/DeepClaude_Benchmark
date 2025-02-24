class StackUnderflowError(Exception):
    """Exception raised when Stack is not full.
       message: explanation of the error.
    """
    def __init__(self, message):
        self.message = message


def evaluate(input_data):
    """Evaluate Forth code and return the stack state."""
    from collections import deque

    custom_words = {}
    stack = []
    
    for line in input_data:
        tokens = line.split()
        if not tokens:
            continue
        
        # Handle word definitions
        if tokens[0] == ":" and tokens[-1] == ";":
            if len(tokens) < 4:
                raise ValueError("invalid word definition")
            name = tokens[1].upper()
            # Check if trying to redefine a number
            try:
                int(name)
                raise ValueError("cannot redefine numbers")
            except ValueError:
                pass  # Name is not a number
            
            # Process definition tokens (convert words to uppercase, leave numbers)
            definition = []
            for token in tokens[2:-1]:
                try:
                    int(token)
                    definition.append(token)
                except ValueError:
                    definition.append(token.upper())
            custom_words[name] = definition
            
        else:
            # Process normal line of code
            processing_queue = deque()
            for token in tokens:
                # Separate numbers and words (convert words to uppercase)
                try:
                    int(token)
                    processing_queue.append(token)
                except ValueError:
                    processing_queue.append(token.upper())
            
            while processing_queue:
                token = processing_queue.popleft()
                
                try:
                    # Try to push number
                    num = int(token)
                    stack.append(num)
                except ValueError:
                    # Handle word/operation
                    word = token
                    if word in custom_words:
                        # Convert definition tokens to appropriate format and prepend
                        def_tokens = []
                        for def_token in custom_words[word]:
                            try:
                                # Check if it's a number (preserve as string)
                                int(def_token)
                                def_tokens.append(def_token)
                            except ValueError:
                                # Convert word to uppercase
                                def_tokens.append(def_token.upper())
                        # Prepend in reverse order to maintain execution order
                        for def_token in reversed(def_tokens):
                            processing_queue.appendleft(def_token)
                    else:
                        # Built-in operations
                        if word == "+":
                            if len(stack) < 2:
                                raise StackUnderflowError("Insufficient number of items in stack")
                            a = stack.pop()
                            b = stack.pop()
                            stack.append(b + a)
                        elif word == "-":
                            if len(stack) < 2:
                                raise StackUnderflowError("Insufficient number of items in stack")
                            a = stack.pop()
                            b = stack.pop()
                            stack.append(b - a)
                        elif word == "*":
                            if len(stack) < 2:
                                raise StackUnderflowError("Insufficient number of items in stack")
                            a = stack.pop()
                            b = stack.pop()
                            stack.append(b * a)
                        elif word == "/":
                            if len(stack) < 2:
                                raise StackUnderflowError("Insufficient number of items in stack")
                            a = stack.pop()
                            b = stack.pop()
                            if a == 0:
                                raise ZeroDivisionError("divide by zero")
                            # Forth uses integer division truncating toward zero
                            stack.append(b // a)
                        elif word == "DUP":
                            if len(stack) < 1:
                                raise StackUnderflowError("Insufficient number of items in stack")
                            stack.append(stack[-1])
                        elif word == "DROP":
                            if len(stack) < 1:
                                raise StackUnderflowError("Insufficient number of items in stack")
                            stack.pop()
                        elif word == "SWAP":
                            if len(stack) < 2:
                                raise StackUnderflowError("Insufficient number of items in stack")
                            a = stack.pop()
                            b = stack.pop()
                            stack.append(a)
                            stack.append(b)
                        elif word == "OVER":
                            if len(stack) < 2:
                                raise StackUnderflowError("Insufficient number of items in stack")
                            stack.append(stack[-2])
                        else:
                            raise ValueError(f"undefined operation {word}")
    
    return stack
