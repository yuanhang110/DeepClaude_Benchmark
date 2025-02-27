package bottlesong

import (
	"fmt"
	"strings"
)

func Recite(startBottles, takeDown int) []string {
	var result []string

	for i := 0; i < takeDown; i++ {
		current := startBottles - i
		verse := generateVerse(current)
		result = append(result, verse...)
		if i != takeDown-1 {
			result = append(result, "")
		}
	}

	return result
}

func generateVerse(n int) []string {
	numWord := numberToWord(n)
	bottles := "bottles"
	if n == 1 {
		bottles = "bottle"
	}

	nextNum := n - 1
	nextNumWord := numberToWord(nextNum)
	nextBottles := "bottles"
	if nextNum == 1 {
		nextBottles = "bottle"
	}

	line1 := fmt.Sprintf("%s green %s hanging on the wall,", numWord, bottles)
	line2 := line1
	line3 := "And if one green bottle should accidentally fall,"
	line4 := fmt.Sprintf("There'll be %s green %s hanging on the wall.", strings.ToLower(nextNumWord), nextBottles)

	return []string{line1, line2, line3, line4}
}

func numberToWord(n int) string {
	switch n {
	case 10:
		return "Ten"
	case 9:
		return "Nine"
	case 8:
		return "Eight"
	case 7:
		return "Seven"
	case 6:
		return "Six"
	case 5:
		return "Five"
	case 4:
		return "Four"
	case 3:
		return "Three"
	case 2:
		return "Two"
	case 1:
		return "One"
	case 0:
		return "No"
	default:
		return ""
	}
}
