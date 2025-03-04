package bottlesong

import (
	"fmt"
)

func Recite(startBottles, takeDown int) []string {
	var verses []string
	for i := startBottles; i > startBottles-takeDown; i-- {
		if i == 0 {
			break
		}
		
		// First two lines
		verse := fmt.Sprintf("%s green %s hanging on the wall,", capitalize(numberToWord(i)), pluralize(i))
		verses = append(verses, verse, verse)
		
		// Third line
		verses = append(verses, "And if one green bottle should accidentally fall,")
		
		// Fourth line
		remaining := i - 1
		if remaining == 0 {
			verses = append(verses, "There'll be no green bottles hanging on the wall.")
		} else {
			verses = append(verses, fmt.Sprintf("There'll be %s green %s hanging on the wall.", numberToWord(remaining), pluralize(remaining)))
		}
		
		// Add empty line between verses except the last one
		if i > startBottles-takeDown+1 {
			verses = append(verses, "")
		}
	}
	return verses
}

func numberToWord(n int) string {
	words := []string{"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"}
	if n >= 0 && n <= 10 {
		return words[n]
	}
	return ""
}

func capitalize(s string) string {
	if len(s) == 0 {
		return s
	}
	return string(s[0]-32) + s[1:]
}

func pluralize(n int) string {
	if n == 1 {
		return "bottle"
	}
	return "bottles"
}
