package kindergarten

import (
	"errors"
	"fmt"
	"sort"
	"strings"
)

type Garden struct {
	plants map[string][]string
}

// The diagram argument starts each row with a '\n'.  This allows Go's
// raw string literals to present diagrams in source code nicely as two
// rows flush left, for example,
//
//     diagram := `
//     VVCCGG
//     VVCCGG`

func NewGarden(diagram string, children []string) (*Garden, error) {
	// Make a copy of the children slice and sort it
	sortedChildren := make([]string, len(children))
	copy(sortedChildren, children)
	sort.Strings(sortedChildren)

	// Check for duplicate child names
	for i := 1; i < len(sortedChildren); i++ {
		if sortedChildren[i] == sortedChildren[i-1] {
			return nil, fmt.Errorf("duplicate child name: %s", sortedChildren[i])
		}
	}

	// Parse diagram
	lines := strings.Split(diagram, "\n")
	if len(lines) < 3 || lines[0] != "" {
		return nil, errors.New("invalid diagram format")
	}
	row1 := lines[1]
	row2 := lines[2]

	// Check for extra non-empty lines
	for _, line := range lines[3:] {
		if line != "" {
			return nil, errors.New("diagram contains extra non-empty lines")
		}
	}

	// Validate row lengths match number of children
	numChildren := len(sortedChildren)
	requiredLength := 2 * numChildren
	if len(row1) != requiredLength || len(row2) != requiredLength {
		return nil, fmt.Errorf("invalid row length, expected %d cups got %d and %d", 
			requiredLength, len(row1), len(row2))
	}

	// Validate plant codes
	plantCodes := map[rune]bool{'V': true, 'C': true, 'R': true, 'G': true}
	for _, c := range row1 + row2 {
		if !plantCodes[rune(c)] {
			return nil, fmt.Errorf("invalid plant code: %c", c)
		}
	}

	// Create plant map
	plantNames := map[rune]string{
		'V': "violets",
		'C': "clover",
		'R': "radishes",
		'G': "grass",
	}
	plantsMap := make(map[string][]string, numChildren)

	for i, child := range sortedChildren {
		start := i * 2
		// Get cups from both rows
		cups1 := row1[start : start+2]
		cups2 := row2[start : start+2]

		plants := make([]string, 0, 4)
		for _, c := range cups1 {
			plants = append(plants, plantNames[rune(c)])
		}
		for _, c := range cups2 {
			plants = append(plants, plantNames[rune(c)])
		}
		plantsMap[child] = plants
	}

	return &Garden{plants: plantsMap}, nil
}

func (g *Garden) Plants(child string) ([]string, bool) {
	plants, exists := g.plants[child]
	if !exists {
		return nil, false
	}
	// Return a copy to prevent external modification
	result := make([]string, len(plants))
	copy(result, plants)
	return result, true
}
