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

	words := strings.Fields(expr)
	var tokens []string

	for i := 0; i < len(words); {
		// Check for two-word operators first
		if i+1 < len(words) {
			if (words[i] == "multiplied" && words[i+1] == "by") || (words[i] == "divided" && words[i+1] == "by") {
				tokens = append(tokens, words[i]+" "+words[i+1])
				i += 2
				continue
			}
		}

		// Check single-word operators
		if words[i] == "plus" || words[i] == "minus" {
			tokens = append(tokens, words[i])
			i++
			continue
		}

		// Check if it's a number
		if _, err := strconv.Atoi(words[i]); err == nil {
			tokens = append(tokens, words[i])
			i++
			continue
		}

		// Invalid token
		return 0, false
	}

	if len(tokens) == 0 {
		return 0, false
	}

	// Validate and parse initial number
	initialNum, err := strconv.Atoi(tokens[0])
	if err != nil {
		return 0, false
	}

	if len(tokens) == 1 {
		return initialNum, true
	}

	// Validate operator-number pairs
	for i := 1; i < len(tokens); i += 2 {
		// Check operator validity
		op := tokens[i]
		if op != "plus" && op != "minus" && op != "multiplied by" && op != "divided by" {
			return 0, false
		}

		// Check number existence and validity
		if i+1 >= len(tokens) {
			return 0, false
		}
		numStr := tokens[i+1]
		if _, err := strconv.Atoi(numStr); err != nil {
			return 0, false
		}
	}

	// Perform calculations
	result := initialNum
	for i := 1; i < len(tokens); i += 2 {
		op := tokens[i]
		num, _ := strconv.Atoi(tokens[i+1]) // already validated above

		switch op {
		case "plus":
			result += num
		case "minus":
			result -= num
		case "multiplied by":
			result *= num
		case "divided by":
			result /= num
		}
	}

	return result, true
}
