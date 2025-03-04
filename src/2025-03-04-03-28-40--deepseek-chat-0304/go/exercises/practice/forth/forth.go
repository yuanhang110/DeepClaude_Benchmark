package forth

import (
	"errors"
	"strconv"
	"strings"
)

type stack []int

func (s *stack) push(v int) {
	*s = append(*s, v)
}

func (s *stack) pop() (int, error) {
	if len(*s) == 0 {
		return 0, errors.New("stack underflow")
	}
	v := (*s)[len(*s)-1]
	*s = (*s)[:len(*s)-1]
	return v, nil
}

func (s *stack) peek() (int, error) {
	if len(*s) == 0 {
		return 0, errors.New("stack underflow")
	}
	return (*s)[len(*s)-1], nil
}

func Forth(input []string) ([]int, error) {
	var s stack
	words := make(map[string][]string)

	for _, line := range input {
		tokens := strings.Fields(line)
		i := 0
		for i < len(tokens) {
			token := strings.ToUpper(tokens[i])
			switch {
			case token == ":":
				// Word definition
				if i+2 >= len(tokens) || tokens[i+1] == ";" {
					return nil, errors.New("invalid word definition")
				}
				wordName := strings.ToUpper(tokens[i+1])
				if _, err := strconv.Atoi(wordName); err == nil {
					return nil, errors.New("invalid word name")
				}
				i += 2
				var definition []string
				for i < len(tokens) && tokens[i] != ";" {
					definition = append(definition, tokens[i])
					i++
				}
				if i >= len(tokens) {
					return nil, errors.New("unterminated word definition")
				}
				words[wordName] = definition
				i++
			case token == "+", token == "-", token == "*", token == "/":
				// Arithmetic operations
				b, err := s.pop()
				if err != nil {
					return nil, err
				}
				a, err := s.pop()
				if err != nil {
					return nil, err
				}
				var result int
				switch token {
				case "+":
					result = a + b
				case "-":
					result = a - b
				case "*":
					result = a * b
				case "/":
					if b == 0 {
						return nil, errors.New("division by zero")
					}
					result = a / b
				}
				s.push(result)
				i++
			case token == "DUP":
				val, err := s.peek()
				if err != nil {
					return nil, err
				}
				s.push(val)
				i++
			case token == "DROP":
				_, err := s.pop()
				if err != nil {
					return nil, err
				}
				i++
			case token == "SWAP":
				b, err := s.pop()
				if err != nil {
					return nil, err
				}
				a, err := s.pop()
				if err != nil {
					return nil, err
				}
				s.push(b)
				s.push(a)
				i++
			case token == "OVER":
				if len(s) < 2 {
					return nil, errors.New("not enough elements for OVER")
				}
				s.push(s[len(s)-2])
				i++
			default:
				// Check if it's a number
				if num, err := strconv.Atoi(token); err == nil {
					s.push(num)
					i++
				} else if def, ok := words[token]; ok {
					// User-defined word - expand it into new tokens
					newTokens := make([]string, 0, len(tokens)-1+len(def))
					newTokens = append(newTokens, tokens[:i]...)
					newTokens = append(newTokens, def...)
					newTokens = append(newTokens, tokens[i+1:]...)
					tokens = newTokens
				} else {
					return nil, errors.New("unknown word")
				}
			}
		}
	}

	return s, nil
}
