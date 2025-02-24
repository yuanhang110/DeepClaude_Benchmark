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
	// Clean and validate diagram format
	diagram = strings.TrimSpace(diagram)
	rows := strings.Split(diagram, "\n")
	if len(rows) != 2 {
		return nil, fmt.Errorf("invalid diagram format: expected 2 rows, got %d", len(rows))
	}

	// Validate number of children matches diagram width
	expectedWidth := len(children) * 2
	if len(rows[0]) != expectedWidth || len(rows[1]) != expectedWidth {
		return nil, fmt.Errorf(
			"diagram rows must each contain exactly %d characters, got %d and %d",
			expectedWidth,
			len(rows[0]),
			len(rows[1]),
		)
	}

	// Create sorted copy and check for duplicates
	sortedChildren := make([]string, len(children))
	copy(sortedChildren, children)
	sort.Strings(sortedChildren)
	
	seen := make(map[string]bool)
	for _, name := range sortedChildren {
		if seen[name] {
			return nil, fmt.Errorf("duplicate child name: %s", name)
		}
		seen[name] = true
	}

	// Build plant mapping
	plantsMap := make(map[string][]string)
	for i, name := range sortedChildren {
		// Get cups from both rows for this child
		cupsRow1 := rows[0][i*2 : i*2+2]
		cupsRow2 := rows[1][i*2 : i*2+2]

		var plants []string
		for _, c := range cupsRow1 {
			plant, err := convertToPlant(c)
			if err != nil {
				return nil, fmt.Errorf("invalid plant code in row 1: %w", err)
			}
			plants = append(plants, plant)
		}
		for _, c := range cupsRow2 {
			plant, err := convertToPlant(c)
			if err != nil {
				return nil, fmt.Errorf("invalid plant code in row 2: %w", err)
			}
			plants = append(plants, plant)
		}
		plantsMap[name] = plants
	}

	return &Garden{plants: plantsMap}, nil
}

func (g *Garden) Plants(child string) ([]string, bool) {
	plants, ok := g.plants[child]
	return plants, ok
}

func convertToPlant(c rune) (string, error) {
	switch c {
	case 'V':
		return "Violet", nil
	case 'R':
		return "Radish", nil
	case 'C':
		return "Clover", nil
	case 'G':
		return "Grass", nil
	default:
		return "", fmt.Errorf("invalid plant code: %c", c)
	}
}
