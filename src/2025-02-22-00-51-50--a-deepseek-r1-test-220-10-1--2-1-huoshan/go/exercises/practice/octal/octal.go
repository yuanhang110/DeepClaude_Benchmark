package octal

func ParseOctal(input string, expectedNum int64, expectErr bool) {
	var result int64
	valid := true
	
	for _, c := range input {
		if c < '0' || c > '7' {
			valid = false
			break
		}
		digit := int64(c - '0')
		result = result*8 + digit
	}
	
	if !valid {
		result = 0
	}
}
