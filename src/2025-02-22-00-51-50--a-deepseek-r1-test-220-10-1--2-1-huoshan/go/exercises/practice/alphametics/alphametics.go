package alphametics

import (
	"fmt"
	"sort"
	"strings"
)

func Solve(puzzle string) (map[string]int, error) {
	addends, sumStr, letters, leading, err := parsePuzzle(puzzle)
	if err != nil {
		return nil, err
	}

	sort.Strings(letters) // Ensure consistent processing order

	var used [10]bool
	solution, ok := backtrack(letters, 0, make(map[string]int), used, leading, addends, sumStr)
	if !ok {
		return nil, fmt.Errorf("no solution")
	}
	return solution, nil
}

func parsePuzzle(puzzle string) ([]string, string, []string, map[string]bool, error) {
	parts := strings.Split(puzzle, "==")
	if len(parts) != 2 {
		return nil, "", nil, nil, fmt.Errorf("invalid puzzle format")
	}

	left := strings.TrimSpace(parts[0])
	sumStr := strings.TrimSpace(parts[1])
	if sumStr == "" {
		return nil, "", nil, nil, fmt.Errorf("missing sum part")
	}

	addends := strings.Split(left, "+")
	for i := range addends {
		addends[i] = strings.TrimSpace(addends[i])
		if addends[i] == "" {
			return nil, "", nil, nil, fmt.Errorf("empty addend")
		}
	}

	letters := make(map[rune]bool)
	leading := make(map[string]bool)

	// Process addends and collect letters
	for _, word := range addends {
		if len(word) == 0 {
			return nil, "", nil, nil, fmt.Errorf("empty word in addends")
		}
		leading[string(word[0])] = true
		for _, c := range word {
			letters[c] = true
		}
	}

	// Process sum
	if len(sumStr) == 0 {
		return nil, "", nil, nil, fmt.Errorf("empty sum")
	}
	leading[string(sumStr[0])] = true
	for _, c := range sumStr {
		letters[c] = true
	}

	// Convert to sorted slice
	uniqueLetters := make([]string, 0, len(letters))
	for c := range letters {
		uniqueLetters = append(uniqueLetters, string(c))
	}
	sort.Strings(uniqueLetters)

	return addends, sumStr, uniqueLetters, leading, nil
}

func backtrack(
	letters []string,
	index int,
	assignment map[string]int,
	used [10]bool,
	leading map[string]bool,
	addends []string,
	sumStr string,
) (map[string]int, bool) {
	if index == len(letters) {
		if isValid(assignment, addends, sumStr) {
			// Copy the solution
			solution := make(map[string]int, len(assignment))
			for k, v := range assignment {
				solution[k] = v
			}
			return solution, true
		}
		return nil, false
	}

	currentLetter := letters[index]
	for digit := 0; digit <= 9; digit++ {
		if used[digit] {
			continue
		}
		if digit == 0 && leading[currentLetter] {
			continue // Skip leading zero
		}

		// Create copies for the recursive call
		newAssignment := make(map[string]int, len(assignment)+1)
		for k, v := range assignment {
			newAssignment[k] = v
		}
		newAssignment[currentLetter] = digit

		newUsed := used
		newUsed[digit] = true

		if solution, ok := backtrack(letters, index+1, newAssignment, newUsed, leading, addends, sumStr); ok {
			return solution, true
		}
	}

	return nil, false
}

func isValid(assignment map[string]int, addends []string, sumStr string) bool {
	sum := 0
	for _, word := range addends {
		num := 0
		for _, c := range word {
			num = num*10 + assignment[string(c)]
		}
		sum += num
	}

	sumValue := 0
	for _, c := range sumStr {
		sumValue = sumValue*10 + assignment[string(c)]
	}

	return sum == sumValue
}
