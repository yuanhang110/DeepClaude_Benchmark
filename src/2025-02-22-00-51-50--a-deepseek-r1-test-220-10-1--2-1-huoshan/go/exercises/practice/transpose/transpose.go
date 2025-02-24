package transpose

func Transpose(input []string) []string {
	if len(input) == 0 {
		return []string{}
	}

	// Find maximum row length
	maxLen := 0
	for _, row := range input {
		if len(row) > maxLen {
			maxLen = len(row)
		}
	}

	var result []string

	for i := 0; i < maxLen; i++ {
		var transposedRow []rune
		for j, row := range input {
			if i < len(row) {
				transposedRow = append(transposedRow, rune(row[i]))
			} else {
				// Check if any row >=j has length >i
				hasLonger := false
				for k := j; k < len(input); k++ {
					if len(input[k]) > i {
						hasLonger = true
						break
					}
				}
				if hasLonger {
					transposedRow = append(transposedRow, ' ')
				}
			}
		}
		result = append(result, string(transposedRow))
	}

	return result
}
