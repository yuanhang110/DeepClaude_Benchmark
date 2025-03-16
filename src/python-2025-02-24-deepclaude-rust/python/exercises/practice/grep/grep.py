def grep(pattern, flags, files):
    output = []
    flags_set = set(flags)
    multiple_files = len(files) > 1

    for file in files:
        try:
            with open(file, 'r') as f:
                lines = f.readlines()
        except IOError:
            continue

        matched_lines = []
        found = False

        for line_num, line in enumerate(lines, 1):
            line_content = line.rstrip('\n')

            current_pattern = pattern
            current_line_content = line_content

            if '-i' in flags_set:
                current_pattern = current_pattern.lower()
                current_line_content = current_line_content.lower()

            if '-x' in flags_set:
                match = (current_line_content == current_pattern)
            else:
                match = (current_pattern in current_line_content)

            if '-v' in flags_set:
                match = not match

            if match:
                if '-l' in flags_set:
                    output.append(file)
                    found = True
                    break

                parts = []
                if multiple_files:
                    parts.append(f"{file}:")
                if '-n' in flags_set:
                    parts.append(f"{line_num}:")
                parts.append(line)
                matched_lines.append(''.join(parts))

        if not found and '-l' not in flags_set:
            output.extend(matched_lines)

    return ''.join(output)
