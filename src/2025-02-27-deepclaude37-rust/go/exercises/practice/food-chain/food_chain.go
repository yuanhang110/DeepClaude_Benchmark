package foodchain

import (
	"fmt"
	"strings"
)

// Define the animals and their corresponding second lines
var animals = []struct {
	name       string
	secondLine string
}{
	{name: "fly", secondLine: ""},
	{name: "spider", secondLine: "It wriggled and jiggled and tickled inside her."},
	{name: "bird", secondLine: "How absurd to swallow a bird!"},
	{name: "cat", secondLine: "Imagine that, to swallow a cat!"},
	{name: "dog", secondLine: "What a hog, to swallow a dog!"},
	{name: "goat", secondLine: "Just opened her throat and swallowed a goat!"},
	{name: "cow", secondLine: "I don't know how she swallowed a cow!"},
	{name: "horse", secondLine: "She's dead, of course!"},
}

// Verse returns a single verse of the song for the given verse number
func Verse(v int) string {
	if v < 1 || v > 8 {
		return ""
	}

	i := v - 1
	animal := animals[i]

	// Special case for horse
	if animal.name == "horse" {
		return fmt.Sprintf("I know an old lady who swallowed a %s.\n%s", animal.name, animal.secondLine)
	}

	// Build the verse
	var lines []string
	lines = append(lines, fmt.Sprintf("I know an old lady who swallowed a %s.", animal.name))
	
	if animal.secondLine != "" {
		lines = append(lines, animal.secondLine)
	}

	// Build the chain of animals she swallowed
	for j := i; j > 0; j-- {
		current := animals[j]
		prev := animals[j-1]
		line := fmt.Sprintf("She swallowed the %s to catch the %s", current.name, prev.name)
		
		// Special case for spider
		if prev.name == "spider" {
			line += " that wriggled and jiggled and tickled inside her"
		}
		line += "."
		lines = append(lines, line)
	}

	// Add the final line for all verses except fly
	if animal.name != "fly" {
		lines = append(lines, "I don't know why she swallowed the fly. Perhaps she'll die.")
	} else {
		lines = append(lines, "I don't know why she swallowed the fly. Perhaps she'll die.")
	}

	return strings.Join(lines, "\n")
}

// Verses returns multiple verses of the song
func Verses(start, end int) string {
	if start < 1 || end > 8 || start > end {
		return ""
	}

	var verses []string
	for i := start; i <= end; i++ {
		verses = append(verses, Verse(i))
	}

	return strings.Join(verses, "\n\n")
}

// Song returns the complete song
func Song() string {
	return Verses(1, 8)
}
