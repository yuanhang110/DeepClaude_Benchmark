package forth

import (
	"errors"
	"fmt"
	"strconv"
	"strings"
)

func Forth(input []string) ([]int, error) {
	stack := []int{}
	customWords := make(map[string][]string)

	for _, line := range input {
		line = strings.TrimSpace(line)
		if line == "" {
			continue
		}

		if strings.HasPrefix(line, ":") {
			parts := strings.Fields(line)
			if len(parts) < 4 || parts[0] != ":" || parts[len(parts)-1] != ";" {
				return nil, errors.New("invalid definition")
			}
			wordName := strings.ToLower(parts[1])
			if _, err := strconv.Atoi(wordName); err == nil {
				return nil, errors.New("word name cannot be a number")
			}
			definition := parts[2 : len(parts)-1]
			for _, token := range definition {
				if strings.ToLower(token) == wordName {
					return nil, errors.New("recursive definition")
				}
			}
			customWords[wordName] = definition
		} else {
			tokens := strings.Fields(line)
			processingQueue := tokens

			for len(processingQueue) > 0 {
				token := processingQueue[0]
				processingQueue = processingQueue[1:]

				tokenLower := strings.ToLower(token)
				if num, err := strconv.Atoi(token); err == nil {
					stack = append(stack, num)
					continue
				}

				if def, ok := customWords[tokenLower]; ok {
					processingQueue = append(def, processingQueue...)
					continue
				}

				switch tokenLower {
				case "+":
					if len(stack) < 2 {
						return nil, errors.New("stack underflow")
					}
					a, b := stack[len(stack)-1], stack[len(stack)-2]
					stack = stack[:len(stack)-2]
					stack = append(stack, b+a)
				case "-":
					if len(stack) < 2 {
						return nil, errors.New("stack underflow")
					}
					a, b := stack[len(stack)-1], stack[len(stack)-2]
					stack = stack[:len(stack)-2]
					stack = append(stack, b-a)
				case "*":
					if len(stack) < 2 {
						return nil, errors.New("stack underflow")
					}
					a, b := stack[len(stack)-1], stack[len(stack)-2]
					stack = stack[:len(stack)-2]
					stack = append(stack, b*a)
				case "/":
					if len(stack) < 2 {
						return nil, errors.New("stack underflow")
					}
					a := stack[len(stack)-1]
					if a == 0 {
						return nil, errors.New("division by zero")
					}
					b := stack[len(stack)-2]
					stack = stack[:len(stack)-2]
					stack = append(stack, b/a)
				case "dup":
					if len(stack) < 1 {
						return nil, errors.New("stack underflow")
					}
					stack = append(stack, stack[len(stack)-1])
				case "drop":
					if len(stack) < 1 {
						return nil, errors.New("stack underflow")
					}
					stack = stack[:len(stack)-1]
				case "swap":
					if len(stack) < 2 {
						return nil, errors.New("stack underflow")
					}
					stack[len(stack)-1], stack[len(stack)-2] = stack[len(stack)-2], stack[len(stack)-1]
				case "over":
					if len(stack) < 2 {
						return nil, errors.New("stack underflow")
					}
					stack = append(stack, stack[len(stack)-2])
				default:
					return nil, fmt.Errorf("undefined word: %s", token)
				}
			}
		}
	}

	return stack, nil
}
