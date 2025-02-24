from json import dumps


class Tree:
    def __init__(self, label, children=None):
        self.label = label
        self.children = children if children is not None else []

    def __dict__(self):
        return {self.label: [c.__dict__() for c in sorted(self.children)]}

    def __str__(self, indent=None):
        return dumps(self.__dict__(), indent=indent)

    def __lt__(self, other):
        return self.label < other.label

    def __eq__(self, other):
        return self.__dict__() == other.__dict__()

    def _find_node(self, label):
        """DFS search to find node with given label."""
        if self.label == label:
            return self
        for child in self.children:
            result = child._find_node(label)
            if result:
                return result
        return None

    def _clone(self):
        """Create a deep copy of the tree."""
        return Tree(self.label, [child._clone() for child in self.children])

    def _find_path(self, target_label):
        """BFS to find path from self to node with target_label."""
        queue = [(self, [self.label])]
        while queue:
            node, path = queue.pop(0)
            if node.label == target_label:
                return path
            for child in node.children:
                queue.append((child, path + [child.label]))
        return None

    def from_pov(self, from_node):
        # Create a copy to avoid modifying original tree
        cloned = self._clone()
        target = cloned._find_node(from_node)
        
        if not target:
            raise ValueError("Tree could not be reoriented")

        if target == cloned:
            return cloned

        # Find path from cloned root to target
        queue = [(cloned, [cloned])]
        found = False
        path = []
        while queue and not found:
            node, current_path = queue.pop(0)
            if node == target:
                path = current_path
                found = True
                break
            for child in node.children:
                queue.append((child, current_path + [child]))

        if not found:
            raise ValueError("Tree could not be reoriented")

        # Reparent along the path
        for i in range(len(path) - 1):
            current = path[i]
            next_node = path[i+1]
            # Remove next_node from current's children
            current.children.remove(next_node)
            # Add current to next_node's children
            next_node.children.append(current)

        # The new root is the target node
        return target

    def path_to(self, from_node, to_node):
        # Reorient tree from from_node's perspective
        reoriented = self.from_pov(from_node)

        # Find path in reoriented tree
        path = reoriented._find_path(to_node)
        if not path:
            raise ValueError("No path found")
        return path
