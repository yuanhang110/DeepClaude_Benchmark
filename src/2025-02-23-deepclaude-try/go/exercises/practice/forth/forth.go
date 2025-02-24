package forth

import (
	"errors"
	"strconv"
	"strings"
)

// Dictionary stores word definitions
type Dictionary map[string][]string

// Stack operations
func (d Dictionary) evalToken(stack []int, token string) ([]int, error) {
	token = strings.ToUpper(token)
	
	// Try to parse as number
	if num, err := strconv.Atoi(token); err == nil {
		return append(stack, num), nil
	}

	// Check stack has enough elements
	switch token {
	case "+", "-", "*", "/", "SWAP", "OVER":
		if len(stack) < 2 {
			return nil, errors.New("stack underflow")
		}
	case "DUP", "DROP":
		if len(stack) < 1 {
			return nil, errors.New("stack underflow")
		}
	}

	// Handle operations
	switch token {
	case "+":
		stack[len(stack)-2] = stack[len(stack)-2] + stack[len(stack)-1]
		return stack[:len(stack)-1], nil
	case "-":
		stack[len(stack)-2] = stack[len(stack)-2] - stack[len(stack)-1]
		return stack[:len(stack)-1], nil
	case "*":
		stack[len(stack)-2] = stack[len(stack)-2] * stack[len(stack)-1]
		return stack[:len(stack)-1], nil
	case "/":
		if stack[len(stack)-1] == 0 {
			return nil, errors.New("division by zero")
		}
		stack[len(stack)-2] = stack[len(stack)-2] / stack[len(stack)-1]
		return stack[:len(stack)-1], nil
	case "DUP":
		return append(stack, stack[len(stack)-1]), nil
	case "DROP":
		return stack[:len(stack)-1], nil
	case "SWAP":
		stack[len(stack)-2], stack[len(stack)-1] = stack[len(stack)-1], stack[len(stack)-2]
		return stack, nil
	case "OVER":
		return append(stack, stack[len(stack)-2]), nil
	default:
		if def, exists := d[token]; exists {
			// Add tokens to be processed to avoid recursion
			tokens := make([]string, len(def))
			copy(tokens, def)
			return stack, nil
		}
		return nil, errors.New("undefined operation")
	}
}

func Forth(input []string) ([]int, error) {
	dict := make(Dictionary)
	stack := make([]int, 0)
	
	for _, line := range input {
		tokens := strings.Fields(strings.ToUpper(line))
		
		// Handle word definition
		if len(tokens) > 0 && tokens[0] == ":" {
			if len(tokens) < 4 || tokens[len(tokens)-1] != ";" {
				return nil, errors.New("invalid definition")
			}
			name := tokens[1]
			
			// Check if name is a number
			if _, err := strconv.Atoi(name); err == nil {
				return nil, errors.New("illegal operation")
			}
			
			// Expand definition by replacing defined words
			expanded := make([]string, 0)
			for _, t := range tokens[2:len(tokens)-1] {
				if def, exists := dict[t]; exists {
					expanded = append(expanded, def...)
				} else {
					expanded = append(expanded, t)
				}
			}
			
			// Store expanded definition without ":" and ";"
			dict[name] = expanded
			continue
		}

		// Process all tokens iteratively
		tokensToProcess := make([]string, len(tokens))
		copy(tokensToProcess, tokens)
		
		for len(tokensToProcess) > 0 {
			token := tokensToProcess[0]
			tokensToProcess = tokensToProcess[1:]
			
			// If token is a defined word, prepend its definition to tokens
			if def, exists := dict[token]; exists {
				newTokens := make([]string, len(def)+len(tokensToProcess))
				copy(newTokens, def)
				copy(newTokens[len(def):], tokensToProcess)
				tokensToProcess = newTokens
				continue
			}
			
			// Evaluate the token
			var err error
			stack, err = dict.evalToken(stack, token)
			if err != nil {
				return nil, err
			}
		}
	}

	return stack, nil
}
