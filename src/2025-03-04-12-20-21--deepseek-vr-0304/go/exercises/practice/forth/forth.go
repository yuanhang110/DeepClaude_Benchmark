package forth

import (
	"fmt"
	"strconv"
	"strings"
)

func Forth(input []string) ([]int, error) {
	stack := []int{}
	customWords := make(map[string][]string)

	for _, line := range input {
		tokens := strings.Fields(line)
		if len(tokens) == 0 {
			continue
		}

		if strings.ToLower(tokens[0]) == ":" {
			err := processDefinition(tokens, customWords)
			if err != nil {
				return nil, err
			}
			continue
		}

		err := evaluateTokens(tokens, customWords, &stack)
		if err != nil {
			return nil, err
		}
	}

	return stack, nil
}

func processDefinition(tokens []string, customWords map[string][]string) error {
	if len(tokens) < 4 || strings.ToLower(tokens[len(tokens)-1]) != ";" {
		return fmt.Errorf("invalid definition")
	}

	wordName := strings.ToLower(tokens[1])
	if _, err := strconv.Atoi(wordName); err == nil {
		return fmt.Errorf("invalid word name")
	}

	definition := tokens[2 : len(tokens)-1]
	customWords[wordName] = definition
	return nil
}

func evaluateTokens(tokens []string, customWords map[string][]string, stack *[]int) error {
	for _, token := range tokens {
		tokenLower := strings.ToLower(token)
		if num, err := strconv.Atoi(token); err == nil {
			*stack = append(*stack, num)
			continue
		}

		if def, ok := customWords[tokenLower]; ok {
			err := evaluateTokens(def, customWords, stack)
			if err != nil {
				return err
			}
			continue
		}

		switch tokenLower {
		case "+", "-", "*", "/":
			err := arithmeticOp(tokenLower, stack)
			if err != nil {
				return err
			}
		case "dup":
			if len(*stack) < 1 {
				return fmt.Errorf("stack underflow")
			}
			*stack = append(*stack, (*stack)[len(*stack)-1])
		case "drop":
			if len(*stack) < 1 {
				return fmt.Errorf("stack underflow")
			}
			*stack = (*stack)[:len(*stack)-1]
		case "swap":
			if len(*stack) < 2 {
				return fmt.Errorf("stack underflow")
			}
			(*stack)[len(*stack)-2], (*stack)[len(*stack)-1] = (*stack)[len(*stack)-1], (*stack)[len(*stack)-2]
		case "over":
			if len(*stack) < 2 {
				return fmt.Errorf("stack underflow")
			}
			*stack = append(*stack, (*stack)[len(*stack)-2])
		default:
			return fmt.Errorf("undefined word %s", token)
		}
	}
	return nil
}

func arithmeticOp(op string, stack *[]int) error {
	if len(*stack) < 2 {
		return fmt.Errorf("stack underflow")
	}
	b := (*stack)[len(*stack)-1]
	a := (*stack)[len(*stack)-2]
	*stack = (*stack)[:len(*stack)-2]

	var result int
	switch op {
	case "+":
		result = a + b
	case "-":
		result = a - b
	case "*":
		result = a * b
	case "/":
		if b == 0 {
			return fmt.Errorf("division by zero")
		}
		result = a / b
	default:
		return fmt.Errorf("invalid operator")
	}
	*stack = append(*stack, result)
	return nil
}
