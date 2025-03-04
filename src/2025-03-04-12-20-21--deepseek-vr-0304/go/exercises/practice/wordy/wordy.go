package wordy

import (
	"strconv"
	"strings"
)

func Answer(question string) (int, bool) {
	if !strings.HasPrefix(question, "What is ") || !strings.HasSuffix(question, "?") {
		return 0, false
	}
	
	expr := strings.TrimSpace(strings.TrimSuffix(strings.TrimPrefix(question, "What is "), "?"))
	if expr == "" {
		return 0, false
	}

	// Normalize operations to operator symbols
	expr = strings.ReplaceAll(expr, "multiplied by", "*")
	expr = strings.ReplaceAll(expr, "divided by", "/")
	expr = strings.ReplaceAll(expr, "plus", "+")
	expr = strings.ReplaceAll(expr, "minus", "-")

	tokens := strings.Fields(expr)
	if len(tokens) == 0 {
		return 0, false
	}

	// Parse first number
	result, err := strconv.Atoi(tokens[0])
	if err != nil {
		return 0, false
	}

	if len(tokens) == 1 {
		return result, true
	}

	if (len(tokens)-1)%2 != 0 {
		return 0, false // Requires pairs of operator+number
	}

	for i := 1; i < len(tokens); i += 2 {
		op := tokens[i]
		numStr := tokens[i+1]

		num, err := strconv.Atoi(numStr)
		if err != nil {
			return 0, false
		}

		switch op {
		case "+":
			result += num
		case "-":
			result -= num
		case "*":
			result *= num
		case "/":
			if num == 0 {
				return 0, false // Avoid division by zero
			}
			result /= num
		default:
			return 0, false // Invalid operator
		}
	}

	return result, true
}
