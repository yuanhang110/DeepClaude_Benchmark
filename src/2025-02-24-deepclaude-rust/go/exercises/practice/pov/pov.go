package pov

type Tree struct {
	value    string
	children []*Tree
}

// New creates and returns a new Tree with the given root value and children.
func New(value string, children ...*Tree) *Tree {
	return &Tree{value: value, children: children}
}

// Value returns the value at the root of a tree.
func (tr *Tree) Value() string {
	return tr.value
}

// Children returns a slice containing the children of a tree.
// There is no need to sort the elements in the result slice,
// they can be in any order.
func (tr *Tree) Children() []*Tree {
	return tr.children
}

// String describes a tree in a compact S-expression format.
// This helps to make test outputs more readable.
// Feel free to adapt this method as you see fit.
func (tr *Tree) String() string {
	if tr == nil {
		return "nil"
	}
	result := tr.Value()
	if len(tr.Children()) == 0 {
		return result
	}
	for _, ch := range tr.Children() {
		result += " " + ch.String()
	}
	return "(" + result + ")"
}

// POV problem-specific functions

// FromPov returns the pov from the node specified in the argument.
func (tr *Tree) FromPov(from string) *Tree {
	if tr == nil {
		return nil
	}
	path := findPath(tr, from)
	if path == nil {
		return nil
	}
	// Reparent each node along the path to make 'from' the root
	for i := len(path) - 2; i >= 0; i-- {
		parent := path[i]
		child := path[i+1]
		// Remove child from parent's children
		for j, c := range parent.children {
			if c == child {
				parent.children = append(parent.children[:j], parent.children[j+1:]...)
				break
			}
		}
		// Add parent to child's children
		child.children = append(child.children, parent)
	}
	return path[len(path)-1]
}

func findPath(node *Tree, target string) []*Tree {
	if node == nil {
		return nil
	}
	if node.value == target {
		return []*Tree{node}
	}
	for _, child := range node.children {
		if path := findPath(child, target); path != nil {
			return append([]*Tree{node}, path...)
		}
	}
	return nil
}

// PathTo returns the shortest path between two nodes in the tree.
func (tr *Tree) PathTo(from, to string) []string {
	pathFrom := findPath(tr, from)
	pathTo := findPath(tr, to)
	if pathFrom == nil || pathTo == nil {
		return nil
	}

	// Find lowest common ancestor (LCA)
	lcaIndex := 0
	for ; lcaIndex < len(pathFrom) && lcaIndex < len(pathTo); lcaIndex++ {
		if pathFrom[lcaIndex] != pathTo[lcaIndex] {
			break
		}
	}
	if lcaIndex == 0 {
		return nil // No common ancestor (shouldn't happen in a tree)
	}
	lcaIndex-- // Step back to last common node

	// Build path by going up from 'from' to LCA, then down to 'to'
	var result []string
	// Reverse the pathFrom from 'from' to LCA (exclusive)
	for i := len(pathFrom) - 1; i > lcaIndex; i-- {
		result = append(result, pathFrom[i].value)
	}
	// Add pathTo from LCA to 'to'
	for i := lcaIndex; i < len(pathTo); i++ {
		result = append(result, pathTo[i].value)
	}
	return result
}
