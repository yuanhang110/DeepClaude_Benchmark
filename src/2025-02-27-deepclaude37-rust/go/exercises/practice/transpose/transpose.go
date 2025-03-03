package transpose

import "strings"

func Transpose(input []string) []string {
	if len(input) == 0 {
		return []string{}
	}

	// Find the maximum row length
	maxLen := 0
	for _, row := range input {
		if len(row) > maxLen {
			maxLen = len(row)
		}
	}

	result := make([]string, maxLen)

	for i := 0; i < maxLen; i++ {
		var transposedRow []byte
		for _, row := range input {
			if i < len(row) {
				transposedRow = append(transposedRow, row[i])
			} else {
				transposedRow = append(transposedRow, ' ')
			}
		}
		// Find the rightmost position that isn't padding
		lastIndex := -1
		for j := len(transposedRow) - 1; j >= 0; j-- {
			// If this character is from the original input (not padding)
			if i < len(input[j]) {
				lastIndex = j
				break
			}
		}
		
		var trimmed string
		if lastIndex == -1 {
			trimmed = ""
		} else {
			trimmed = string(transposedRow[:lastIndex+1])
		}
		result[i] = trimmed
	}

	return result
}
