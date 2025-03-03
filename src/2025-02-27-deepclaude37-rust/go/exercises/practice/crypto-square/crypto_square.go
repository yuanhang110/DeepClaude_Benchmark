package cryptosquare

import (
	"math"
	"strings"
	"unicode"
)

func Encode(pt string) string {
	// Normalize the input text
	var normalized strings.Builder
	for _, ch := range pt {
		if unicode.IsLetter(ch) || unicode.IsDigit(ch) {
			normalized.WriteRune(unicode.ToLower(ch))
		}
	}
	normalizedStr := normalized.String()
	length := normalized.Len()
	if length == 0 {
		return ""
	}

	// Calculate rectangle dimensions
	c := int(math.Ceil(math.Sqrt(float64(length))))
	r := int(math.Ceil(float64(length) / float64(c)))

	// Pad the normalized string to fill the rectangle
	requiredLength := r * c
	pad := requiredLength - length
	normalizedStr += strings.Repeat(" ", pad)

	// Split into rows
	rows := make([]string, r)
	for i := 0; i < r; i++ {
		start := i * c
		end := start + c
		rows[i] = normalizedStr[start:end]
	}

	// Read columns to form the encoded string
	var encoded strings.Builder
	for col := 0; col < c; col++ {
		for _, row := range rows {
			encoded.WriteByte(row[col])
		}
	}
	encodedStr := encoded.String()

	// Split into chunks and join with spaces
	var result strings.Builder
	for i := 0; i < c; i++ {
		start := i * r
		end := start + r
		if i > 0 {
			result.WriteByte(' ')
		}
		result.WriteString(encodedStr[start:end])
	}

	return result.String()
}
