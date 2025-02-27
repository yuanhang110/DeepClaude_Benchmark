package octal

import "fmt"

func ParseOctal(input string) (int64, error) {
	var result int64
	for _, c := range input {
		if c < '0' || c > '7' {
			// Invalid input is treated as octal 0
			return 0, fmt.Errorf("invalid octal digit")
		}
		digit := int64(c - '0')
		result = result*8 + digit
	}
	return result, nil
}
