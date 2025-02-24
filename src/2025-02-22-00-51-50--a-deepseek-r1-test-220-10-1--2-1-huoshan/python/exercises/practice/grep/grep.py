def grep(pattern, flags, files):
    line_number = '-n' in flags
    list_files = '-l' in flags
    case_insensitive = '-i' in flags
    invert = '-v' in flags
    entire_line = '-x' in flags

    result = []
    multiple_files = len(files) > 1

    for filename in files:
        with open(filename, 'r') as f:
            lines = f.readlines()

        file_matched = False
        for idx, line in enumerate(lines, 1):
            # Prepare line for matching
            line_content = line.rstrip('\n') if entire_line else line
            if case_insensitive:
                match_line = line_content.lower()
                match_pattern = pattern.lower()
            else:
                match_line = line_content
                match_pattern = pattern

            # Check for match
            if entire_line:
                matched = (match_line == match_pattern)
            else:
                matched = match_pattern in match_line

            if invert:
                matched = not matched

            if matched:
                if list_files:
                    if not file_matched:
                        result.append(f"{filename}\n")
                        file_matched = True
                    break  # No need to check other lines
                else:
                    parts = []
                    if multiple_files:
                        parts.append(f"{filename}:")
                    if line_number:
                        parts.append(f"{idx}:")
                    # Keep original line including newline character
                    parts.append(line)
                    result.append(''.join(parts))

    return ''.join(result)
