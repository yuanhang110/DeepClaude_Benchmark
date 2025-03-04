package trinary

import (
	"errors"
	"math"
)

func ParseTrinary(s string) (int64, error) {
	if len(s) == 0 {
		return 0, nil
	}

	// Validate input
	for _, r := range s {
		if r < '0' || r > '2' {
			return 0, errors.New("invalid trinary digit")
		}
	}

	// Convert trinary to decimal
	var result int64
	length := len(s)
	for i, r := range s {
		digit := int64(r - '0')
		power := length - i - 1
		term := digit * int64(math.Pow(3, float64(power)))
		
		// Check for overflow
		if result > math.MaxInt64 - term {
			return 0, errors.New("integer overflow")
		}
		result += term
	}

	return result, nil
}
