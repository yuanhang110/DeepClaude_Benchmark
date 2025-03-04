package counter

import "unicode"

type counterImpl struct {
	lines    int
	letters int
	chars    int
}

func NewCounter() Counter {
	return &counterImpl{}
}

func (c *counterImpl) AddString(s string) {
	for _, r := range s {
		c.chars++
		if r == '\n' {
			c.lines++
		}
		if unicode.IsLetter(r) {
			c.letters++
		}
	}
}

func (c *counterImpl) Lines() int {
	return c.lines
}

func (c *counterImpl) Letters() int {
	return c.letters
}

func (c *counterImpl) Characters() int {
	return c.chars
}
