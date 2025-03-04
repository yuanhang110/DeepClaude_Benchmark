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

	// Build transposed matrix
	result := make([]string, maxLen)
	for i := 0; i < maxLen; i++ {
		var transposedRow []byte
		for rowIdx := 0; rowIdx < len(input); rowIdx++ {
			if i < len(input[rowIdx]) {
				transposedRow = append(transposedRow, input[rowIdx][i])
			} else if len(transposedRow) > 0 {
				// Only pad with space if we've already started building this row
				transposedRow = append(transposedRow, ' ')
			}
		}
		result[i] = string(transposedRow)
	}

	return result
}
