package counter

import (
	"strings"
	"unicode"
)

type counterImpl struct {
	totalNewlines   int
	totalLetters    int
	totalCharacters int
}

func New() Counter {
	return &counterImpl{}
}

func (c *counterImpl) AddString(s string) {
	c.totalCharacters += len(s)
	c.totalNewlines += strings.Count(s, "\n")
	for _, r := range s {
		if unicode.IsLetter(r) {
			c.totalLetters++
		}
	}
}

func (c *counterImpl) Lines() int {
	if c.totalCharacters == 0 {
		return 0
	}
	return c.totalNewlines + 1
}

func (c *counterImpl) Letters() int {
	return c.totalLetters
}

func (c *counterImpl) Characters() int {
	return c.totalCharacters
}
