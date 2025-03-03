package trinary

import "errors"

// ParseTrinary converts a trinary string to its decimal equivalent
// If the input string is invalid (contains characters other than 0, 1, or 2),
// the function returns 0 and an error
func ParseTrinary(arg string) (int64, error) {
	var result int64
	
	// Check if the string is valid
	for _, c := range arg {
		if c < '0' || c > '2' {
			return 0, errors.New("invalid trinary digit")
		}
	}
	
	// Convert to decimal
	for i := 0; i < len(arg); i++ {
		digit := int64(arg[i] - '0')
		position := len(arg) - 1 - i
		
		// Calculate 3^position
		power := int64(1)
		for j := 0; j < position; j++ {
			power *= 3
		}
		
		result += digit * power
	}
	
	return result, nil
}
