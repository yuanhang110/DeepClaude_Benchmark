package wordy

import (
	"strconv"
	"strings"
)

func Answer(question string) (int, bool) {
	if !strings.HasPrefix(question, "What is ") || !strings.HasSuffix(question, "?") {
		return 0, false
	}

	content := strings.TrimSpace(question[8 : len(question)-1])
	if content == "" {
		return 0, false
	}

	words := strings.Fields(content)
	var tokens []string

	for i := 0; i < len(words); {
		word := words[i]
		if word == "multiplied" && i+1 < len(words) && words[i+1] == "by" {
			tokens = append(tokens, "multiplied by")
			i += 2
		} else if word == "divided" && i+1 < len(words) && words[i+1] == "by" {
			tokens = append(tokens, "divided by")
			i += 2
		} else if word == "plus" || word == "minus" {
			tokens = append(tokens, word)
			i++
		} else {
			tokens = append(tokens, word)
			i++
		}
	}

	if len(tokens) == 0 {
		return 0, false
	}

	num, err := strconv.Atoi(tokens[0])
	if err != nil {
		return 0, false
	}

	result := num

	for i := 1; i < len(tokens); i += 2 {
		if i+1 >= len(tokens) {
			return 0, false
		}

		op := tokens[i]
		nextNum, err := strconv.Atoi(tokens[i+1])
		if err != nil {
			return 0, false
		}

		switch op {
		case "plus":
			result += nextNum
		case "minus":
			result -= nextNum
		case "multiplied by":
			result *= nextNum
		case "divided by":
			if nextNum == 0 {
				return 0, false
			}
			result /= nextNum
		default:
			return 0, false
		}
	}

	return result, true
}
