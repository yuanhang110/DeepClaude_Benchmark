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

	// Sort records by ID
	sort.Slice(records, func(i, j int) bool {
		return records[i].ID < records[j].ID
	})

	// Validate root node
	if records[0].ID != 0 || records[0].Parent != 0 {
		return nil, fmt.Errorf("invalid root node")
	}

	// Validate all IDs are contiguous and parent IDs are valid
	for i, r := range records {
		if r.ID != i {
			return nil, fmt.Errorf("non-contiguous or invalid ID %d", r.ID)
		}
		if r.ID != 0 && r.Parent >= r.ID {
			return nil, fmt.Errorf("invalid parent ID %d for node %d", r.Parent, r.ID)
		}
	}

	// Build tree using a map for quick lookups
	nodes := make(map[int]*Node)
	root := &Node{ID: 0}
	nodes[0] = root

	for i := 1; i < len(records); i++ {
		r := records[i]
		parent, exists := nodes[r.Parent]
		if !exists {
			return nil, fmt.Errorf("parent %d not found for node %d", r.Parent, r.ID)
		}

		node := &Node{ID: r.ID}
		parent.Children = append(parent.Children, node)
		nodes[r.ID] = node
	}

	return root, nil
}
