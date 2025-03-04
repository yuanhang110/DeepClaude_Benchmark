package wordy

import (
	"strconv"
	"strings"
)

func Answer(question string) (int, bool) {
	// Check if question starts with "What is" and ends with "?"
	if !strings.HasPrefix(question, "What is ") || !strings.HasSuffix(question, "?") {
		return 0, false
	}

	// Remove prefix and suffix
	expr := strings.TrimPrefix(question, "What is ")
	expr = strings.TrimSuffix(expr, "?")

	// Split into tokens
	tokens := strings.Fields(expr)
	if len(tokens) == 0 {
		return 0, false
	}

	// Parse first number
	first, err := strconv.Atoi(tokens[0])
	if err != nil {
		return 0, false
	}

	result := first
	tokens = tokens[1:]

	for len(tokens) > 0 {
		if len(tokens) < 2 {
			return 0, false
		}

		// Get operation
		op := tokens[0]
		// Handle "multiplied by" and "divided by" as two tokens
		if len(tokens) > 1 && (op == "multiplied" || op == "divided") {
			if tokens[1] != "by" {
				return 0, false
			}
			// Skip "by" token
			tokens = tokens[1:]
		}
		// Get next number
		numStr := tokens[1]
		num, err := strconv.Atoi(numStr)
		if err != nil {
			return 0, false
		}

		// Perform operation
		switch op {
		case "plus":
			result += num
		case "minus":
			result -= num
		case "multiplied":
			result *= num
		case "divided":
			if num == 0 {
				return 0, false
			}
			result /= num
		default:
			return 0, false
		}

		tokens = tokens[2:]
	}

	return result, true
}
