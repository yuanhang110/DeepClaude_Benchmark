package say

import (
	"strings"
)

var ones = [...]string{
	"zero", "one", "two", "three", "four",
	"five", "six", "seven", "eight", "nine",
	"ten", "eleven", "twelve", "thirteen", "fourteen",
	"fifteen", "sixteen", "seventeen", "eighteen", "nineteen",
}

var tens = [...]string{
	"", "", "twenty", "thirty", "forty",
	"fifty", "sixty", "seventy", "eighty", "ninety",
}

var scales = [...]string{
	"", "thousand", "million", "billion",
}

func Say(n int64) (string, bool) {
	if n < 0 || n > 999_999_999_999 {
		return "", false
	}

	if n == 0 {
		return ones[0], true
	}

	var chunks []int64
	for n > 0 {
		chunks = append(chunks, n%1000)
		n /= 1000
	}

	var parts []string
	for i, chunk := range chunks {
		if chunk == 0 {
			continue
		}
		part := convertChunk(chunk)
		if i > 0 {
			part += " " + scales[i]
		}
		parts = append([]string{part}, parts...)
	}

	return strings.Join(parts, " "), true
}

func convertChunk(n int64) string {
	var parts []string
	
	if n >= 100 {
		parts = append(parts, ones[n/100]+" hundred")
		n %= 100
	}

	if n >= 20 {
		part := tens[n/10]
		if n%10 != 0 {
			part += "-" + ones[n%10]
		}
		parts = append(parts, part)
	} else if n > 0 {
		parts = append(parts, ones[n])
	}

	return strings.Join(parts, " ")
}
