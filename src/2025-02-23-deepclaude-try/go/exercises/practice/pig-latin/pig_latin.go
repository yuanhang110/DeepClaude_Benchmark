package piglatin

import (
	"strings"
)

func isVowel(c byte) bool {
	return c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'
}

func Sentence(sentence string) string {
	words := strings.Fields(sentence)
	var translated []string

	for _, word := range words {
		lowerWord := strings.ToLower(word)
		var splitIndex int

		// Check Rule 1: vowel, xr, yt
		if len(lowerWord) >= 2 {
			if isVowel(lowerWord[0]) || strings.HasPrefix(lowerWord, "xr") || strings.HasPrefix(lowerWord, "yt") {
				translated = append(translated, lowerWord+"ay")
				continue
			}
		} else if len(lowerWord) > 0 && isVowel(lowerWord[0]) {
			translated = append(translated, lowerWord+"ay")
			continue
		}

		// Check Rule 3: consonants followed by qu
		for i := 0; i < len(lowerWord)-1; i++ {
			if lowerWord[i] == 'q' && lowerWord[i+1] == 'u' {
				splitIndex = i + 2
				break
			}
		}

		// Check Rule 4: consonants followed by y
		if splitIndex == 0 {
			for i := 0; i < len(lowerWord); i++ {
				if lowerWord[i] == 'y' {
					splitIndex = i
					break
				}
				if isVowel(lowerWord[i]) {
					break
				}
			}
		}

		// Check Rule 2: consonants before first vowel
		if splitIndex == 0 {
			for i := 0; i < len(lowerWord); i++ {
				if isVowel(lowerWord[i]) {
					splitIndex = i
					break
				}
			}
			// If no vowels found, move all consonants
			if splitIndex == 0 {
				splitIndex = len(lowerWord)
			}
		}

		translatedWord := lowerWord[splitIndex:] + lowerWord[:splitIndex] + "ay"
		translated = append(translated, translatedWord)
	}

	return strings.Join(translated, " ")
}
