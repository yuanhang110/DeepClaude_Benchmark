package octal

import (
	"errors"
)

func ParseOctal(input string) (int64, error) {
	var result int64
	for _, char := range input {
		// Check if character is a valid octal digit
		if char < '0' || char > '7' {
			return 0, errors.New("invalid octal digit")
		}
		
		// Convert character to digit value
		digit := int64(char - '0')
		
		// Shift result left by 3 bits (equivalent to multiplying by 8)
		// and add the new digit
		result = result*8 + digit
	}
	
	return result, nil
}
