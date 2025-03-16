def transpose(text):
    lines = text.split('\n')
    max_len = max(len(line) for line in lines) if lines else 0
    transposed = []
    for i in range(max_len):
        row = []
        for line in lines:
            if i < len(line):
                row.append(line[i])
            else:
                row.append(' ')
        transposed_row = ''.join(row).rstrip()
        transposed.append(transposed_row)
    return '\n'.join(transposed)
