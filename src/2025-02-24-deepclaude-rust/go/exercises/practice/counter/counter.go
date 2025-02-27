package counter

type MyCounter struct {
    totalLines   int
    totalLetters int
    totalChars   int
}

func NewCounter() Counter {
    return &MyCounter{}
}

func (c *MyCounter) AddString(s string) {
    c.totalChars += len(s)
    for _, r := range s {
        if r == '\n' {
            c.totalLines++
        }
        if (r >= 'a' && r <= 'z') || (r >= 'A' && r <= 'Z') {
            c.totalLetters++
        }
    }
}

func (c *MyCounter) Lines() int {
    if c.totalChars == 0 {
        return 0
    }
    return c.totalLines + 1
}

func (c *MyCounter) Letters() int {
    return c.totalLetters
}

func (c *MyCounter) Characters() int {
    return c.totalChars
}
