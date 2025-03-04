package scale

import (
	"strings"
)

var (
	sharpScale = []string{"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"}
	flatScale  = []string{"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"}
)

// getScaleType determines whether to use sharp or flat scale based on tonic
func getScaleType(tonic string) []string {
	// More precise scale selection
	switch tonic {
	case "C", "a", "G", "D", "A", "E", "B", "F#", "e", "b", "f#", "c#", "g#", "d#":
		return sharpScale
	case "F", "Bb", "Eb", "Ab", "Db", "Gb", "d", "g", "c", "f", "bb", "eb":
		return flatScale
	default:
		// Default to sharp scale for unknown tonics
		return sharpScale
	}
}

// findIndex finds the starting index of the tonic in the scale
func findIndex(scale []string, tonic string) int {
	// Handle flats properly
	tonic = strings.ToUpper(tonic)
	if len(tonic) > 1 && tonic[1] == 'b' {
		tonic = string(tonic[0]) + "b"
	}
	for i, note := range scale {
		if strings.EqualFold(note, tonic) {
			return i
		}
	}
	return 0
}

// getIntervalSteps converts interval pattern to step sizes
func getIntervalSteps(interval string) []int {
	steps := []int{}
	for _, r := range interval {
		switch r {
		case 'm':
			steps = append(steps, 1)
		case 'M':
			steps = append(steps, 2)
		case 'A':
			steps = append(steps, 3)
		}
	}
	return steps
}

func Scale(tonic, interval string) []string {
	scale := getScaleType(tonic)
	index := findIndex(scale, tonic)
	result := []string{scale[index]}

	if interval == "" {
		// Chromatic scale - return all 12 notes
		for i := 1; i < 12; i++ {
			index = (index + 1) % 12
			result = append(result, scale[index])
		}
	} else {
		// Diatonic scale - follow interval pattern
		steps := getIntervalSteps(interval)
		for _, step := range steps {
			index = (index + step) % 12
			result = append(result, scale[index])
		}
	}

	return result
}
