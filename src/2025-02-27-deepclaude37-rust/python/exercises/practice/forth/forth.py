class StackUnderflowError(Exception):
    """Exception raised when Stack is not full.
       message: explanation of the error.
    """
    def __init__(self, message):
        self.message = message
        super().__init__(message)


def evaluate(input_data):
    stack = []
    custom_words = {}

    def tokenize(line):
        tokens = []
        for token in line.split():
            tokens.append(token.lower() if not is_number(token) else token)
        return tokens

    def is_number(s):
        try:
            int(s)
            return True
        except ValueError:
            return False

    def process_tokens(tokens, stack, custom_words):
        i = 0
        while i < len(tokens):
            token = tokens[i]
            
            # Handle word definition
            if token == ':':
                if i + 1 >= len(tokens):
                    raise ValueError("Invalid word definition")
                word_name = tokens[i + 1]
                
                # Word names cannot be numbers
                if is_number(word_name):
                    raise ValueError("illegal operation")
                
                # Collect definition until semicolon
                j = i + 2
                while j < len(tokens) and tokens[j] != ';':
                    j += 1
                
                if j >= len(tokens) or tokens[j] != ';':
                    raise ValueError("Missing semicolon")
                
                # Store definition
                definition = tokens[i+2:j]
                
                # Recursively expand custom words in the definition at definition time
                def expand_definition(tokens, words):
                    expanded = []
                    for token in tokens:
                        if token in words:
                            expanded.extend(expand_definition(words[token], words))
                        else:
                            expanded.append(token)
                    return expanded
                
                expanded_def = expand_definition(definition, custom_words)
                custom_words[word_name] = expanded_def
                i = j + 1
            
            # Process regular token
            else:
                if is_number(token):
                    stack.append(int(token))
                elif token in custom_words:
                    # Instead of recursive processing, insert the definition directly
                    definition = custom_words[token]
                    # Process each token in the definition
                    for def_token in definition:
                        if is_number(def_token):
                            stack.append(int(def_token))
                        else:
                            # Handle built-in operations
                            if def_token == '+':
                                if len(stack) < 2:
                                    raise StackUnderflowError("Insufficient number of items in stack")
                                a, b = stack.pop(), stack.pop()
                                stack.append(b + a)
                            elif def_token == '-':
                                if len(stack) < 2:
                                    raise StackUnderflowError("Insufficient number of items in stack")
                                a, b = stack.pop(), stack.pop()
                                stack.append(b - a)
                            elif def_token == '*':
                                if len(stack) < 2:
                                    raise StackUnderflowError("Insufficient number of items in stack")
                                a, b = stack.pop(), stack.pop()
                                stack.append(b * a)
                            elif def_token == '/':
                                if len(stack) < 2:
                                    raise StackUnderflowError("Insufficient number of items in stack")
                                a, b = stack.pop(), stack.pop()
                                if a == 0:
                                    raise ZeroDivisionError("divide by zero")
                                stack.append(b // a)
                            elif def_token == 'dup':
                                if len(stack) < 1:
                                    raise StackUnderflowError("Insufficient number of items in stack")
                                stack.append(stack[-1])
                            elif def_token == 'drop':
                                if len(stack) < 1:
                                    raise StackUnderflowError("Insufficient number of items in stack")
                                stack.pop()
                            elif def_token == 'swap':
                                if len(stack) < 2:
                                    raise StackUnderflowError("Insufficient number of items in stack")
                                a, b = stack.pop(), stack.pop()
                                stack.append(a)
                                stack.append(b)
                            elif def_token == 'over':
                                if len(stack) < 2:
                                    raise StackUnderflowError("Insufficient number of items in stack")
                                stack.append(stack[-2])
                            else:
                                raise ValueError("undefined operation")
                else:
                    # Process built-in operations
                    if token == '+':
                        if len(stack) < 2:
                            raise StackUnderflowError("Insufficient number of items in stack")
                        a, b = stack.pop(), stack.pop()
                        stack.append(b + a)
                    elif token == '-':
                        if len(stack) < 2:
                            raise StackUnderflowError("Insufficient number of items in stack")
                        a, b = stack.pop(), stack.pop()
                        stack.append(b - a)
                    elif token == '*':
                        if len(stack) < 2:
                            raise StackUnderflowError("Insufficient number of items in stack")
                        a, b = stack.pop(), stack.pop()
                        stack.append(b * a)
                    elif token == '/':
                        if len(stack) < 2:
                            raise StackUnderflowError("Insufficient number of items in stack")
                        a, b = stack.pop(), stack.pop()
                        if a == 0:
                            raise ZeroDivisionError("divide by zero")
                        stack.append(b // a)
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
                        a, b = stack.pop(), stack.pop()
                        stack.append(a)
                        stack.append(b)
                    elif token == 'over':
                        if len(stack) < 2:
                            raise StackUnderflowError("Insufficient number of items in stack")
                        stack.append(stack[-2])
                    else:
                        raise ValueError("undefined operation")
                i += 1

    # Process each line in the input
    for line in input_data:
        if line.strip():  # Skip empty lines
            tokens = tokenize(line)
            process_tokens(tokens, stack, custom_words)

    return stack
