package scale

import "strings"

var sharpNotes = []string{"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"}
var flatNotes = []string{"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"}

var sharpTonics = map[string]bool{
	"C":  true,
	"G":  true,
	"D":  true,
	"A":  true,
	"E":  true,
	"B":  true,
	"F#": true,
	"a":  true,
	"e":  true,
	"b":  true,
	"f#": true,
	"c#": true,
	"g#": true,
	"d#": true,
}

var flatTonics = map[string]bool{
	"F":  true,
	"Bb": true,
	"Eb": true,
	"Ab": true,
	"Db": true,
	"Gb": true,
	"d":  true,
	"g":  true,
	"c":  true,
	"f":  true,
	"bb": true,
	"eb": true,
}

func normalizeTonic(tonic string) string {
	if len(tonic) == 0 {
		return ""
	}
	base := string(tonic[0])
	if len(tonic) == 1 {
		return base
	}
	accidental := strings.ToLower(tonic[1:])
	if accidental == "b" || accidental == "#" {
		return base + accidental
	}
	return base + accidental
}

func findIndex(notes []string, tonic string) int {
	for i, note := range notes {
		if note == tonic {
			return i
		}
	}
	return -1
}

func Scale(tonic, interval string) []string {
	normalizedTonic := normalizeTonic(tonic)
	var notes []string
	if sharpTonics[normalizedTonic] {
		notes = sharpNotes
	} else if flatTonics[normalizedTonic] {
		notes = flatNotes
	} else {
		notes = sharpNotes
	}

	index := findIndex(notes, normalizedTonic)
	if index == -1 {
		return nil
	}

	chromatic := append(notes[index:], notes[:index]...)

	if interval == "" {
		return chromatic
	}

	result := []string{chromatic[0]}
	currentIndex := 0
	for _, r := range interval {
		var steps int
		switch r {
		case 'M':
			steps = 2
		case 'm':
			steps = 1
		case 'A':
			steps = 3
		default:
			continue
		}
		currentIndex = (currentIndex + steps) % 12
		result = append(result, chromatic[currentIndex])
	}

	return result
}
