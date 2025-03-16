class StackUnderflowError(Exception):
    """Exception raised when stack has insufficient values."""
    def __init__(self, message="Insufficient number of items in stack"):
        self.message = message
        super().__init__(self.message)


def evaluate(input_data):
    """Evaluate Forth code and return the stack."""
    stack = []
    custom_words = {}
    
    def is_number(token):
        """Check if token is a number."""
        try:
            int(token)
            return True
        except ValueError:
            return False
            
    def apply_operation(op, stack):
        """Apply arithmetic operation to top two stack values."""
        if len(stack) < 2:
            raise StackUnderflowError()
        b = stack.pop()
        a = stack.pop()
        if op == '+':
            stack.append(a + b)
        elif op == '-':
            stack.append(a - b)
        elif op == '*':
            stack.append(a * b)
        elif op == '/':
            if b == 0:
                raise ZeroDivisionError("divide by zero")
            stack.append(a // b)  # Integer division
            
    def handle_builtin(word, stack):
        """Handle built-in stack operations."""
        word = word.upper()
        if word == 'DUP':
            if not stack:
                raise StackUnderflowError()
            stack.append(stack[-1])
        elif word == 'DROP':
            if not stack:
                raise StackUnderflowError()
            stack.pop()
        elif word == 'SWAP':
            if len(stack) < 2:
                raise StackUnderflowError()
            stack[-1], stack[-2] = stack[-2], stack[-1]
        elif word == 'OVER':
            if len(stack) < 2:
                raise StackUnderflowError()
            stack.append(stack[-2])
    
    def process_tokens(tokens, in_definition=False):
        """Process a list of tokens."""
        i = 0
        while i < len(tokens):
            token = tokens[i].lower()
            
            # Handle word definition
            if token == ':':
                if i + 1 >= len(tokens):
                    raise ValueError("invalid word definition")
                word_name = tokens[i + 1].lower()
                if is_number(word_name):
                    raise ValueError("illegal operation")
                
                # Find the closing semicolon
                definition_end = i + 2
                while definition_end < len(tokens) and tokens[definition_end] != ';':
                    definition_end += 1
                if definition_end >= len(tokens):
                    raise ValueError("invalid word definition")
                    
                # Store the definition
                definition = tokens[i + 2:definition_end]
                custom_words[word_name] = definition
                i = definition_end + 1
                continue
                
            # Handle numbers
            if is_number(token):
                stack.append(int(token))
            # Handle arithmetic operators
            elif token in {'+', '-', '*', '/'}:
                apply_operation(token, stack)
            # Handle built-in words
            elif token.upper() in {'DUP', 'DROP', 'SWAP', 'OVER'}:
                handle_builtin(token, stack)
            # Handle custom words
            elif token in custom_words:
                process_tokens(custom_words[token])
            else:
                raise ValueError("undefined operation")
            
            i += 1
            
    # Process each line of input
    for line in input_data:
        tokens = line.split()
        process_tokens(tokens)
        
    return stack
