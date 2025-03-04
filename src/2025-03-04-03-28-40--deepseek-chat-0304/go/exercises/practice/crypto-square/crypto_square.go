package cryptosquare

import (
	"math"
	"strings"
	"unicode"
)

func Encode(pt string) string {
	// Normalize the input
	var normalized strings.Builder
	for _, r := range pt {
		if unicode.IsLetter(r) || unicode.IsDigit(r) {
			normalized.WriteRune(unicode.ToLower(r))
		}
	}
	text := normalized.String()
	length := len(text)

	if length == 0 {
		return ""
	}

	// Calculate rectangle dimensions
	c := int(math.Ceil(math.Sqrt(float64(length))))
	r := c
	if r*(c-1) >= length {
		c--
	}

	// Pad text with spaces if needed
	padding := r*c - length
	if padding > 0 {
		text += strings.Repeat(" ", padding)
	}

	// Build the encoded message
	var encoded strings.Builder
	for col := 0; col < c; col++ {
		if col > 0 {
			encoded.WriteByte(' ')
		}
		for row := 0; row < r; row++ {
			idx := col + row*c
			encoded.WriteByte(text[idx])
		}
	}

	return encoded.String()
}
