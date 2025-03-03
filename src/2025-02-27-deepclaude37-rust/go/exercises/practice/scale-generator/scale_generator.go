package scale

import (
	"strings"
	"unicode"
)

var (
	sharpScale = []string{"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"}
	flatScale  = []string{"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"}

	sharpMajors = map[string]bool{"G": true, "D": true, "A": true, "E": true, "B": true, "F#": true}
	sharpMinors = map[string]bool{"E": true, "B": true, "F#": true, "C#": true, "G#": true, "D#": true}
	flatMajors  = map[string]bool{"F": true, "Bb": true, "Eb": true, "Ab": true, "Db": true, "Gb": true}
	flatMinors  = map[string]bool{"D": true, "G": true, "C": true, "F": true, "Bb": true, "Eb": true}
)

func normalizeTonic(tonic string) string {
	if len(tonic) == 0 {
		return ""
	}
	runes := []rune(tonic)
	first := string(unicode.ToUpper(runes[0]))
	rest := ""
	if len(runes) > 1 {
		rest = strings.ToLower(string(runes[1:]))
	}
	return first + rest
}

func determineScaleType(tonic string) []string {
	// Handle natural tonics first
	if tonic == "C" || tonic == "A" {
		return sharpScale
	}
	// Check flat keys before sharp keys
	if flatMajors[tonic] || flatMinors[tonic] {
		return flatScale
	}
	if sharpMajors[tonic] || sharpMinors[tonic] {
		return sharpScale
	}
	// Default to sharps
	return sharpScale
}

func indexOf(note string, scale []string) int {
	for i, n := range scale {
		if n == note {
			return i
		}
	}
	return -1
}

func rotateScale(base []string, index int) []string {
	return append(base[index:], base[:index]...)
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

func Scale(tonic, interval string) []string {
	// Normalize the tonic
	normalizedTonic := normalizeTonic(tonic)

	// Determine the base scale (sharp or flat)
	baseScale := determineScaleType(normalizedTonic)

	// Find the index of the tonic in the base scale
	index := indexOf(normalizedTonic, baseScale)
	if index == -1 {
		// Tonic not found in base scale
		return nil
	}

	// Rotate the base scale to start with the tonic
	rotated := rotateScale(baseScale, index)

	// If interval is empty, return the chromatic scale
	if interval == "" {
		return rotated
	}

	// Generate diatonic scale based on the interval pattern
	steps := getSteps(interval)
	currentIndex := 0
	scale := make([]string, 0, len(steps)+1)
	scale = append(scale, rotated[0])

	for _, step := range steps {
		currentIndex = (currentIndex + step) % 12
		scale = append(scale, rotated[currentIndex])
	}

	return scale
}
