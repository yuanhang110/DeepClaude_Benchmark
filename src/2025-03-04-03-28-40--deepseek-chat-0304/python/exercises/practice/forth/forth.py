class StackUnderflowError(Exception):
    """Exception raised when Stack is not full."""
    def __init__(self, message):
        self.message = message


def evaluate(input_data):
    stack = []
    words = {}
    MAX_RECURSION = 1000
    recursion_depth = 0
    
    def pop(n=1):
        if len(stack) < n:
            raise StackUnderflowError("Insufficient number of items in stack")
        return stack.pop() if n == 1 else [stack.pop() for _ in range(n)]
    
    def push(value):
        stack.append(value)
    
    for line in input_data:
        tokens = line.upper().split()
        i = 0
        while i < len(tokens):
            token = tokens[i]
            if token.isdigit() or (token[0] == '-' and token[1:].isdigit()):
                push(int(token))
            elif token == ':':
                # Word definition
                i += 1
                if i >= len(tokens) or tokens[i].isdigit():
                    raise ValueError("Invalid word definition")
                word_name = tokens[i]
                i += 1
                definition = []
                while i < len(tokens) and tokens[i] != ';':
                    definition.append(tokens[i])
                    i += 1
                if i >= len(tokens):
                    raise ValueError("Word definition not terminated")
                words[word_name] = definition
            elif token in words:
                # Expand user-defined word with recursion limit
                recursion_depth += 1
                if recursion_depth > MAX_RECURSION:
                    raise ValueError("maximum recursion depth exceeded")
                if tokens[i:i+1] == words[token]:
                    raise ValueError("recursive word definition")
                tokens[i:i+1] = words[token]
                i -= 1
            else:
                # Built-in operations
                if token == '+':
                    a, b = pop(2)
                    push(b + a)
                elif token == '-':
                    a, b = pop(2)
                    push(b - a)
                elif token == '*':
                    a, b = pop(2)
                    push(b * a)
                elif token == '/':
                    a, b = pop(2)
                    if a == 0:
                        raise ZeroDivisionError("divide by zero")
                    push(b // a)
                elif token == 'DUP':
                    a = pop()
                    push(a)
                    push(a)
                elif token == 'DROP':
                    pop()
                elif token == 'SWAP':
                    a, b = pop(2)
                    push(a)
                    push(b)
                elif token == 'OVER':
                    a, b = pop(2)
                    push(b)
                    push(a)
                    push(b)
                else:
                    raise ValueError("undefined operation")
            i += 1
    
    return stack
