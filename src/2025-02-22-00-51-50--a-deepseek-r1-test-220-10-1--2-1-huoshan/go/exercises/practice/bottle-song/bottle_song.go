package bottlesong

import (
	"fmt"
	"strings"
)

var numberWords = map[int]string{
	1:  "One",
	2:  "Two",
	3:  "Three",
	4:  "Four",
	5:  "Five",
	6:  "Six",
	7:  "Seven",
	8:  "Eight",
	9:  "Nine",
	10: "Ten",
}

func Recite(startBottles, takeDown int) []string {
	var verses []string
	end := startBottles - takeDown + 1
	if end < 1 {
		end = 1
	}
	for current := startBottles; current >= end; current-- {
		verse := generateVerse(current)
		verses = append(verses, verse...)
		if current > end {
			verses = append(verses, "")
		}
	}
	return verses
}

func generateVerse(current int) []string {
	next := current - 1
	line1 := fmt.Sprintf("%s hanging on the wall,", currentBottlePhrase(current))
	line2 := line1
	line3 := "And if one green bottle should accidentally fall,"
	line4 := fmt.Sprintf("There'll be %s hanging on the wall.", nextBottlePhrase(next))
	return []string{line1, line2, line3, line4}
}

func currentBottlePhrase(n int) string {
	if n == 1 {
		return "One green bottle"
	}
	word, ok := numberWords[n]
	if !ok {
		return fmt.Sprintf("%d green bottles", n)
	}
	return fmt.Sprintf("%s green bottles", word)
}

func nextBottlePhrase(n int) string {
	if n == 0 {
		return "no green bottles"
	}
	if n == 1 {
		return "one green bottle"
	}
	word, ok := numberWords[n]
	if !ok {
		return fmt.Sprintf("%d green bottles", n)
	}
	return fmt.Sprintf("%s green bottles", strings.ToLower(word))
}
