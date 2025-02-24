package counter

import "unicode"

type counterImpl struct {
	lines     int
	letters  int
	characters int
}

// New creates a new Counter instance
func New() *counterImpl {
	return &counterImpl{}
}

func (c *counterImpl) AddString(s string) {
	for _, r := range s {
		if r == '\n' {
			c.lines++
		}
		if unicode.IsLetter(r) {
			c.letters++
		}
		c.characters++
	}
}

func (c *counterImpl) Lines() int {
	return c.lines
}

func (c *counterImpl) Letters() int {
	return c.letters
}

func (c *counterImpl) Characters() int {
	return c.characters
}
