package piglatin

import (
	"strings"
	"unicode"
)

func Sentence(sentence string) string {
	words := strings.Fields(sentence)
	var translated []string
	for _, word := range words {
		translated = append(translated, translateWord(word))
	}
	return strings.Join(translated, " ")
}

func translateWord(word string) string {
	switch {
	case startsWithVowelSound(word):
		return word + "ay"
	case hasQUPrefix(word):
		return handleQU(word)
	case hasConsonantY(word):
		return handleConsonantY(word)
	default:
		return handleConsonantCluster(word)
	}
}

func startsWithVowelSound(word string) bool {
	if len(word) == 0 {
		return false
	}
	
	// Check for vowel at start
	first := unicode.ToLower(rune(word[0]))
	if strings.ContainsRune("aeiou", first) {
		return true
	}
	
	// Check for 'xr' or 'yt' at start
	if len(word) >= 2 {
		firstTwo := strings.ToLower(word[:2])
		return firstTwo == "xr" || firstTwo == "yt"
	}
	return false
}

func hasQUPrefix(word string) bool {
	lowerWord := strings.ToLower(word)
	for i := 0; i < len(lowerWord)-1; i++ {
		if lowerWord[i:i+2] == "qu" {
			return true
		}
		if i > 0 && lowerWord[i-1:i+2] == "squ" {
			return true
		}
	}
	return false
}

func handleQU(word string) string {
	lowerWord := strings.ToLower(word)
	quIndex := strings.Index(lowerWord, "qu")
	if quIndex == -1 {
		quIndex = strings.Index(lowerWord, "squ")
	}
	
	if quIndex != -1 && quIndex+2 < len(word) {
		return word[quIndex+2:] + word[:quIndex+2] + "ay"
	}
	return word + "ay"
}

func hasConsonantY(word string) bool {
	lowerWord := strings.ToLower(word)
	if startsWithVowelSound(lowerWord) {
		return false
	}
	
	yIndex := strings.Index(lowerWord, "y")
	if yIndex == -1 {
		return false
	}
	
	// Check if there are any vowels before y
	for _, r := range lowerWord[:yIndex] {
		if strings.ContainsRune("aeiou", r) {
			return false
		}
	}
	
	return yIndex > 0
}

func handleConsonantY(word string) string {
	yIndex := strings.Index(strings.ToLower(word), "y")
	return word[yIndex:] + word[:yIndex] + "ay"
}

func handleConsonantCluster(word string) string {
	var cluster string
	lowerWord := strings.ToLower(word)
	
	for i, r := range lowerWord {
		if strings.ContainsRune("aeiou", r) {
			cluster = word[:i]
			return word[i:] + cluster + "ay"
		}
	}
	return word + "ay"
}
