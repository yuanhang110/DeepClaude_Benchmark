package beer

import (
	"errors"
	"fmt"
	"strings"
)

func Song() string {
	s, _ := Verses(99, 0)
	return s
}

func Verses(start, stop int) (string, error) {
	if start < stop {
		return "", errors.New("start must be greater than or equal to stop")
	}
	var verses []string
	for i := start; i >= stop; i-- {
		verse, err := Verse(i)
		if err != nil {
			return "", err
		}
		verses = append(verses, verse)
	}
	return strings.Join(verses, "\n\n") + "\n", nil
}

func Verse(n int) (string, error) {
	if n < 0 || n > 99 {
		return "", errors.New("invalid verse")
	}
	if n == 0 {
		return "No more bottles of beer on the wall, no more bottles of beer.\nGo to the store and buy some more, 99 bottles of beer on the wall.", nil
	}

	action := "Take one down and pass it around"
	if n == 1 {
		action = "Take it down and pass it around"
	}

	next := n - 1
	nextPhrase := nextBottlesPhrase(next)

	line1 := fmt.Sprintf("%s of beer on the wall, %s of beer.", bottlesPhrase(n), bottlesPhrase(n))
	line2 := fmt.Sprintf("%s, %s of beer on the wall.", action, nextPhrase)

	return strings.Join([]string{line1, line2}, "\n") + "\n", nil
}

func bottlesPhrase(n int) string {
	if n == 1 {
		return "1 bottle"
	}
	return fmt.Sprintf("%d bottles", n)
}

func nextBottlesPhrase(next int) string {
	if next == 0 {
		return "no more bottles"
	} else if next == 1 {
		return "1 bottle"
	}
	return fmt.Sprintf("%d bottles", next)
}
