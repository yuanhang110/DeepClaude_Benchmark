package foodchain

import (
	"fmt"
	"strings"
)

type animal struct {
	name   string
	intro  string
	suffix string
}

var animals = []animal{
	{"fly", "I don't know why she swallowed the fly. Perhaps she'll die.", ""},
	{"spider", "It wriggled and jiggled and tickled inside her.", " that wriggled and jiggled and tickled inside her"},
	{"bird", "How absurd to swallow a bird!", ""},
	{"cat", "Imagine that, to swallow a cat!", ""},
	{"dog", "What a hog, to swallow a dog!", ""},
	{"goat", "Just opened her throat and swallowed a goat!", ""},
	{"cow", "I don't know how she swallowed a cow!", ""},
	{"horse", "She's dead, of course!", ""},
}

func Verse(v int) string {
	if v == 8 {
		return "I know an old lady who swallowed a horse.\n" + 
			animals[7].intro
	}

	var lines []string
	lines = append(lines, fmt.Sprintf("I know an old lady who swallowed a %s.", animals[v-1].name))
	lines = append(lines, animals[v-1].intro)

	for i := v - 1; i > 0; i-- {
		lines = append(lines, fmt.Sprintf(
			"She swallowed the %s to catch the %s%s.",
			animals[i].name,
			animals[i-1].name,
			animals[i-1].suffix,
		))
	}

	if v > 1 {
		lines = append(lines, animals[0].intro)
	}

	return strings.Join(lines, "\n")
}

func Verses(start, end int) string {
	var verses []string
	for i := start; i <= end; i++ {
		verses = append(verses, Verse(i))
	}
	return strings.Join(verses, "\n")
}

func Song() string {
	return Verses(1, 8)
}
