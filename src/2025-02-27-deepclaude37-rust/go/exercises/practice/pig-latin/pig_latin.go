package piglatin

import (
	"regexp"
	"strings"
)

func Sentence(sentence string) string {
	words := strings.Fields(sentence)
	for i, word := range words {
		words[i] = translateWord(word)
	}
	return strings.Join(words, " ")
}

func translateWord(word string) string {
	// Rule 1: If word begins with vowel or starts with "xr" or "yt"
	if len(word) >= 2 && (word[:2] == "xr" || word[:2] == "yt") {
		return word + "ay"
	}
	if len(word) > 0 && strings.ContainsRune("aeiou", rune(word[0])) {
		return word + "ay"
	}

	// Rule 3: If word starts with zero or more consonants followed by "qu"
	re := regexp.MustCompile(`^([^aeiou]*qu)(.*)`)
	if matches := re.FindStringSubmatch(word); matches != nil {
		return matches[2] + matches[1] + "ay"
	}

	// Rule 4: If word starts with consonants followed by "y"
	re = regexp.MustCompile(`^([^aeiou]+)(y.*)`)
	if matches := re.FindStringSubmatch(word); matches != nil {
		return matches[2] + matches[1] + "ay"
	}

	// Rule 2: If word begins with consonants
	re = regexp.MustCompile(`^([^aeiou]+)(.*)`)
	matches := re.FindStringSubmatch(word)
	return matches[2] + matches[1] + "ay"
}
