package kindergarten

import (
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
	// Check for duplicate children
	childMap := make(map[string]bool)
	for _, child := range children {
		if childMap[child] {
			return nil, fmt.Errorf("duplicate child name: %s", child)
		}
		childMap[child] = true
	}

	// Sort a copy of the children slice
	sorted := make([]string, len(children))
	copy(sorted, children)
	sort.Strings(sorted)

	// Parse diagram rows
	rows := strings.Split(diagram, "\n")
	if len(rows) != 3 || rows[0] != "" {
		return nil, fmt.Errorf("diagram must start with newline and have exactly two rows")
	}
	row1, row2 := rows[1], rows[2]

	// Validate row lengths
	expectedLen := 2 * len(children)
	if len(row1) != expectedLen || len(row2) != expectedLen {
		return nil, fmt.Errorf("rows must have exactly %d characters each", expectedLen)
	}

	// Validate plant codes
	for _, c := range row1 + row2 {
		if !strings.ContainsRune("VRCG", c) {
			return nil, fmt.Errorf("invalid plant code '%c'", c)
		}
	}

	// Build plants mapping
	plantsMap := make(map[string][]string, len(children))
	for i, child := range sorted {
		start := i * 2
		var plants []string
		for _, c := range row1[start : start+2] {
			plants = append(plants, codeToPlant(c))
		}
		for _, c := range row2[start : start+2] {
			plants = append(plants, codeToPlant(c))
		}
		plantsMap[child] = plants
	}

	return &Garden{plants: plantsMap}, nil
}

func codeToPlant(c rune) string {
	switch c {
	case 'V':
		return "violets"
	case 'R':
		return "radishes"
	case 'C':
		return "clover"
	default: // 'G' is only remaining valid option
		return "grass"
	}
}

func (g *Garden) Plants(child string) ([]string, bool) {
	plants, ok := g.plants[child]
	if !ok {
		return nil, false
	}
	// Return a copy to prevent modification of internal data
	copyPlants := make([]string, len(plants))
	copy(copyPlants, plants)
	return copyPlants, true
}
