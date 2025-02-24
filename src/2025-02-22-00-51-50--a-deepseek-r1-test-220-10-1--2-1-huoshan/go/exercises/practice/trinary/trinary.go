package trinary

func ParseTrinary(s string) (int64, bool) {
	var result int64
	power := 1
	valid := true
	
	for i := len(s) - 1; i >= 0; i-- {
		c := s[i]
		switch c {
		case '0':
			// No change to result
		case '1':
			result += int64(power)
		case '2':
			result += 2 * int64(power)
		default: // Invalid trinary digit
			return 0, false
		}
		power *= 3
	}
	
	return result, valid
}
