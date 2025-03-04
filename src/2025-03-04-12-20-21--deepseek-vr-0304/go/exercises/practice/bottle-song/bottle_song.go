package bottlesong

import (
	"fmt"
	"strconv"
	"strings"
)

func Recite(startBottles, takeDown int) []string {
	numberWords := map[int]string{
		0:  "no",
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

	getWord := func(n int) string {
		if word, ok := numberWords[n]; ok {
			return word
		}
		return strconv.Itoa(n)
	}

	var verses []string

	for i := 0; i < takeDown; i++ {
		current := startBottles - i
		currentWord := getWord(current)
		currentBottles := "bottles"
		if current == 1 {
			currentBottles = "bottle"
		}

		line1 := fmt.Sprintf("%s green %s hanging on the wall,", currentWord, currentBottles)
		line2 := line1
		line3 := "And if one green bottle should accidentally fall,"

		next := current - 1
		nextWord := strings.ToLower(getWord(next))

		nextBottles := "bottles"
		if next == 1 {
			nextBottles = "bottle"
		}

		line4 := fmt.Sprintf("There'll be %s green %s hanging on the wall.", nextWord, nextBottles)

		verses = append(verses, line1, line2, line3, line4)

		if i != takeDown-1 {
			verses = append(verses, "")
		}
	}

	return verses
}
