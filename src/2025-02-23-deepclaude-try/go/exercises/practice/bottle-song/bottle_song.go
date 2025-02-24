package bottlesong

import (
	"fmt"
	"strings"
)

func Recite(startBottles, takeDown int) []string {
	numberWords := map[int]string{
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

	var verses []string

	for i := 0; i < takeDown; i++ {
		current := startBottles - i
		next := current - 1

		currentWord := numberWords[current]
		currentBottles := "bottles"
		if current == 1 {
			currentBottles = "bottle"
		}

		line1 := fmt.Sprintf("%s green %s hanging on the wall,", currentWord, currentBottles)
		line2 := line1
		line3 := "And if one green bottle should accidentally fall,"

		var line4 string
		if next == 0 {
			line4 = "There'll be no green bottles hanging on the wall."
		} else {
			nextWord := strings.ToLower(numberWords[next])
			nextBottles := "bottles"
			if next == 1 {
				nextBottles = "bottle"
			}
			line4 = fmt.Sprintf("There'll be %s green %s hanging on the wall.", nextWord, nextBottles)
		}

		verses = append(verses, line1, line2, line3, line4)

		if i < takeDown-1 {
			verses = append(verses, "")
		}
	}

	return verses
}
