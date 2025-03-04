package foodchain

import (
	"fmt"
	"strings"
)

var animals = []string{
	"fly",
	"spider",
	"bird",
	"cat",
	"dog",
	"goat",
	"cow",
	"horse",
}

var descriptions = []string{
	"",
	"It wriggled and jiggled and tickled inside her.",
	"How absurd to swallow a bird!",
	"Imagine that, to swallow a cat!",
	"What a hog, to swallow a dog!",
	"Just opened her throat and swallowed a goat!",
	"I don't know how she swallowed a cow!",
	"She's dead, of course!",
}

func Verse(v int) string {
	if v < 1 || v > 8 {
		return ""
	}

	verse := fmt.Sprintf("I know an old lady who swallowed a %s.\n", animals[v-1])
	
	if v == 8 {
		return verse + descriptions[v-1]
	}

	if descriptions[v-1] != "" {
		verse += descriptions[v-1] + "\n"
	}

	for i := v - 1; i > 0; i-- {
		verse += fmt.Sprintf("She swallowed the %s to catch the %s", animals[i], animals[i-1])
		if i == 2 {
			verse += " that wriggled and jiggled and tickled inside her"
		}
		verse += ".\n"
	}

	verse += "I don't know why she swallowed the fly. Perhaps she'll die."
	return verse
}

func Verses(start, end int) string {
	var verses []string
	for i := start; i <= end; i++ {
		verses = append(verses, Verse(i))
	}
	return strings.Join(verses, "\n\n")
}

func Song() string {
	return Verses(1, 8)
}
