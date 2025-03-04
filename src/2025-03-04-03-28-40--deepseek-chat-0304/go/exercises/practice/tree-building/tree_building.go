package tree

import (
	"errors"
	"sort"
)

type Record struct {
	ID     int
	Parent int
}

type Node struct {
	ID       int
	Children []*Node
}

func Build(records []Record) (*Node, error) {
	if len(records) == 0 {
		return nil, nil
	}

	// Sort records by ID to ensure we process them in order
	sort.Slice(records, func(i, j int) bool {
		return records[i].ID < records[j].ID
	})

	// Validate records
	for i, r := range records {
		if r.ID != i {
			return nil, errors.New("records must be continuous")
		}
		if r.ID == 0 && r.Parent != 0 {
			return nil, errors.New("root node must have itself as parent")
		}
		if r.ID != 0 && r.Parent >= r.ID {
			return nil, errors.New("parent ID must be lower than child ID")
		}
	}

	// Create a map for quick node lookup
	nodes := make(map[int]*Node)
	for _, r := range records {
		nodes[r.ID] = &Node{ID: r.ID}
	}

	// Build the tree
	for _, r := range records {
		if r.ID == 0 {
			continue // Skip root node
		}

		parent := nodes[r.Parent]
		child := nodes[r.ID]
		parent.Children = append(parent.Children, child)
	}

	return nodes[0], nil
}
