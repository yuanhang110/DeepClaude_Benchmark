package say

import "strings"

var (
	underTwenty = []string{
		"",         // 0
		"one",      // 1
		"two",      // 2
		"three",    // 3
		"four",     // 4
		"five",     // 5
		"six",      // 6
		"seven",    // 7
		"eight",    // 8
		"nine",     // 9
		"ten",      // 10
		"eleven",   // 11
		"twelve",   // 12
		"thirteen", // 13
		"fourteen", // 14
		"fifteen",  // 15
		"sixteen",  // 16
		"seventeen",// 17
		"eighteen", // 18
		"nineteen", // 19
	}
	tens = []string{
		"",        // 0
		"ten",     // 10
		"twenty",  // 20
		"thirty",  // 30
		"forty",   // 40
		"fifty",   // 50
		"sixty",   // 60
		"seventy", // 70
		"eighty",  // 80
		"ninety",  // 90
	}
)

func Say(n int64) (string, bool) {
	if n < 0 || n > 999999999999 {
		return "", false
	}
	if n == 0 {
		return "zero", true
	}

	chunks := splitNumber(n)
	scales := []string{"", "thousand", "million", "billion"}
	var parts []string

	for i, chunk := range chunks {
		scaleIndex := len(chunks) - i - 1
		part := convertTrio(chunk)
		if part != "" {
			if scaleIndex > 0 {
				part += " " + scales[scaleIndex]
			}
			parts = append(parts, part)
		}
	}

	return strings.Join(parts, " "), true
}

func splitNumber(n int64) []int {
	var chunks []int
	for n > 0 {
		chunks = append(chunks, int(n%1000))
		n /= 1000
	}
	// Reverse chunks to get highest order first
	for i, j := 0, len(chunks)-1; i < j; i, j = i+1, j-1 {
		chunks[i], chunks[j] = chunks[j], chunks[i]
	}
	return chunks
}

func convertTrio(n int) string {
	if n == 0 {
		return ""
	}
	var result string
	hundreds := n / 100
	if hundreds > 0 {
		result += underTwenty[hundreds] + " hundred"
	}
	tensPart := n % 100
	if tensPart > 0 {
		if hundreds > 0 {
			result += " "
		}
		if tensPart < 20 {
			result += underTwenty[tensPart]
		} else {
			tensDigit := tensPart / 10
			onesDigit := tensPart % 10
			result += tens[tensDigit]
			if onesDigit > 0 {
				result += "-" + underTwenty[onesDigit]
			}
		}
	}
	return result
}
