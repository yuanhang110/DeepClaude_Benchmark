package alphametics

import (
	"errors"
	"strings"
)

func Solve(puzzle string) (map[string]int, error) {
	// Parse the puzzle into addends and result
	parts := strings.Split(puzzle, "==")
	if len(parts) != 2 {
		return nil, errors.New("invalid puzzle format")
	}
	leftPart := strings.TrimSpace(parts[0])
	rightPart := strings.TrimSpace(parts[1])
	addends := strings.Split(leftPart, "+")
	for i := range addends {
		addends[i] = strings.TrimSpace(addends[i])
		if len(addends[i]) == 0 {
			return nil, errors.New("empty addend")
		}
	}
	result := strings.TrimSpace(rightPart)
	if len(result) == 0 {
		return nil, errors.New("empty result")
	}

	// Collect unique letters and track first letters
	letters := []rune{}
	firstLetters := make(map[rune]bool)
	letterSet := make(map[rune]bool)

	// Process addends
	for _, word := range addends {
		// Track first letter of each addend
		firstChar := rune(word[0])
		firstLetters[firstChar] = true

		// Collect all letters in addend
		for _, c := range word {
			if !letterSet[c] {
				letters = append(letters, c)
				letterSet[c] = true
			}
		}
	}

	// Process result word
	firstChar := rune(result[0])
	firstLetters[firstChar] = true
	for _, c := range result {
		if !letterSet[c] {
			letters = append(letters, c)
			letterSet[c] = true
		}
	}

	// Check if there are more than 10 unique letters
	if len(letters) > 10 {
		return nil, errors.New("too many unique letters")
	}

	// Create a map from letter to index for quick lookup
	letterIndex := make(map[rune]int)
	for i, c := range letters {
		letterIndex[c] = i
	}

	// Prepare for backtracking
	solution := make(map[string]int)
	solutionDigits := make([]int, len(letters))
	used := make([]bool, 10)
	found := false

	var backtrack func(int)
	backtrack = func(pos int) {
		if found {
			return
		}
		if pos == len(letters) {
			// Calculate sum of addends
			sum := 0
			for _, word := range addends {
				num := 0
				for _, c := range word {
					num = num*10 + solutionDigits[letterIndex[c]]
				}
				sum += num
			}

			// Calculate result value
			resultNum := 0
			for _, c := range result {
				resultNum = resultNum*10 + solutionDigits[letterIndex[c]]
			}

			if sum == resultNum {
				// Build the solution map
				for i, c := range letters {
					solution[string(c)] = solutionDigits[i]
				}
				found = true
			}
			return
		}

		currentLetter := letters[pos]
		for d := 0; d < 10; d++ {
			if used[d] {
				continue
			}
			// Skip zero for first letters
			if d == 0 && firstLetters[currentLetter] {
				continue
			}
			used[d] = true
			solutionDigits[pos] = d
			backtrack(pos + 1)
			if found {
				return
			}
			used[d] = false
		}
	}

	backtrack(0)

	if found {
		return solution, nil
	}
	return nil, errors.New("no solution")
}
