package octal

import "fmt"

func ParseOctal(input string) (int64, error) {
	// Convert octal string to decimal
	var result int64
	valid := true

	if len(input) == 0 {
		valid = false
	} else {
		for _, ch := range input {
			// Check if digit is valid octal (0-7)
			if ch < '0' || ch > '7' {
				valid = false
				break
			}
			// Calculate: result = result * 8 + digit_value
			result = result*8 + int64(ch-'0')
		}
	}

	// Handle validation result
	if !valid {
		return 0, fmt.Errorf("invalid octal %q", input)
	}

	return result, nil
}
