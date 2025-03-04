def grep(pattern, flags, files):
    flag_list = flags.split()
    print_line_numbers = '-n' in flag_list
    print_filenames = '-l' in flag_list
    case_insensitive = '-i' in flag_list
    invert_match = '-v' in flag_list
    match_entire_line = '-x' in flag_list

    results = []
    for filename in files:
        with open(filename, 'r') as f:
            file_matched = False
            for line_num, line in enumerate(f, start=1):
                line = line.rstrip('\n')
                match_line = line
                match_pattern = pattern

                if case_insensitive:
                    match_line = match_line.lower()
                    match_pattern = match_pattern.lower()

                matches = False
                if match_entire_line:
                    matches = (match_line == match_pattern)
                else:
                    matches = (match_pattern in match_line)

                if invert_match:
                    matches = not matches

                if matches:
                    if print_filenames:
                        if not file_matched:
                            results.append(filename)
                            file_matched = True
                        continue

                    output = []
                    if len(files) > 1:
                        output.append(f"{filename}:")
                    if print_line_numbers:
                        output.append(f"{line_num}:")
                    output.append(line)
                    results.append(''.join(output))

    return '\n'.join(results) + '\n' if results else ''
