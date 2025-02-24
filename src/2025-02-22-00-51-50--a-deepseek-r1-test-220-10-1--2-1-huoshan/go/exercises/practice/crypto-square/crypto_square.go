package cryptosquare

import (
	"math"
	"strings"
	"unicode"
)

func Encode(pt string) string {
	// Normalize the input
	var normalizedBuf strings.Builder
	for _, ch := range pt {
		if unicode.IsLetter(ch) || unicode.IsDigit(ch) {
			normalizedBuf.WriteRune(unicode.ToLower(ch))
		}
	}
	normalized := normalizedBuf.String()
	length := len(normalized)

	if length == 0 {
		return ""
	}

	// Calculate rectangle dimensions
	c := int(math.Ceil(math.Sqrt(float64(length))))
	r := int(math.Ceil(float64(length) / float64(c)))

	// Pad the normalized string with spaces if needed
	padLength := c*r - length
	normalized += strings.Repeat(" ", padLength)

	// Build rows
	rows := make([]string, r)
	for i := 0; i < r; i++ {
		start := i * c
		end := start + c
		rows[i] = normalized[start:end]
	}

	// Build encoded string by reading columns
	var encoded strings.Builder
	for col := 0; col < c; col++ {
		for _, row := range rows {
			if col < len(row) {
				encoded.WriteByte(row[col])
			} else {
				encoded.WriteByte(' ')
			}
		}
	}

	// Split into chunks and join with spaces
	encodedStr := encoded.String()
	result := make([]string, 0, c)
	for i := 0; i < c; i++ {
		start := i * r
		end := start + r
		result = append(result, encodedStr[start:end])
	}

	return strings.Join(result, " ")
}
