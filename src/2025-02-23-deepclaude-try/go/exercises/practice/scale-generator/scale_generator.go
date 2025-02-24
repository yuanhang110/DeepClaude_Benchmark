package scale

import (
	"strings"
)

func Scale(tonic, interval string) []string {
	normalizedTonic := normalizeTonic(tonic)
	
	sharpScale := []string{"A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"}
	flatScale := []string{"A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"}
	
	sharpTonics := map[string]bool{
		"C":   true, // Natural
		"A":   true, // Natural
		"E":   true, // In both major and minor sharp lists
		"B":   true, // In both major and minor sharp lists
		"F#":  true, // In both major and minor sharp lists
		"C#":  true, // Minor sharp
		"G#":  true, // Minor sharp  
		"D#":  true, // Minor sharp
	}
	
	var baseScale []string
	if sharpTonics[normalizedTonic] {
		baseScale = sharpScale
	} else {
		baseScale = flatScale
	}
	
	rotated := rotateScale(baseScale, normalizedTonic)
	
	if interval == "" {
		return rotated
	}
	
	intervalSteps := map[rune]int{
		'm': 1,
		'M': 2,
		'A': 3,
	}
	
	scale := []string{rotated[0]}
	currentIndex := 0
	for _, r := range interval {
		step, ok := intervalSteps[r]
		if !ok {
			return []string{}
		}
		currentIndex = (currentIndex + step) % 12
		scale = append(scale, rotated[currentIndex])
	}
	return scale
}

func normalizeTonic(tonic string) string {
	if len(tonic) == 0 {
		return ""
	}
	normalized := strings.ToUpper(string(tonic[0]))
	if len(tonic) > 1 {
		normalized += strings.ToLower(tonic[1:])
	}
	return normalized
}

func rotateScale(scale []string, tonic string) []string {
	for i, note := range scale {
		if note == tonic {
			return append(scale[i:], scale[:i]...)
		}
	}
	return scale
}
