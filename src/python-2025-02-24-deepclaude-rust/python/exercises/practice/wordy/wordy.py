def answer(question):
    if not question.startswith("What is "):
        raise ValueError("unknown operation")
    if not question.endswith("?"):
        raise ValueError("syntax error")
    content = question[8:-1].strip()
    if not content:
        raise ValueError("syntax error")
    tokens = content.split()
    if not tokens:
        raise ValueError("syntax error")
    try:
        result = int(tokens[0])
    except ValueError:
        raise ValueError("syntax error")
    i = 1
    while i < len(tokens):
        if tokens[i] in ('plus', 'minus'):
            op = tokens[i]
            op_len = 1
        elif tokens[i] in ('multiplied', 'divided'):
            if i + 1 >= len(tokens) or tokens[i+1] != 'by':
                raise ValueError("unknown operation")
            op = tokens[i]
            op_len = 2
        else:
            try:
                int(tokens[i])
                raise ValueError("syntax error")
            except ValueError:
                raise ValueError("unknown operation")
        if i + op_len >= len(tokens):
            raise ValueError("syntax error")
        operand_str = tokens[i + op_len]
        try:
            operand = int(operand_str)
        except ValueError:
            raise ValueError("syntax error")
        if op == 'plus':
            result += operand
        elif op == 'minus':
            result -= operand
        elif op == 'multiplied':
            result *= operand
        elif op == 'divided':
            result //= operand
        else:
            raise ValueError("unknown operation")
        i += op_len + 1
    if i != len(tokens):
        raise ValueError("syntax error")
    return result
