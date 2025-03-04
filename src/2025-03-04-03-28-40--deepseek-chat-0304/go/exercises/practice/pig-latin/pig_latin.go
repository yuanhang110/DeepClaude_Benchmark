package piglatin

import (
	"strings"
	"unicode"
)

func isVowel(c rune) bool {
	c = unicode.ToLower(c)
	return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
}

func startsWithXRorYT(word string) bool {
	if len(word) < 2 {
		return false
	}
	return strings.HasPrefix(strings.ToLower(word), "xr") ||
		strings.HasPrefix(strings.ToLower(word), "yt")
}

func translateWord(word string) string {
	if len(word) == 0 {
		return word
	}

	// Rule 1: Starts with vowel or xr/yt
	if isVowel(rune(word[0])) || startsWithXRorYT(word) {
		return word + "ay"
	}

	// Rule 3 & 4: Handle 'qu' and 'y' cases
	for i := 0; i < len(word); i++ {
		c := rune(word[i])
		if c == 'y' && i > 0 {
			return word[i:] + word[:i] + "ay"
		}
		if c == 'q' && i+1 < len(word) && word[i+1] == 'u' {
			return word[i+2:] + word[:i+2] + "ay"
		}
		if isVowel(c) {
			break
		}
	}

	// Rule 2: Starts with consonants
	for i := 0; i < len(word); i++ {
		if isVowel(rune(word[i])) {
			return word[i:] + word[:i] + "ay"
		}
	}

	return word + "ay"
}

func Sentence(sentence string) string {
	words := strings.Fields(sentence)
	for i, word := range words {
		words[i] = translateWord(word)
	}
	return strings.Join(words, " ")
}
