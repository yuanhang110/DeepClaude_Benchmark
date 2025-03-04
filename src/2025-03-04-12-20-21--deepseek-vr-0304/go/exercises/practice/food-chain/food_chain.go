package foodchain

import (
	"strings"
)

type animal struct {
	name     string
	intro    string
	action   string
	previous *animal
}

var animals = []animal{
	{"fly", "I know an old lady who swallowed a fly.", "I don't know why she swallowed the fly. Perhaps she'll die.", nil},
	{"spider", "I know an old lady who swallowed a spider.", "It wriggled and jiggled and tickled inside her.", nil},
	{"bird", "I know an old lady who swallowed a bird.", "How absurd to swallow a bird!", nil},
	{"cat", "I know an old lady who swallowed a cat.", "Imagine that, to swallow a cat!", nil},
	{"dog", "I know an old lady who swallowed a dog.", "What a hog, to swallow a dog!", nil},
	{"goat", "I know an old lady who swallowed a goat.", "Just opened her throat and swallowed a goat!", nil},
	{"cow", "I know an old lady who swallowed a cow.", "I don't know how she swallowed a cow!", nil},
	{"horse", "I know an old lady who swallowed a horse.", "She's dead, of course!", nil},
}

func init() {
	// Set up previous pointers
	for i := 1; i < len(animals); i++ {
		animals[i].previous = &animals[i-1]
	}
}

func Verse(v int) string {
	if v < 1 || v > 8 {
		return ""
	}
	
	a := animals[v-1]
	var lines []string
	
	// Add the intro lines
	lines = append(lines, a.intro)
	
	// Special case for horse
	if a.name == "horse" {
		lines = append(lines, a.action)
		return strings.Join(lines, "\n")
	}
	
	// Add the action line for current animal
	lines = append(lines, a.action)
	
	// Build the swallowed chain
	current := &a
	for current.previous != nil {
		prev := current.previous
		line := "She swallowed the " + current.name + " to catch the " + prev.name
		if prev.name == "spider" {
			line += " that wriggled and jiggled and tickled inside her."
		} else {
			line += "."
		}
		lines = append(lines, line)
		current = current.previous
	}
	
	// Add the ending lines if not fly
	if a.name != "fly" {
		lines = append(lines, animals[0].action)
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
