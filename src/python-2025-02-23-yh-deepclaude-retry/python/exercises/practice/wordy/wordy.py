def answer(question):
    if not question.startswith("What is ") or not question.endswith("?"):
        raise ValueError("syntax error")
    expr = question[8:-1].strip()
    if not expr:
        raise ValueError("syntax error")
    
    words = expr.split()
    tokens = []
    i = 0
    while i < len(words):
        current_word = words[i]
        if current_word in ('multiplied', 'divided'):
            if i + 1 >= len(words) or words[i+1] != 'by':
                raise ValueError("syntax error")
            tokens.append('*' if current_word == 'multiplied' else '/')
            i += 2
        elif current_word == 'plus':
            tokens.append('+')
            i += 1
        elif current_word == 'minus':
            tokens.append('-')
            i += 1
        else:
            try:
                tokens.append(int(current_word))
                i += 1
            except ValueError:
                if current_word == 'by':
                    raise ValueError("syntax error")
                raise ValueError("unknown operation")
    
    if not tokens or len(tokens) % 2 == 0 or not isinstance(tokens[0], int):
        raise ValueError("syntax error")
    for idx in range(1, len(tokens), 2):
        if tokens[idx] not in '+-*/' or (idx + 1 >= len(tokens)) or not isinstance(tokens[idx+1], int):
            raise ValueError("syntax error")
    
    result = tokens[0]
    for idx in range(1, len(tokens), 2):
        op = tokens[idx]
        num = tokens[idx+1]
        if op == '+':
            result += num
        elif op == '-':
            result -= num
        elif op == '*':
            result *= num
        elif op == '/':
            result = result // num
    
    return result
