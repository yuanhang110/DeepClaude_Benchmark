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
	if start < stop || start > 99 || stop < 0 {
		return "", errors.New("invalid verse range")
	}
	
	var verses strings.Builder
	for i := start; i >= stop; i-- {
		v, err := Verse(i)
		if err != nil {
			return "", err
		}
		if verses.Len() > 0 {
			verses.WriteString("\n\n")
		}
		verses.WriteString(v)
	}
	return verses.String(), nil
}

func Verse(n int) (string, error) {
	if n < 0 || n > 99 {
		return "", errors.New("invalid verse number")
	}
	
	if n == 0 {
		return "No more bottles of beer on the wall, no more bottles of beer.\nGo to the store and buy some more, 99 bottles of beer on the wall.\n", nil
	}
	
	firstLine := fmt.Sprintf("%d %s of beer on the wall, %d %s of beer.\n", n, pluralize(n), n, pluralize(n))
	
	var secondLine string
	if n == 1 {
		secondLine = "Take it down and pass it around, no more bottles of beer on the wall."
	} else {
		next := n - 1
		if next == 1 {
			secondLine = fmt.Sprintf("Take one down and pass it around, 1 bottle of beer on the wall.")
		} else if next == 0 {
			secondLine = "Take one down and pass it around, no more bottles of beer on the wall."
		} else {
			secondLine = fmt.Sprintf("Take one down and pass it around, %d %s of beer on the wall.", next, pluralize(next))
		}
	}
	
	return firstLine + secondLine + "\n", nil
}

func pluralize(n int) string {
	if n == 1 {
		return "bottle"
	}
	return "bottles"
}
