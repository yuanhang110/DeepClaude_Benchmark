package pov

type Tree struct {
	// Add the needed fields here
}

func New(value string, children ...*Tree) *Tree {
	t := &Tree{
		value:    value,
		children: children,
	}
	for _, child := range children {
		child.parent = t
	}
	return t
}

func (tr *Tree) Value() string {
	return tr.value
}

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

func (tr *Tree) findRoot() *Tree {
	current := tr
	for current.parent != nil {
		current = current.parent
	}
	return current
}

func (tr *Tree) findNode(value string) *Tree {
	root := tr.findRoot()
	queue := []*Tree{root}
	for len(queue) > 0 {
		node := queue[0]
		queue = queue[1:]
		if node.value == value {
			return node
		}
		queue = append(queue, node.children...)
	}
	return nil
}

func (tr *Tree) pathToRoot() []string {
	var path []string
	current := tr
	for current != nil {
		path = append(path, current.value)
		current = current.parent
	}
	return path
}

func (tr *Tree) FromPov(from string) *Tree {
	target := tr.findNode(from)
	if target == nil {
		return nil
	}

	current := target
	for current.parent != nil {
		parent := current.parent
		for i, child := range parent.children {
			if child == current {
				parent.children = append(parent.children[:i], parent.children[i+1:]...)
				break
			}
		}
		current.children = append(current.children, parent)
		parent.parent = current
		current = parent
	}

	target.parent = nil
	return target
}

func min(a, b int) int {
	if a < b {
		return a
	}
	return b
}

func (tr *Tree) PathTo(from, to string) []string {
	fromNode := tr.findNode(from)
	toNode := tr.findNode(to)
	if fromNode == nil || toNode == nil {
		return nil
	}

	fromPath := fromNode.pathToRoot()
	toPath := toNode.pathToRoot()

	lcaIndex := 0
	minLen := min(len(fromPath), len(toPath))
	for i := 0; i < minLen; i++ {
		fromIdx := len(fromPath) - 1 - i
		toIdx := len(toPath) - 1 - i
		if fromPath[fromIdx] != toPath[toIdx] {
			break
		}
		lcaIndex++
	}

	if lcaIndex == 0 {
		return nil
	}

	path := make([]string, 0)
	fromPart := fromPath[0 : len(fromPath)-lcaIndex]
	path = append(path, fromPart...)
	lcaValue := fromPath[len(fromPath)-lcaIndex]
	path = append(path, lcaValue)

	toPart := toPath[0 : len(toPath)-lcaIndex]
	for i := len(toPart) - 1; i >= 0; i-- {
		path = append(path, toPart[i])
	}

	return path
}
