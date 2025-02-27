package foodchain

import (
	"fmt"
	"strings"
)

type animal struct {
	name       string
	secondLine string
	chainName  string
}

var animals = []animal{
	{"fly", "I don't know why she swallowed the fly. Perhaps she'll die.", "fly"},
	{"spider", "It wriggled and jiggled and tickled inside her.", "spider that wriggled and jiggled and tickled inside her"},
	{"bird", "How absurd to swallow a bird!", "bird"},
	{"cat", "Imagine that, to swallow a cat!", "cat"},
	{"dog", "What a hog, to swallow a dog!", "dog"},
	{"goat", "Just opened her throat and swallowed a goat!", "goat"},
	{"cow", "I don't know how she swallowed a cow!", "cow"},
	{"horse", "She's dead, of course!", ""},
}

func Verse(v int) string {
	if v < 1 || v > 8 {
		return ""
	}
	if v == 8 {
		return "I know an old lady who swallowed a horse.\nShe's dead, of course!"
	}
	i := v - 1
	a := animals[i]
	lines := []string{
		fmt.Sprintf("I know an old lady who swallowed a %s.", a.name),
		a.secondLine,
	}
	if v > 1 {
		for j := i; j >= 1; j-- {
			prev := animals[j-1]
			lines = append(lines, fmt.Sprintf("She swallowed the %s to catch the %s.", animals[j].name, prev.chainName))
		}
		lines = append(lines, animals[0].secondLine)
	}
	return strings.Join(lines, "\n")
}

func Verses(start, end int) string {
	var vs []string
	for v := start; v <= end; v++ {
		vs = append(vs, Verse(v))
	}
	return strings.Join(vs, "\n\n")
}

func Song() string {
	return Verses(1, 8)
}
