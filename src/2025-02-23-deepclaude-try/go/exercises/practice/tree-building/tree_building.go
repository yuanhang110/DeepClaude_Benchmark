package tree

import (
	"errors"
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

	sort.Slice(records, func(i, j int) bool {
		return records[i].ID < records[j].ID
	})

	// Validate root node
	if records[0].ID != 0 || records[0].Parent != 0 {
		return nil, errors.New("root node invalid")
	}

	// Validate all IDs match their index and parent relationships
	for i, r := range records {
		if r.ID != i {
			return nil, errors.New("non-contiguous or out-of-order IDs")
		}

		if i == 0 {
			continue // root already validated
		}

		if r.Parent >= r.ID {
			return nil, errors.New("parent ID must be lower than child ID")
		}
	}

	nodes := make(map[int]*Node)
	root := &Node{ID: 0}
	nodes[0] = root

	for _, r := range records[1:] {
		parent, exists := nodes[r.Parent]
		if !exists {
			return nil, errors.New("parent node not found")
		}

		node := &Node{ID: r.ID}
		parent.Children = append(parent.Children, node)
		nodes[r.ID] = node
	}

	return root, nil
}
