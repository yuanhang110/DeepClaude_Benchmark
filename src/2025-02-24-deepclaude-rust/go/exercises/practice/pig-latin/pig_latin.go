package piglatin

import "strings"

func isVowel(c rune) bool {
	return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
}

func allConsonants(s string) bool {
	for _, c := range s {
		if isVowel(c) {
			return false
		}
	}
	return true
}

func translateWord(word string) string {
	// Rule 1: starts with vowel, xr, yt
	if len(word) >= 2 {
		if word[:2] == "xr" || word[:2] == "yt" {
			return word + "ay"
		}
	}
	if len(word) >= 1 && isVowel(rune(word[0])) {
		return word + "ay"
	}

	// Rule 3: check for "qu" after consonants
	for i := 0; i < len(word)-1; i++ {
		if i+2 <= len(word) && word[i:i+2] == "qu" {
			prefix := word[:i]
			if allConsonants(prefix) {
				return word[i+2:] + prefix + "qu" + "ay"
			}
		}
	}

	// Rule 4: consonants followed by y
	for i := 0; i < len(word); i++ {
		if word[i] == 'y' {
			prefix := word[:i]
			if len(prefix) >= 1 && allConsonants(prefix) {
				return word[i:] + prefix + "ay"
			}
		}
	}

	// Rule 2: move consonants to end
	for i, c := range word {
		if isVowel(c) {
			return word[i:] + word[:i] + "ay"
		}
	}
	return word + "ay"
}

func Sentence(sentence string) string {
	words := strings.Split(sentence, " ")
	translated := make([]string, len(words))
	for i, word := range words {
		translated[i] = translateWord(word)
	}
	return strings.Join(translated, " ")
}
