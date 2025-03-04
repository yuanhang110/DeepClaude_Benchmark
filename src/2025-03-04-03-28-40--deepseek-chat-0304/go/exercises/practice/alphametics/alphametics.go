package alphametics

import (
	"errors"
	"strings"
	"unicode"
)

func Solve(puzzle string) (map[string]int, error) {
	parts := strings.Split(puzzle, "==")
	if len(parts) != 2 {
		return nil, errors.New("invalid puzzle format")
	}

	leftWords := strings.Fields(parts[0])
	rightWord := strings.TrimSpace(parts[1])

	// Collect all unique letters and identify leading letters
	letters := make(map[rune]bool)
	leadingLetters := make(map[rune]bool)
	for _, word := range append(leftWords, rightWord) {
		for i, char := range word {
			if unicode.IsLetter(char) {
				letters[char] = true
				if i == 0 {
					leadingLetters[char] = true
				}
			}
		}
	}

	uniqueLetters := make([]rune, 0, len(letters))
	for char := range letters {
		uniqueLetters = append(uniqueLetters, char)
	}

	// Try all permutations using backtracking
	mapping := make(map[string]int)
	usedDigits := make(map[int]bool)
	if solveRecursive(uniqueLetters, 0, mapping, usedDigits, leftWords, rightWord, leadingLetters) {
		return mapping, nil
	}

	return nil, errors.New("no solution found")
}

func solveRecursive(letters []rune, index int, mapping map[string]int, usedDigits map[int]bool,
	leftWords []string, rightWord string, leadingLetters map[rune]bool) bool {
	if index == len(letters) {
		return isValidMapping(leftWords, rightWord, mapping)
	}

	char := letters[index]
	for digit := 0; digit <= 9; digit++ {
		// Skip if digit is already used or if it's a leading zero
		if usedDigits[digit] || (digit == 0 && leadingLetters[char]) {
			continue
		}

		mapping[string(char)] = digit
		usedDigits[digit] = true

		if solveRecursive(letters, index+1, mapping, usedDigits, leftWords, rightWord, leadingLetters) {
			return true
		}

		delete(mapping, string(char))
		usedDigits[digit] = false
	}

	return false
}

func isValidMapping(leftWords []string, rightWord string, mapping map[string]int) bool {
	leftSum := 0
	for _, word := range leftWords {
		num := 0
		for _, char := range word {
			num = num*10 + mapping[string(char)]
		}
		leftSum += num
	}

	rightSum := 0
	for _, char := range rightWord {
		rightSum = rightSum*10 + mapping[string(char)]
	}

	return leftSum == rightSum
}
