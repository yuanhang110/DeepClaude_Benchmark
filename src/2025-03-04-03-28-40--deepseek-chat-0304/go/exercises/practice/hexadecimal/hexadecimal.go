package hexadecimal

import (
	"errors"
	"strings"
)

var ErrInvalidHex = errors.New("invalid hexadecimal string")

func ParseHex(in string) (int64, error) {
	// Handle empty string
	if len(in) == 0 {
		return 0, ErrInvalidHex
	}

	// Convert to lowercase for case insensitivity
	in = strings.ToLower(in)

	var result int64 = 0

	for _, char := range in {
		result *= 16
		switch {
		case char >= '0' && char <= '9':
			result += int64(char - '0')
		case char >= 'a' && char <= 'f':
			result += int64(char - 'a' + 10)
		default:
			return 0, ErrInvalidHex
		}
	}

	return result, nil
}

func HandleErrors(hexStrings []string) int {
	errorCount := 0
	for _, hexStr := range hexStrings {
		_, err := ParseHex(hexStr)
		if err != nil {
			errorCount++
		}
	}
	return errorCount
}
