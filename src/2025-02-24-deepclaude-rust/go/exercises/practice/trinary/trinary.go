package trinary

func ParseTrinary(arg string) (int64, bool) {
	var result int64 = 0
	
	// Check for invalid characters
	for _, c := range arg {
		if c != '0' && c != '1' && c != '2' {
			return 0, false
		}
	}
	
	// Convert to decimal
	for i := 0; i < len(arg); i++ {
		digit := int64(arg[i] - '0')
		// Calculate 3^(len-1-i) and multiply by digit
		power := len(arg) - 1 - i
		multiplier := int64(1)
		for j := 0; j < power; j++ {
			multiplier *= 3
		}
		result += digit * multiplier
	}
	
	return result, true
}
