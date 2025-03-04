package counter

import (
	"unicode"
)

// Impl1 implements the Counter interface
type Impl1 struct {
	lineCount    int
	letterCount  int
	charCount    int
}

func NewCounter() Counter {
	return &Impl1{}
}

func (c *Impl1) AddString(s string) {
	for _, r := range s {
		c.charCount++
		if unicode.IsLetter(r) {
			c.letterCount++
		}
		if r == '\n' {
			c.lineCount++
		}
	}
	// If the string ends without a newline, it's still a line
	if len(s) > 0 && !hasFinalNewline(s) {
		c.lineCount++
	}
}

func (c *Impl1) Lines() int {
	return c.lineCount
}

func (c *Impl1) Letters() int {
	return c.letterCount
}

func (c *Impl1) Characters() int {
	return c.charCount
}

func hasFinalNewline(s string) bool {
	return len(s) > 0 && s[len(s)-1] == '\n'
}
