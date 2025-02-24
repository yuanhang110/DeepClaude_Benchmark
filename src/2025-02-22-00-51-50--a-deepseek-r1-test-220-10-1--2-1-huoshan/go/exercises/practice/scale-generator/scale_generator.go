package scale

import "strings"

var sharpNotes = []string{"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"}
var flatNotes = []string{"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"}

func Scale(tonic, interval string) []string {
	normalizedTonic := normalizeTonic(tonic)
	base := getBaseScale(normalizedTonic)
	index := findIndex(base, normalizedTonic)
	rotated := rotateScale(base, index)

	if interval == "" {
		return rotated
	}

	steps := getSteps(interval)
	return buildScale(rotated, steps)
}

func normalizeTonic(tonic string) string {
	if tonic == "" {
		return ""
	}
	t := strings.ReplaceAll(tonic, "♯", "#")
	t = strings.ReplaceAll(t, "♭", "b")
	if len(t) == 1 {
		return strings.ToUpper(t)
	}
	return strings.ToUpper(string(t[0])) + strings.ToLower(t[1:])
}

func getBaseScale(tonic string) []string {
	sharpTonics := map[string]bool{
		"G": true, "D": true, "A": true, "E": true, "B": true, "F#": true,
		"C#": true, "G#": true, "D#": true, "E": true, "B": true, "F#": true,
	}
	if sharpTonics[tonic] {
		return sharpNotes
	}
	return flatNotes
}

func findIndex(scale []string, tonic string) int {
	for i, note := range scale {
		if note == tonic {
			return i
		}
	}
	return 0
}

func rotateScale(scale []string, index int) []string {
	return append(scale[index:], scale[:index]...)
}

func getSteps(interval string) []int {
	steps := make([]int, len(interval))
	for i, c := range interval {
		switch c {
		case 'm':
			steps[i] = 1
		case 'M':
			steps[i] = 2
		case 'A':
			steps[i] = 3
		}
	}
	return steps
}

func buildScale(scale []string, steps []int) []string {
	notes := make([]string, 0, len(steps)+1)
	current := 0
	notes = append(notes, scale[current])
	for _, step := range steps {
		current += step
		current %= len(scale)
		notes = append(notes, scale[current])
	}
	return notes
}
