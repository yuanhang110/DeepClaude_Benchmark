package foodchain

import (
	"fmt"
	"strings"
)

var animals = [...]struct {
	name  string
	verse string
	chain string
}{
	{ // verse 1 - fly
		name:  "fly",
		verse: "I don't know why she swallowed the fly. Perhaps she'll die.",
		chain: "fly",
	},
	{ // verse 2 - spider
		name:  "spider",
		verse: "It wriggled and jiggled and tickled inside her.",
		chain: "spider that wriggled and jiggled and tickled inside her",
	},
	{ // verse 3 - bird
		name:  "bird",
		verse: "How absurd to swallow a bird!",
		chain: "bird",
	},
	{ // verse 4 - cat
		name:  "cat",
		verse: "Imagine that, to swallow a cat!",
		chain: "cat",
	},
	{ // verse 5 - dog
		name:  "dog",
		verse: "What a hog, to swallow a dog!",
		chain: "dog",
	},
	{ // verse 6 - goat
		name:  "goat",
		verse: "Just opened her throat and swallowed a goat!",
		chain: "goat",
	},
	{ // verse 7 - cow
		name:  "cow",
		verse: "I don't know how she swallowed a cow!",
		chain: "cow",
	},
	{ // verse 8 - horse
		name:  "horse",
		verse: "She's dead, of course!",
		chain: "",
	},
}

func Verse(v int) string {
	if v < 1 || v > 8 {
		return ""
	}
	if v == 8 {
		return "I know an old lady who swallowed a horse.\nShe's dead, of course!"
	}

	index := v - 1
	animal := animals[index]

	lines := []string{
		fmt.Sprintf("I know an old lady who swallowed a %s.", animal.name),
	}

	if v == 1 {
		lines = append(lines, animal.verse)
	} else {
		lines = append(lines, animal.verse)

		// Build the chain from current index down to 1 (spider)
		for j := index; j >= 1; j-- {
			prev := animals[j-1]
			lines = append(lines, fmt.Sprintf("She swallowed the %s to catch the %s.", animals[j].name, prev.chain))
		}

		// Add the fly line
		lines = append(lines, animals[0].verse)
	}

	return strings.Join(lines, "\n")
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
