package wordy

import (
	"strconv"
	"strings"
)

func Answer(question string) (int, bool) {
	// Check if the question follows the expected format
	if !strings.HasPrefix(question, "What is ") || !strings.HasSuffix(question, "?") {
		return 0, false
	}

	// Extract the actual question content
	q := strings.TrimSpace(question[8 : len(question)-1])
	if q == "" {
		return 0, false
	}

	// Split into words
	words := strings.Fields(q)
	if len(words) == 0 {
		return 0, false
	}

	// Parse initial number
	result, err := strconv.Atoi(words[0])
	if err != nil {
		return 0, false
	}

	i := 1
	for i < len(words) {
		// Check for operator
		op := words[i]
		switch op {
		case "plus", "minus":
			// Single-word operators
			if i+1 >= len(words) {
				return 0, false // Missing number after operator
			}
			num, err := strconv.Atoi(words[i+1])
			if err != nil {
				return 0, false // Not a number
			}
			if op == "plus" {
				result += num
			} else {
				result -= num
			}
			i += 2
		case "multiplied", "divided":
			// Two-word operators
			if i+2 >= len(words) || words[i+1] != "by" {
				return 0, false // Invalid two-word operator or missing number
			}
			num, err := strconv.Atoi(words[i+2])
			if err != nil {
				return 0, false // Not a number
			}
			if op == "multiplied" {
				result *= num
			} else {
				if num == 0 {
					return 0, false // Avoid division by zero
				}
				result /= num
			}
			i += 3
		default:
			return 0, false // Unsupported operation
		}
	}

	return result, true
}
