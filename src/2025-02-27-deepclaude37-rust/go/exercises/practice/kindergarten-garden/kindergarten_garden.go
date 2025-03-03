package kindergarten

import (
	"fmt"
	"sort"
	"strings"
)

// Garden represents a kindergarten garden with plants assigned to children.
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
	rows := strings.Split(diagram, "\n")
	if len(rows) != 3 || rows[0] != "" {
		return nil, fmt.Errorf("invalid diagram format")
	}

	row1 := rows[1]
	row2 := rows[2]

	sortedChildren := make([]string, len(children))
	copy(sortedChildren, children)
	sort.Strings(sortedChildren)

	// Check for duplicate child names
	seen := make(map[string]bool)
	for _, child := range sortedChildren {
		if seen[child] {
			return nil, fmt.Errorf("duplicate child name: %s", child)
		}
		seen[child] = true
	}

	numChildren := len(sortedChildren)
	if len(row1) != 2*numChildren || len(row2) != 2*numChildren {
		return nil, fmt.Errorf("row length mismatch")
	}

	plantsMap := make(map[string][]string)
	for i, child := range sortedChildren {
		start := 2 * i
		end := start + 2

		cupsRow1 := row1[start:end]
		cupsRow2 := row2[start:end]

		var plants []string
		for _, c := range []byte(cupsRow1) {
			plant, err := plantFor(c)
			if err != nil {
				return nil, err
			}
			plants = append(plants, plant)
		}
		for _, c := range []byte(cupsRow2) {
			plant, err := plantFor(c)
			if err != nil {
				return nil, err
			}
			plants = append(plants, plant)
		}

		plantsMap[child] = plants
	}

	return &Garden{plants: plantsMap}, nil
}

func plantFor(c byte) (string, error) {
	switch c {
	case 'V':
		return "violets", nil
	case 'C':
		return "clover", nil
	case 'R':
		return "radishes", nil
	case 'G':
		return "grass", nil
	default:
		return "", fmt.Errorf("invalid plant code: %c", c)
	}
}

func (g *Garden) Plants(child string) ([]string, bool) {
	plants, ok := g.plants[child]
	if !ok {
		return nil, false
	}
	return plants, true
}
