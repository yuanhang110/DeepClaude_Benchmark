package wordy

import (
	"strconv"
	"strings"
)

func Answer(question string) (int, bool) {
	// Check question format
	if !strings.HasPrefix(question, "What is ") || !strings.HasSuffix(question, "?") {
		return 0, false
	}

	// Extract body and handle empty case
	body := strings.TrimSpace(strings.TrimSuffix(strings.TrimPrefix(question, "What is "), "?"))
	if body == "" {
		return 0, false
	}

	// Normalize operations to single tokens
	body = strings.ReplaceAll(body, "multiplied by", "multiply")
	body = strings.ReplaceAll(body, "divided by", "divide")

	tokens := strings.Split(body, " ")

	// Validate token count and pattern
	if len(tokens) != 1 && len(tokens)%2 == 0 {
		return 0, false
	}

	var result int
	for i, token := range tokens {
		if i%2 == 0 { // Number position
			num, err := strconv.Atoi(token)
			if err != nil {
				return 0, false
			}

			if i == 0 {
				result = num
			} else {
				op := tokens[i-1]
				switch op {
				case "plus":
					result += num
				case "minus":
					result -= num
				case "multiply":
					result *= num
				case "divide":
					if num == 0 {
						return 0, false // Division by zero
					}
					result /= num
				default:
					return 0, false // Invalid operation
				}
			}
		} else { // Operation position
			if token != "plus" && token != "minus" && token != "multiply" && token != "divide" {
				return 0, false
			}
		}
	}

	return result, true
}
