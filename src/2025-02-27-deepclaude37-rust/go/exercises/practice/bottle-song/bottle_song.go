package bottlesong

import "fmt"

func Recite(startBottles, takeDown int) []string {
	numbers := []struct{ title, lower string }{
		{"No", "no"},
		{"One", "one"},
		{"Two", "two"},
		{"Three", "three"},
		{"Four", "four"},
		{"Five", "five"},
		{"Six", "six"},
		{"Seven", "seven"},
		{"Eight", "eight"},
		{"Nine", "nine"},
		{"Ten", "ten"},
	}

	var result []string

	for i := 0; i < takeDown; i++ {
		current := startBottles - i
		if current < 0 {
			continue
		}

		var verse []string

		// First two lines
		var titleWord string
		if current >= 0 && current < len(numbers) {
			titleWord = numbers[current].title
		} else {
			titleWord = ""
		}

		pluralCurrent := "bottles"
		if current == 1 {
			pluralCurrent = "bottle"
		}
		line := fmt.Sprintf("%s green %s hanging on the wall,", titleWord, pluralCurrent)
		verse = append(verse, line, line)

		// Third line
		verse = append(verse, "And if one green bottle should accidentally fall,")

		// Fourth line
		m := current - 1
		var lowerWord, pluralNext string
		if m < 0 {
			lowerWord = "no"
			pluralNext = "bottles"
		} else {
			if m < len(numbers) {
				lowerWord = numbers[m].lower
			} else {
				lowerWord = ""
			}
			if m == 1 {
				pluralNext = "bottle"
			} else {
				pluralNext = "bottles"
			}
		}
		line4 := fmt.Sprintf("There'll be %s green %s hanging on the wall.", lowerWord, pluralNext)
		verse = append(verse, line4)

		// Add verse to result
		result = append(result, verse...)

		// Add empty line if not last verse
		if i < takeDown-1 {
			result = append(result, "")
		}
	}

	return result
}
