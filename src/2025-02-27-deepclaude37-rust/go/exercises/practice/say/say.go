package say

import "strings"

var ones = []string{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"}
var teens = []string{"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"}
var tens = []string{"", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"}
var scales = []string{"", "thousand", "million", "billion", "trillion"}

func Say(n int64) (string, bool) {
	if n < 0 || n > 999999999999 {
		return "", false
	}
	if n == 0 {
		return "zero", true
	}

	chunks := splitIntoChunks(n)
	reversedChunks := reverseChunks(chunks)
	var parts []string

	for i, chunk := range reversedChunks {
		if chunk == 0 {
			continue
		}
		part := numToWordsUnder1000(chunk)
		scaleIndex := len(reversedChunks) - 1 - i
		if scaleIndex >= len(scales) {
			return "", false
		}
		scale := scales[scaleIndex]
		if scale != "" {
			part += " " + scale
		}
		parts = append(parts, part)
	}

	return strings.Join(parts, " "), true
}

func splitIntoChunks(n int64) []int64 {
	var chunks []int64
	for n > 0 {
		chunks = append(chunks, n%1000)
		n /= 1000
	}
	return chunks
}

func reverseChunks(chunks []int64) []int64 {
	reversed := make([]int64, len(chunks))
	for i, j := 0, len(chunks)-1; j >= 0; i, j = i+1, j-1 {
		reversed[i] = chunks[j]
	}
	return reversed
}

func numToWordsUnder1000(n int64) string {
	var parts []string
	hundreds := n / 100
	remainder := n % 100

	if hundreds > 0 {
		parts = append(parts, ones[hundreds]+" hundred")
	}
	if remainder > 0 {
		parts = append(parts, numToWordsUnder100(remainder))
	}
	return strings.Join(parts, " ")
}

func numToWordsUnder100(n int64) string {
	if n < 10 {
		return ones[n]
	}
	if n < 20 {
		return teens[n-10]
	}
	tensDigit := n / 10
	onesDigit := n % 10
	tensPart := tens[tensDigit]
	if onesDigit == 0 {
		return tensPart
	}
	return tensPart + "-" + ones[onesDigit]
}
