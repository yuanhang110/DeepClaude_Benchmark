package pov

type Tree struct {
	value    string
	parent   *Tree
	children []*Tree
}

// New creates and returns a new Tree with the given root value and children.
func New(value string, children ...*Tree) *Tree {
	t := &Tree{
		value:    value,
		children: children,
	}
	// Set parent for each child
	for _, child := range children {
		child.parent = t
	}
	return t
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
	node := findNode(tr, from)
	if node == nil {
		return nil
	}
	return reparent(node)
}

// PathTo returns the shortest path between two nodes in the tree.
func (tr *Tree) PathTo(from, to string) []string {
	fromNode := findNode(tr, from)
	toNode := findNode(tr, to)
	if fromNode == nil || toNode == nil {
		return nil
	}

	// Collect path from 'from' to root
	fromPath := []string{}
	for n := fromNode; n != nil; n = n.parent {
		fromPath = append(fromPath, n.value)
	}

	// Collect path from 'to' to root
	toPath := []string{}
	for n := toNode; n != nil; n = n.parent {
		toPath = append(toPath, n.value)
	}

	// Find the last common ancestor
	i, j := len(fromPath)-1, len(toPath)-1
	for i >= 0 && j >= 0 && fromPath[i] == toPath[j] {
		i--
		j--
	}

	// Construct the final path
	path := make([]string, 0)
	// Walk up from 'from' to LCA
	for k := 0; k <= i; k++ {
		path = append(path, fromPath[k])
	}
	// Walk down to 'to' (reversing the toPath)
	for k := j; k >= 0; k-- {
		path = append(path, toPath[k])
	}

	return path
}

// Helper function to find a node with given value in the tree
func findNode(root *Tree, value string) *Tree {
	if root == nil {
		return nil
	}
	if root.value == value {
		return root
	}
	for _, child := range root.children {
		if found := findNode(child, value); found != nil {
			return found
		}
	}
	return nil
}

// Helper function to reparent the tree from a given node
func reparent(node *Tree) *Tree {
	current := node
	current.parent = nil // new root has no parent

	for originalParent := current.parent; originalParent != nil; originalParent = current.parent {
		// Remove current from original parent's children
		for i, child := range originalParent.children {
			if child == current {
				originalParent.children = append(originalParent.children[:i], originalParent.children[i+1:]...)
				break
			}
		}
		// Add original parent as child of current
		current.children = append(current.children, originalParent)
		// Update parent relationships
		originalParent.parent = current
		// Move up the tree through original parent hierarchy
		current = originalParent
	}

	return node
}
