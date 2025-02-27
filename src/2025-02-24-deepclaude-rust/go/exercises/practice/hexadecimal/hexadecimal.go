package hexadecimal

import (
	"errors"
	"math"
)

func HandleErrors(err error) string {
	if err == nil {
		return ""
	}
	switch err.Error() {
	case "invalid syntax":
		return "invalid"
	case "overflow":
		return "overflow"
	default:
		return ""
	}
}

func ParseHex(s string) (int64, error) {
	if s == "" {
		return 0, errors.New("invalid syntax")
	}

	maxInt64 := uint64(math.MaxInt64)
	var result uint64
	for _, c := range s {
		var digit uint64
		switch {
		case c >= '0' && c <= '9':
			digit = uint64(c - '0')
		case c >= 'a' && c <= 'f':
			digit = uint64(c - 'a' + 10)
		case c >= 'A' && c <= 'F':
			digit = uint64(c - 'A' + 10)
		default:
			return 0, errors.New("invalid syntax")
		}

		if result > (maxInt64 - digit)/16 {
			return 0, errors.New("overflow")
		}
		result = result * 16 + digit
	}
	return int64(result), nil
}
