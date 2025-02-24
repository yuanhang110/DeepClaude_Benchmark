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

	// Sort records by ID to ensure we process parents before children
	sort.Slice(records, func(i, j int) bool {
		return records[i].ID < records[j].ID
	})

	nodes := make(map[int]*Node)
	
	for i, record := range records {
		// Validate record ID matches its index after sorting
		if i != record.ID {
			return nil, fmt.Errorf("non-contiguous ID %d", record.ID)
		}

		// Validate root node
		if record.ID == 0 {
			if record.Parent != 0 {
				return nil, fmt.Errorf("root node has parent %d", record.Parent)
			}
			nodes[0] = &Node{ID: 0}
			continue
		}

		// Validate parent exists and has lower ID
		if record.Parent >= record.ID {
			return nil, fmt.Errorf("node %d has invalid parent %d", record.ID, record.Parent)
		}

		parent, exists := nodes[record.Parent]
		if !exists {
			return nil, fmt.Errorf("parent %d not found for node %d", record.Parent, record.ID)
		}

		// Create and append child node
		nodes[record.ID] = &Node{ID: record.ID}
		parent.Children = append(parent.Children, nodes[record.ID])
	}

	return nodes[0], nil
}
