package beer

import (
	"fmt"
	"strings"
)

func Song() string {
	verses, _ := Verses(99, 0)
	return verses
}

func Verses(start, stop int) (string, error) {
	if start < stop {
		return "", fmt.Errorf("start must be >= stop")
	}
	if start > 99 || stop < 0 {
		return "", fmt.Errorf("verse number out of range")
	}

	var verses []string
	for n := start; n >= stop; n-- {
		v, err := Verse(n)
		if err != nil {
			return "", err
		}
		verses = append(verses, v)
	}

	return strings.Join(verses, "\n\n") + "\n", nil
}

func Verse(n int) (string, error) {
	if n < 0 || n > 99 {
		return "", fmt.Errorf("invalid verse number %d", n)
	}

	var first, second string

	switch {
	case n == 0:
		first = "No more bottles of beer on the wall, no more bottles of beer."
		second = "Go to the store and buy some more, 99 bottles of beer on the wall."
	case n == 1:
		first = "1 bottle of beer on the wall, 1 bottle of beer."
		second = "Take it down and pass it around, no more bottles of beer on the wall."
	default:
		first = fmt.Sprintf("%d bottles of beer on the wall, %d bottles of beer.", n, n)
		
		remaining := n - 1
		bottles := "bottles"
		if remaining == 1 {
			bottles = "bottle"
		}
		action := "one"
		if n == 1 {
			action = "it"
		}
		
		if remaining > 0 {
			second = fmt.Sprintf("Take %s down and pass it around, %d %s of beer on the wall.", action, remaining, bottles)
		} else {
			second = "Take one down and pass it around, no more bottles of beer on the wall."
		}
	}

	return fmt.Sprintf("%s\n%s\n", first, second), nil
}
