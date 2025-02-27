package cryptosquare

import (
	"math"
	"strings"
	"unicode"
)

func Encode(pt string) string {
	// Normalize the input
	var normalized []rune
	for _, ch := range pt {
		if unicode.IsLetter(ch) || unicode.IsDigit(ch) {
			normalized = append(normalized, unicode.ToLower(ch))
		}
	}
	n := len(normalized)
	if n == 0 {
		return ""
	}

	// Compute rectangle dimensions
	c := int(math.Ceil(math.Sqrt(float64(n))))
	r := int(math.Ceil(float64(n) / float64(c)))

	// Ensure the grid has enough space
	padLength := r*c - n
	normalized = append(normalized, []rune(strings.Repeat(" ", padLength))...)

	// Build encoded columns
	encoded := make([]string, c)
	for i := 0; i < c; i++ {
		col := make([]rune, r)
		for j := 0; j < r; j++ {
			pos := j*c + i
			col[j] = normalized[pos]
		}
		encoded[i] = string(col)
	}

	return strings.Join(encoded, " ")
}
