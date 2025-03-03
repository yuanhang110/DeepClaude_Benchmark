package alphametics

import (
	"errors"
	"strings"
)

func Solve(puzzle string) (map[string]int, error) {
	parts := strings.Split(puzzle, "==")
	if len(parts) != 2 {
		return nil, errors.New("invalid puzzle format")
	}
	lhs := strings.TrimSpace(parts[0])
	rhs := strings.TrimSpace(parts[1])
	addends := strings.Split(lhs, "+")
	for i := range addends {
		addends[i] = strings.TrimSpace(addends[i])
	}
	sumWord := rhs

	letters := make([]rune, 0)
	firstLetters := make(map[rune]struct{})
	seen := make(map[rune]struct{})
	for _, word := range append(addends, sumWord) {
		for i, c := range word {
			if _, exists := seen[c]; !exists {
				seen[c] = struct{}{}
				letters = append(letters, c)
			}
			if i == 0 {
				firstLetters[c] = struct{}{}
			}
		}
	}

	if len(letters) > 10 {
		return nil, errors.New("too many unique letters")
	}

	var solution map[rune]int
	used := make([]bool, 10)

	var backtrack func(int, map[rune]int, []bool) bool
	backtrack = func(index int, assignment map[rune]int, used []bool) bool {
		if index == len(letters) {
			sumTotal := 0
			for _, addend := range addends {
				sumTotal += wordToNum(addend, assignment)
			}
			sumResult := wordToNum(sumWord, assignment)
			if sumTotal == sumResult {
				solution = make(map[rune]int)
				for k, v := range assignment {
					solution[k] = v
				}
				return true
			}
			return false
		}

		currentLetter := letters[index]
		for digit := 0; digit <= 9; digit++ {
			if used[digit] {
				continue
			}
			if digit == 0 {
				if _, ok := firstLetters[currentLetter]; ok {
					continue
				}
			}
			assignment[currentLetter] = digit
			used[digit] = true
			if backtrack(index+1, assignment, used) {
				return true
			}
			delete(assignment, currentLetter)
			used[digit] = false
		}
		return false
	}

	assignment := make(map[rune]int)
	if backtrack(0, assignment, used) {
		result := make(map[string]int)
		for k, v := range solution {
			result[string(k)] = v
		}
		return result, nil
	}

	return nil, errors.New("no solution found")
}

func wordToNum(word string, assignment map[rune]int) int {
	num := 0
	for _, c := range word {
		num = num*10 + assignment[c]
	}
	return num
}
