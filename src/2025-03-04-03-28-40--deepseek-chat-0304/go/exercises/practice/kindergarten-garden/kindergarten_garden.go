package kindergarten

import (
	"errors"
	"sort"
	"strings"
)

type Garden struct {
	plants map[string][]string
}

var plantMap = map[rune]string{
	'G': "grass",
	'C': "clover",
	'R': "radishes",
	'V': "violets",
}

func NewGarden(diagram string, children []string) (*Garden, error) {
	// Validate diagram format
	lines := strings.Split(diagram, "\n")
	if len(lines) != 3 || lines[0] != "" {
		return nil, errors.New("invalid diagram format")
	}
	
	// Sort children alphabetically
	sortedChildren := make([]string, len(children))
	copy(sortedChildren, children)
	sort.Strings(sortedChildren)
	
	// Create mapping from child to index
	childIndex := make(map[string]int)
	for i, child := range sortedChildren {
		if _, exists := childIndex[child]; exists {
			return nil, errors.New("duplicate child name")
		}
		childIndex[child] = i
	}
	
	// Parse plant rows
	row1 := lines[1]
	row2 := lines[2]
	
	if len(row1) != len(row2) || len(row1)%2 != 0 || len(row1)/2 != len(children) {
		return nil, errors.New("mismatched diagram size")
	}
	
	// Build plant mapping
	plants := make(map[string][]string)
	for i := 0; i < len(row1); i += 2 {
		child := sortedChildren[i/2]
		
		// Get plant codes
		codes := []rune{
			rune(row1[i]),
			rune(row1[i+1]),
			rune(row2[i]),
			rune(row2[i+1]),
		}
		
		// Convert to plant names
		var plantNames []string
		for _, code := range codes {
			if name, exists := plantMap[code]; exists {
				plantNames = append(plantNames, name)
			} else {
				return nil, errors.New("invalid plant code")
			}
		}
		
		plants[child] = plantNames
	}
	
	return &Garden{plants: plants}, nil
}

func (g *Garden) Plants(child string) ([]string, bool) {
	plants, exists := g.plants[child]
	return plants, exists
}
