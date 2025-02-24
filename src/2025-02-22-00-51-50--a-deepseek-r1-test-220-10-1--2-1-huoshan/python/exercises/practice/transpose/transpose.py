def transpose(text):
    lines = text.split('\n')
    if not lines:
        return ''
    max_length = max(len(line) for line in lines)
    transposed = []
    for i in range(max_length):
        row = []
        for line in lines:
            if i < len(line):
                row.append(line[i])
            else:
                row.append(' ')
        transposed_row = ''.join(row)
        transposed.append(transposed_row)
    return '\n'.join(transposed)
