package tree

import (
	"fmt"
	"sort"
)

type Record struct {
	ID     int
	Parent int
	// feel free to add fields as you see fit
}

type Node struct {
	ID       int
	Children []*Node
	// feel free to add fields as you see fit
}

func Build(records []Record) (*Node, error) {
	if len(records) == 0 {
		return nil, nil
	}

	// Sort records by ID for processing in order
	sort.Slice(records, func(i, j int) bool {
		return records[i].ID < records[j].ID
	})

	// Validate records
	for i, r := range records {
		// Check IDs are contiguous and start from 0
		if r.ID != i {
			return nil, fmt.Errorf("non-contiguous or invalid ID: %d", r.ID)
		}

		// Check parent relationships
		if i == 0 {
			// Root node must have parent = self
			if r.Parent != 0 {
				return nil, fmt.Errorf("root node has invalid parent: %d", r.Parent)
			}
		} else {
			// Non-root nodes must have parent ID < own ID
			if r.Parent >= r.ID {
				return nil, fmt.Errorf("node %d has invalid parent: %d", r.ID, r.Parent)
			}
		}
	}

	// Create nodes
	nodes := make([]*Node, len(records))
	for i := range records {
		nodes[i] = &Node{ID: i}
	}

	// Build tree structure
	for i := 1; i < len(records); i++ {
		parentID := records[i].Parent
		nodes[parentID].Children = append(nodes[parentID].Children, nodes[i])
	}

	return nodes[0], nil
}
