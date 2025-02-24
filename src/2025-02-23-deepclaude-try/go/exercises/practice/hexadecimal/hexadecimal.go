package hexadecimal

import "fmt"

func ParseHex(in string) (int64, error) {
	if len(in) == 0 {
		return 0, fmt.Errorf("empty string")
	}

	result := int64(0)
	for _, c := range in {
		result *= 16
		switch {
		case '0' <= c && c <= '9':
			result += int64(c - '0')
		case 'a' <= c && c <= 'f':
			result += int64(c - 'a' + 10)
		case 'A' <= c && c <= 'F':
			result += int64(c - 'A' + 10)
		default:
			return 0, fmt.Errorf("invalid character")
		}
		
		if result < 0 {
			return 0, fmt.Errorf("overflow")
		}
	}
	
	return result, nil
}
