def grep(pattern, flags, files):
    """Search files for lines matching a pattern.
    
    Args:
        pattern: The string to search for.
        flags: List of flags to customize behavior.
        files: List of files to search in.
        
    Returns:
        A string containing all matching lines.
    """
    result = []
    case_insensitive = '-i' in flags
    invert_match = '-v' in flags
    exact_match = '-x' in flags
    show_line_numbers = '-n' in flags
    files_with_matches = '-l' in flags
    multiple_files = len(files) > 1
    
    for file_path in files:
        file_has_match = False
        try:
            with open(file_path, 'r') as file:
                for line_num, line in enumerate(file, start=1):
                    line = line.rstrip('\n')
                    
                    # Determine if the line matches
                    if case_insensitive:
                        if exact_match:
                            match = pattern.lower() == line.lower()
                        else:
                            match = pattern.lower() in line.lower()
                    else:
                        if exact_match:
                            match = pattern == line
                        else:
                            match = pattern in line
                    
                    # Invert the match if -v flag is present
                    if invert_match:
                        match = not match
                    
                    if match:
                        file_has_match = True
                        
                        # If -l flag is set, we only need to know that the file has a match
                        if files_with_matches:
                            continue
                        
                        # Construct the output line
                        output_parts = []
                        if multiple_files:
                            output_parts.append(f"{file_path}:")
                        if show_line_numbers:
                            output_parts.append(f"{line_num}:")
                        output_parts.append(line)
                        result.append("".join(output_parts))
                
                # Add file name to result if -l flag is set and file has matches
                if files_with_matches and file_has_match:
                    result.append(file_path)
        except:
            # Silently skip files that can't be opened
            pass
    
    # Join all results with newlines
    if result:
        return "\n".join(result) + "\n"
    return ""
