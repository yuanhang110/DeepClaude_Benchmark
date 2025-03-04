package pov

type Tree struct {
	value    string
	children []*Tree
	parent   *Tree
}

// New creates and returns a new Tree with the given root value and children.
func New(value string, children ...*Tree) *Tree {
	t := &Tree{
		value:    value,
		children: children,
	}
	// Set parent pointers for children
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
	node := tr.findNode(from)
	if node == nil {
		return nil
	}
	return node.reparent()
}

func (tr *Tree) findNode(value string) *Tree {
	if tr.value == value {
		return tr
	}
	for _, child := range tr.children {
		if found := child.findNode(value); found != nil {
			return found
		}
	}
	return nil
}

func (tr *Tree) reparent() *Tree {
	if tr.parent == nil {
		return tr
	}
	
	// Remove self from parent's children
	parent := tr.parent
	for i, child := range parent.children {
		if child == tr {
			parent.children = append(parent.children[:i], parent.children[i+1:]...)
			break
		}
	}
	
	// Make parent a child of self
	tr.parent = nil
	tr.children = append(tr.children, parent)
	
	// Reparent the parent
	parent.reparent()
	
	return tr
}

// PathTo returns the shortest path between two nodes in the tree.
func (tr *Tree) PathTo(from, to string) []string {
	fromNode := tr.findNode(from)
	toNode := tr.findNode(to)
	if fromNode == nil || toNode == nil {
		return nil
	}

	// Get paths from both nodes to root
	fromPath := fromNode.pathToRoot()
	toPath := toNode.pathToRoot()

	// Find the lowest common ancestor
	var lca *Tree
	i := 0
	for i < len(fromPath) && i < len(toPath) && fromPath[len(fromPath)-1-i] == toPath[len(toPath)-1-i] {
		lca = fromPath[len(fromPath)-1-i]
		i++
	}

	// Build the path
	var path []string
	// From 'from' to LCA (excluding LCA)
	for _, node := range fromPath {
		if node == lca {
			break
		}
		path = append(path, node.value)
	}
	// From LCA to 'to' (including LCA)
	lcaIndex := len(toPath) - i
	for j := lcaIndex; j >= 0; j-- {
		path = append(path, toPath[j].value)
	}

	return path
}

func (tr *Tree) pathToRoot() []*Tree {
	var path []*Tree
	current := tr
	for current != nil {
		path = append(path, current)
		current = current.parent
	}
	return path
}
