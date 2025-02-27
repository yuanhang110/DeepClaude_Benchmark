package transpose

import "strings"

func Transpose(input []string) []string {
	if len(input) == 0 {
		return []string{}
	}

	maxLen := 0
	for _, row := range input {
		if len(row) > maxLen {
			maxLen = len(row)
		}
	}

	result := make([]string, maxLen)
	for i := 0; i < maxLen; i++ {
		var builder strings.Builder
		for _, row := range input {
			if i < len(row) {
				builder.WriteByte(row[i])
			} else {
				builder.WriteByte(' ')
			}
		}
		trimmed := strings.TrimRight(builder.String(), " ")
		result[i] = trimmed
	}

	return result
}
