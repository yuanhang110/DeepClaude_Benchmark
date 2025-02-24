package trinary

import "errors"

func ParseTrinary(s string) (int64, error) {
	if len(s) == 0 {
		return 0, errors.New("invalid trinary digit")
	}
	
	var result int64
	for _, c := range s {
		if c < '0' || c > '2' {
			return 0, errors.New("invalid trinary digit")
		}
		digit := int64(c - '0')
		result = result*3 + digit
	}
	return result, nil
}
