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
	result := tr.value
	if len(tr.children) == 0 {
		return result
	}
	for _, ch := range tr.children {
		result += " " + ch.String()
	}
	return "(" + result + ")"
}

// POV problem-specific functions

// FromPov returns the pov from the node specified in the argument.
func (tr *Tree) FromPov(from string) *Tree {
	target := tr.findNode(from)
	if target == nil || target == tr {
		return target
	}

	parentMap := tr.buildParentMap()
	path := []*Tree{target}
	current := target
	for {
		parent, exists := parentMap[current]
		if !exists {
			break
		}
		path = append(path, parent)
		current = parent
	}

	for i := 1; i < len(path); i++ {
		child := path[i-1]
		parent := path[i]

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

	return target
}

// PathTo returns the shortest path between two nodes in the tree.
func (tr *Tree) PathTo(from, to string) []string {
	start := tr.findNode(from)
	end := tr.findNode(to)
	if start == nil || end == nil {
		return nil
	}

	parentMap := tr.buildParentMap()

	visited := make(map[*Tree]bool)
	queue := [][]*Tree{{start}}
	visited[start] = true

	for len(queue) > 0 {
		path := queue[0]
		queue = queue[1:]

		current := path[len(path)-1]
		if current == end {
			result := make([]string, len(path))
			for i, node := range path {
				result[i] = node.value
			}
			return result
		}

		neighbors := current.children
		if parent, exists := parentMap[current]; exists {
			neighbors = append(neighbors, parent)
		}

		for _, neighbor := range neighbors {
			if !visited[neighbor] {
				visited[neighbor] = true
				newPath := make([]*Tree, len(path))
				copy(newPath, path)
				newPath = append(newPath, neighbor)
				queue = append(queue, newPath)
			}
		}
	}

	return nil
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

func (tr *Tree) buildParentMap() map[*Tree]*Tree {
	parentMap := make(map[*Tree]*Tree)
	queue := []*Tree{tr}
	for len(queue) > 0 {
		node := queue[0]
		queue = queue[1:]
		for _, child := range node.children {
			parentMap[child] = node
			queue = append(queue, child)
		}
	}
	return parentMap
}
