package counter

import (
	"strings"
	"unicode"
	"unicode/utf8"
)

// counterImpl is an implementation of the Counter interface
type counterImpl struct {
	lines      int
	letters    int
	characters int
}

// Lines returns the number of lines
func (c *counterImpl) Lines() int {
	return c.lines
}

// Letters returns the number of letters
func (c *counterImpl) Letters() int {
	return c.letters
}

// Characters returns the number of characters
func (c *counterImpl) Characters() int {
	return c.characters
}

// AddString processes a string and updates the counter
func (c *counterImpl) AddString(s string) {
	c.lines += len(strings.Split(s, "\n"))
	c.characters += utf8.RuneCountInString(s)
	for _, r := range s {
		if unicode.IsLetter(r) {
			c.letters++
		}
	}
}

// New creates a new Counter
func New() Counter {
	return &counterImpl{}
}
