package alphametics

import (
	"errors"
	"fmt"
	"strings"
)

func Solve(puzzle string) (map[string]int, error) {
	// Remove all spaces from the puzzle
	puzzle = strings.ReplaceAll(puzzle, " ", "")

	// Split into left and right parts
	parts := strings.Split(puzzle, "==")
	if len(parts) != 2 {
		return nil, errors.New("invalid puzzle format")
	}
	leftPart := parts[0]
	rightPart := parts[1]

	// Split left part into individual words
	leftWords := strings.Split(leftPart, "+")
	if len(leftWords) < 1 {
		return nil, errors.New("invalid puzzle format: no left operands")
	}

	// Collect all words including the right part
	allWords := append(leftWords, rightPart)

	// Collect unique letters and leading letters
	letterSet := make(map[rune]struct{})
	var leadingLetters []rune
	for _, word := range allWords {
		for i, c := range word {
			// Add to letterSet if not present
			if _, exists := letterSet[c]; !exists {
				letterSet[c] = struct{}{}
			}
			// Record leading letters (first character of each word)
			if i == 0 {
				leadingLetters = append(leadingLetters, c)
			}
		}
	}

	// Convert letterSet to a slice of runes
	letters := make([]rune, 0, len(letterSet))
	for c := range letterSet {
		letters = append(letters, c)
	}

	// Create a set of leading letters for quick lookup
	leadingSet := make(map[rune]bool)
	for _, c := range leadingLetters {
		leadingSet[c] = true
	}

	// Initialize backtracking variables
	usedDigits := make([]bool, 10)
	assignments := make(map[rune]int)

	// Start backtracking
	solution, found := backtrack(letters, leadingSet, 0, usedDigits, assignments, leftWords, rightPart)
	if !found {
		return nil, errors.New("no solution found")
	}

	// Convert map[rune]int to map[string]int
	result := make(map[string]int)
	for c, digit := range solution {
		result[string(c)] = digit
	}

	return result, nil
}

func backtrack(
	letters []rune,
	leadingSet map[rune]bool,
	pos int,
	usedDigits []bool,
	assignments map[rune]int,
	leftWords []string,
	rightPart string,
) (map[rune]int, bool) {
	// If all letters are assigned, check the solution
	if pos == len(letters) {
		// Calculate sum of left words
		sumLeft := 0
		for _, word := range leftWords {
			num, err := wordToNumber(word, assignments)
			if err != nil {
				return nil, false
			}
			sumLeft += num
		}

		// Calculate right part
		numRight, err := wordToNumber(rightPart, assignments)
		if err != nil {
			return nil, false
		}

		// Check if sum matches
		if sumLeft == numRight {
			// Create a copy of the assignments to return
			solution := make(map[rune]int)
			for k, v := range assignments {
				solution[k] = v
			}
			return solution, true
		}
		return nil, false
	}

	// Try all possible digits for current letter
	currentLetter := letters[pos]
	for digit := 0; digit <= 9; digit++ {
		// Skip used digits
		if usedDigits[digit] {
			continue
		}

		// Skip zero for leading letters
		if leadingSet[currentLetter] && digit == 0 {
			continue
		}

		// Assign the digit
		usedDigits[digit] = true
		assignments[currentLetter] = digit

		// Recurse to next position
		if solution, found := backtrack(letters, leadingSet, pos+1, usedDigits, assignments, leftWords, rightPart); found {
			return solution, true
		}

		// Backtrack
		usedDigits[digit] = false
		delete(assignments, currentLetter)
	}

	return nil, false
}

func wordToNumber(word string, assignments map[rune]int) (int, error) {
	num := 0
	for _, c := range word {
		digit, ok := assignments[c]
		if !ok {
			return 0, fmt.Errorf("letter %c not in assignments", c)
		}
		num = num*10 + digit
	}
	return num, nil
}
