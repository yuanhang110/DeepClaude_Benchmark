package forth

import (
	"errors"
	"fmt"
	"strconv"
	"strings"
)

type stack struct {
	data []int
}

func (s *stack) push(v int) {
	s.data = append(s.data, v)
}

func (s *stack) pop() (int, error) {
	if len(s.data) == 0 {
		return 0, errors.New("stack underflow")
	}
	v := s.data[len(s.data)-1]
	s.data = s.data[:len(s.data)-1]
	return v, nil
}

func (s *stack) peek() (int, error) {
	if len(s.data) == 0 {
		return 0, errors.New("insufficient elements")
	}
	return s.data[len(s.data)-1], nil
}

func (s *stack) dup() error {
	v, err := s.peek()
	if err != nil {
		return err
	}
	s.push(v)
	return nil
}

func (s *stack) swap() error {
	if len(s.data) < 2 {
		return errors.New("insufficient elements")
	}
	a, _ := s.pop()
	b, _ := s.pop()
	s.push(a)
	s.push(b)
	return nil
}

func (s *stack) over() error {
	if len(s.data) < 2 {
		return errors.New("insufficient elements")
	}
	v := s.data[len(s.data)-2]
	s.push(v)
	return nil
}

var builtins = map[string]func(*stack) error{
	"+": func(s *stack) error {
		a, err := s.pop()
		if err != nil {
			return err
		}
		b, err := s.pop()
		if err != nil {
			return err
		}
		s.push(b + a)
		return nil
	},
	"-": func(s *stack) error {
		a, err := s.pop()
		if err != nil {
			return err
		}
		b, err := s.pop()
		if err != nil {
			return err
		}
		s.push(b - a)
		return nil
	},
	"*": func(s *stack) error {
		a, err := s.pop()
		if err != nil {
			return err
		}
		b, err := s.pop()
		if err != nil {
			return err
		}
		s.push(b * a)
		return nil
	},
	"/": func(s *stack) error {
		a, err := s.pop()
		if err != nil {
			return err
		}
		if a == 0 {
			return errors.New("division by zero")
		}
		b, err := s.pop()
		if err != nil {
			return err
		}
		s.push(b / a)
		return nil
	},
	"DUP":  func(s *stack) error { return s.dup() },
	"DROP": func(s *stack) error { _, err := s.pop(); return err },
	"SWAP": func(s *stack) error { return s.swap() },
	"OVER": func(s *stack) error { return s.over() },
}

func Forth(input []string) ([]int, error) {
	s := &stack{}
	customWords := make(map[string][]string)

	for _, line := range input {
		tokens := tokenize(line)
		if len(tokens) == 0 {
			continue
		}

		// Expand custom words and process tokens
		processed := []string{}
		for i := 0; i < len(tokens); i++ {
			token := tokens[i]
			upperToken := strings.ToUpper(token)

			if upperToken == ":" {
				// Handle word definition
				if i+3 > len(tokens) {
					return nil, errors.New("invalid word definition")
				}
				wordName := strings.ToUpper(tokens[i+1])
				if _, err := strconv.Atoi(wordName); err == nil {
					return nil, fmt.Errorf("invalid word name: %s", wordName)
				}
				end := i + 2
				for ; end < len(tokens) && tokens[end] != ";"; end++ {
				}
				if end >= len(tokens) {
					return nil, errors.New("unterminated word definition")
				}
				processedDef := make([]string, 0, len(tokens[i+2:end]))
				for _, t := range tokens[i+2 : end] {
					if _, err := strconv.Atoi(t); err == nil {
						processedDef = append(processedDef, t)
					} else {
						processedDef = append(processedDef, strings.ToUpper(t))
					}
				}
				customWords[wordName] = processedDef
				i = end // skip to end of definition
				continue
			}

			if def, ok := customWords[upperToken]; ok {
				processed = append(processed, def...)
			} else {
				processed = append(processed, upperToken)
			}
		}

		// Evaluate processed tokens
		for _, token := range processed {
			if num, err := strconv.Atoi(token); err == nil {
				s.push(num)
				continue
			}

			op, ok := builtins[token]
			if !ok {
				return nil, fmt.Errorf("undefined word: %s", token)
			}

			if err := op(s); err != nil {
				return nil, err
			}
		}
	}

	return s.data, nil
}

func tokenize(line string) []string {
	var tokens []string
	var current strings.Builder

	for _, r := range line {
		if r == ' ' {
			if current.Len() > 0 {
				tokens = append(tokens, current.String())
				current.Reset()
			}
		} else {
			current.WriteRune(r)
		}
	}
	if current.Len() > 0 {
		tokens = append(tokens, current.String())
	}

	return tokens
}
