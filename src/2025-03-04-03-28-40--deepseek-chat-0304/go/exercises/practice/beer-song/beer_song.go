package beer

import (
	"errors"
	"fmt"
	"strings"
)

func Verse(n int) (string, error) {
	if n < 0 || n > 99 {
		return "", errors.New("invalid verse number")
	}

	switch n {
	case 0:
		return "No more bottles of beer on the wall, no more bottles of beer.\n" +
			"Go to the store and buy some more, 99 bottles of beer on the wall.\n", nil
	case 1:
		return "1 bottle of beer on the wall, 1 bottle of beer.\n" +
			"Take it down and pass it around, no more bottles of beer on the wall.\n", nil
	case 2:
		return "2 bottles of beer on the wall, 2 bottles of beer.\n" +
			"Take one down and pass it around, 1 bottle of beer on the wall.\n", nil
	default:
		return fmt.Sprintf("%d bottles of beer on the wall, %d bottles of beer.\n"+
			"Take one down and pass it around, %d bottles of beer on the wall.\n",
			n, n, n-1), nil
	}
}

func Verses(start, stop int) (string, error) {
	if start < stop || start < 0 || stop < 0 || start > 99 || stop > 99 {
		return "", errors.New("invalid verse range")
	}

	var verses []string
	for i := start; i >= stop; i-- {
		v, err := Verse(i)
		if err != nil {
			return "", err
		}
		verses = append(verses, v)
	}
	return strings.Join(verses, "\n") + "\n", nil
}

func Song() string {
	song, _ := Verses(99, 0)
	return song
}
