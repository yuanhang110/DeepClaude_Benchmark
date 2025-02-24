def grep(pattern, flags, files):
    flags_set = set(flags)
    results = []
    pattern = pattern.lower() if '-i' in flags_set else pattern

    for file in files:
        with open(file, 'r') as f:
            lines = f.readlines()

        found = False
        for line_num, line in enumerate(lines, 1):
            stripped_line = line.rstrip('\n')
            current_line = stripped_line.lower() if '-i' in flags_set else stripped_line
            current_pattern = pattern.lower() if '-i' in flags_set else pattern

            if '-x' in flags_set:
                match = (current_line == current_pattern)
            else:
                match = (current_pattern in current_line)

            if '-v' in flags_set:
                match = not match

            if match:
                if '-l' in flags_set:
                    if not found:
                        results.append(f"{file}\n")
                        found = True
                        break  # No need to check other lines in this file
                else:
                    parts = []
                    if len(files) > 1:
                        parts.append(f"{file}:")
                    if '-n' in flags_set:
                        parts.append(f"{line_num}:")
                    parts.append(line)
                    formatted_line = ''.join(parts)
                    results.append(formatted_line)

    return ''.join(results)
