package hexadecimal

import (
	"errors"
	"strings"
)

// HandleErrors checks if all inputs are invalid hexadecimal strings
func HandleErrors(inputs []string) bool {
	for _, input := range inputs {
		_, err := ParseHex(input)
		if err == nil {
			return false
		}
	}
	return true
}

func ParseHex(in string) (int64, error) {
	var result int64
	
	// Clean input
	in = strings.TrimSpace(strings.ToLower(in))
	
	// Handle empty string
	if len(in) == 0 {
		return 0, errors.New("empty string")
	}
	
	// Process each character
	for i, c := range in {
		// Calculate position value
		var val int64
		
		if c >= '0' && c <= '9' {
			val = int64(c - '0')
		} else if c >= 'a' && c <= 'f' {
			val = int64(c - 'a' + 10)
		} else if c >= 'A' && c <= 'F' {
			val = int64(c - 'A' + 10)
		} else {
			return 0, errors.New("invalid hexadecimal character")
		}
		
		// Multiply previous result by 16 and add new value
		result = result*16 + val
		
		// Check for overflow
		if i > 0 && result < 0 {
			return 0, errors.New("value overflow")
		}
	}
	
	return result, nil
}
