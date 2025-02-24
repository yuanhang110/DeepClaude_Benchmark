package cryptosquare

import (
    "bytes"
    "math"
    "strings"
    "unicode"
)

func Encode(pt string) string {
    // Normalize input
    var normalized bytes.Buffer
    for _, ch := range pt {
        if unicode.IsLetter(ch) || unicode.IsDigit(ch) {
            normalized.WriteRune(unicode.ToLower(ch))
        }
    }
    normalizedStr := normalized.String()
    length := len(normalizedStr)

    if length == 0 {
        return ""
    }

    // Calculate rectangle dimensions
    c := int(math.Ceil(math.Sqrt(float64(length))))
    r := int(math.Ceil(float64(length) / float64(c)))

    // Pad the normalized string to fill the rectangle
    padded := normalizedStr + strings.Repeat(" ", c*r - length)

    // Build rows
    rows := make([]string, r)
    for i := 0; i < r; i++ {
        start := i * c
        end := start + c
        rows[i] = padded[start:end]
    }

    // Read columns and build encoded string
    var encodedParts []string
    for j := 0; j < c; j++ {
        var colBuf bytes.Buffer
        for i := 0; i < r; i++ {
            if j < len(rows[i]) {
                colBuf.WriteByte(rows[i][j])
            } else {
                colBuf.WriteByte(' ')
            }
        }
        encodedParts = append(encodedParts, colBuf.String())
    }

    return strings.Join(encodedParts, " ")
}
