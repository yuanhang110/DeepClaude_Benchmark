package beer

import (
	"errors"
	"fmt"
	"strings"
)

func Song() string {
	verses, _ := Verses(99, 0)
	return verses
}

func Verses(start, stop int) (string, error) {
	if start < stop || start > 99 || stop < 0 {
		return "", errors.New("invalid verse range")
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
		return "", errors.New("invalid verse number")
	}

	current := bottleCount(n)
	line1 := fmt.Sprintf("%s of beer on the wall, %s of beer.\n", capitalize(current), current)

	var line2 string
	if n == 0 {
		line2 = "Go to the store and buy some more, 99 bottles of beer on the wall.\n"
	} else {
		action := "Take one down and pass it around"
		if n == 1 {
			action = "Take it down and pass it around"
		}
		next := bottleCount(n-1)
		line2 = fmt.Sprintf("%s, %s of beer on the wall.\n", action, next)
	}

	return line1 + line2, nil
}

func bottleCount(n int) string {
	if n == 0 {
		return "no more bottles"
	}
	return fmt.Sprintf("%d %s", n, pluralize(n))
}

func pluralize(n int) string {
	if n == 1 {
		return "bottle"
	}
	return "bottles"
}

func capitalize(s string) string {
	if s == "" {
		return ""
	}
	return strings.ToUpper(s[:1]) + s[1:]
}
