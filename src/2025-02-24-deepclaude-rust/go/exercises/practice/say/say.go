package say

import "strings"

func Say(n int64) (string, bool) {
	if n < 0 || n > 999999999999 {
		return "", false
	}
	if n == 0 {
		return "zero", true
	}

	chunks := splitChunks(n)
	scales := []string{"", "thousand", "million", "billion", "trillion"}
	var parts []string

	for i, chunk := range chunks {
		if chunk == 0 {
			continue
		}
		scaleIndex := len(chunks) - 1 - i
		chunkStr := convertLessThanThousand(chunk)
		if scaleIndex < len(scales) && scales[scaleIndex] != "" {
			chunkStr += " " + scales[scaleIndex]
		}
		parts = append(parts, chunkStr)
	}

	return strings.Join(parts, " "), true
}

func splitChunks(n int64) []int64 {
	var chunks []int64
	for n > 0 {
		chunks = append(chunks, n%1000)
		n = n / 1000
	}
	// Reverse chunks
	for i, j := 0, len(chunks)-1; i < j; i, j = i+1, j-1 {
		chunks[i], chunks[j] = chunks[j], chunks[i]
	}
	return chunks
}

func convertLessThanThousand(num int64) string {
	ones := []string{
		"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine",
		"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
	}
	tens := []string{
		"", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety",
	}

	var result string

	hundreds := num / 100
	remainder := num % 100

	if hundreds > 0 {
		result += ones[hundreds] + " hundred "
	}

	if remainder > 0 {
		if remainder < 20 {
			result += ones[remainder]
		} else {
			tenPart := tens[remainder/10]
			unitPart := ones[remainder%10]
			if unitPart != "zero" {
				result += tenPart + "-" + unitPart
			} else {
				result += tenPart
			}
		}
	}

	return strings.TrimSpace(result)
}
