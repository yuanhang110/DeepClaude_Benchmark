def grep(pattern, flags, files):
    """
    Search files for lines matching a pattern and return all matching lines.
    
    Args:
        pattern (str): The string to search for
        flags (list): List of flags to modify search behavior
        files (list): List of file paths to search in
    
    Returns:
        str: All matching lines concatenated into a single string
    """
    result = []
    
    # Process each file
    for file_path in files:
        try:
            with open(file_path, 'r') as file:
                lines = file.readlines()
                
            found_match_in_file = False
            
            # Process each line in the file
            for line_num, line in enumerate(lines, 1):
                # Remove trailing newline for comparison
                line_content = line.rstrip('\n')
                
                # Handle case-insensitive flag
                if '-i' in flags:
                    matches = pattern.lower() in line_content.lower()
                else:
                    matches = pattern in line_content
                
                # Handle exact match flag
                if '-x' in flags:
                    if '-i' in flags:
                        matches = pattern.lower() == line_content.lower()
                    else:
                        matches = pattern == line_content
                
                # Handle invert match flag
                if '-v' in flags:
                    matches = not matches
                
                if matches:
                    found_match_in_file = True
                    
                    # Handle list files only flag
                    if '-l' in flags:
                        result.append(f"{file_path}\n")
                        break
                    
                    # Construct the output line
                    output_parts = []
                    
                    # Add filename if multiple files
                    if len(files) > 1:
                        output_parts.append(f"{file_path}:")
                    
                    # Add line number if requested
                    if '-n' in flags:
                        output_parts.append(f"{line_num}:")
                    
                    # Add the actual line content
                    output_parts.append(line)
                    
                    result.append(''.join(output_parts))
            
            # If we're listing files and didn't find a match, we don't add anything
            
        except FileNotFoundError:
            result.append(f"grep: {file_path}: No such file or directory\n")
    
    return ''.join(result)
