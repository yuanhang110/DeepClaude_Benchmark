package say

import (
	"strings"
)

var (
	ones = []string{"", "one", "two", "three", "four", 
		"five", "six", "seven", "eight", "nine"}
	teens = []string{"ten", "eleven", "twelve", "thirteen", 
		"fourteen", "fifteen", "sixteen", 
		"seventeen", "eighteen", "nineteen"}
	tens = []string{"", "ten", "twenty", "thirty", "forty", 
		"fifty", "sixty", "seventy", "eighty", "ninety"}
	scales = []string{"", "thousand", "million", "billion"}
)

func Say(n int64) (string, bool) {
	if n < 0 || n > 999_999_999_999 {
		return "", false
	}
	if n == 0 {
		return "zero", true
	}
	
	chunks := splitIntoChunks(n)
	result := []string{}
	
	for i, chunk := range chunks {
		if chunk == 0 {
			continue
		}
		words := chunkToWords(chunk)
		if scales[i] != "" {
			words += " " + scales[i]
		}
		result = append([]string{words}, result...)
	}
	
	return strings.Join(result, " "), true
}

func splitIntoChunks(n int64) []int64 {
	chunks := []int64{}
	for n > 0 {
		chunks = append(chunks, n%1000)
		n /= 1000
	}
	return chunks
}

func chunkToWords(n int64) string {
	if n == 0 {
		return ""
	}
	
	words := ""
	if n >= 100 {
		words += ones[n/100] + " hundred "
		n %= 100
	}
	
	if n >= 20 {
		words += tens[n/10] + " "
		n %= 10
	} else if n >= 10 {
		words += teens[n-10] + " "
		n = 0
	}
	
	if n > 0 {
		words += ones[n] + " "
	}
	
	return strings.TrimSpace(words)
}
