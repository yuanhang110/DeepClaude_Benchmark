def grep(pattern, flags, files):
    case_insensitive = '-i' in flags
    invert = '-v' in flags
    full_line = '-x' in flags
    line_numbers = '-n' in flags
    print_filenames = '-l' in flags
    results = []
    multiple_files = len(files) > 1

    for filename in files:
        try:
            with open(filename, 'r') as f:
                file_has_match = False
                for line_number, line in enumerate(f, 1):
                    line_content = line.rstrip('\n')

                    # Determine if line matches
                    if case_insensitive:
                        line_check = line_content.lower()
                        pattern_check = pattern.lower()
                    else:
                        line_check = line_content
                        pattern_check = pattern

                    if full_line:
                        match = (line_check == pattern_check)
                    else:
                        match = (pattern_check in line_check)

                    if invert:
                        match = not match

                    if match:
                        if print_filenames:
                            file_has_match = True
                            break  # No need to check further lines
                        else:
                            parts = []
                            if multiple_files:
                                parts.append(f"{filename}:")
                            if line_numbers:
                                parts.append(f"{line_number}:")
                            parts.append(line_content)
                            results.append(''.join(parts) + '\n')
                # After processing all lines, check if we need to add filename for -l
                if print_filenames and file_has_match:
                    results.append(filename + '\n')
        except IOError:
            pass  # Ignore files that can't be opened

    return ''.join(results)
