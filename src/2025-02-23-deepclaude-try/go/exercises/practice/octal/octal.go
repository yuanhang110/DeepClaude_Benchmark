package octal

import "fmt"

func ParseOctal(input string) (int64, error) {
	var result int64

	// Handle empty input
	if len(input) == 0 {
		return 0, nil
	}

	// Process each character
	for _, c := range input {
		// Check for invalid digits
		if c < '0' || c > '7' {
			return 0, fmt.Errorf("invalid octal digit")
		}
		// Convert character to digit and accumulate result
		digit := int64(c - '0')
		result = result*8 + digit
	}

	return result, nil
}
