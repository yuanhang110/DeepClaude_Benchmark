def grep(pattern, flags, files):
    """Search files for lines matching a pattern with optional flags."""
    results = []
    show_line_numbers = '-n' in flags
    list_files_only = '-l' in flags
    case_insensitive = '-i' in flags
    invert_match = '-v' in flags
    exact_match = '-x' in flags
    
    # Prepare pattern for matching
    search_pattern = pattern.lower() if case_insensitive else pattern
    
    for file_name in files:
        try:
            with open(file_name, 'r') as file:
                for line_number, line in enumerate(file, 1):
                    # Prepare line for matching
                    line_text = line.rstrip('\n')
                    match_text = line_text.lower() if case_insensitive else line_text
                    
                    # Determine if line matches
                    if exact_match:
                        matches = (match_text == search_pattern)
                    else:
                        matches = (search_pattern in match_text)
                        
                    # Handle invert flag
                    if invert_match:
                        matches = not matches
                        
                    if matches:
                        if list_files_only:
                            results.append(file_name)
                            break  # Only need one match per file
                        else:
                            output = []
                            if len(files) > 1:
                                output.append(f"{file_name}:")
                            if show_line_numbers:
                                output.append(f"{line_number}:")
                            output.append(line_text)
                            results.append("".join(output))
        except IOError:
            continue
            
    # Remove duplicate filenames if -l flag is used
    if list_files_only:
        results = list(dict.fromkeys(results))
        
    # Join results with newlines and add final newline if there are any results
    if results:
        return '\n'.join(results) + '\n'
    return ''
