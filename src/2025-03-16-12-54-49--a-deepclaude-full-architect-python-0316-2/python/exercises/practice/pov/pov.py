from json import dumps


class Tree:
    def __init__(self, label, children=None):
        self.label = label
        self.children = children if children is not None else []
        
    def clone(self):
        """Create a deep copy of the tree"""
        return Tree(self.label, [child.clone() for child in self.children])

    def __dict__(self):
        return {self.label: [c.__dict__() for c in sorted(self.children)]}

    def __str__(self, indent=None):
        return dumps(self.__dict__(), indent=indent)

    def __lt__(self, other):
        return self.label < other.label

    def __eq__(self, other):
        return self.__dict__() == other.__dict__()
        
    def _find_node(self, target_label):
        """Find a node with the given label in the tree"""
        if self.label == target_label:
            return self
            
        for child in self.children:
            found = child._find_node(target_label)
            if found:
                return found
                
        return None
        
    def _find_path(self, target_label):
        """Find path from self to node with target_label"""
        if self.label == target_label:
            return [self]
            
        for child in self.children:
            path = child._find_path(target_label)
            if path:
                return [self] + path
                
        return None

    def from_pov(self, from_node):
        """Reorient the tree with from_node as the new root"""
        # If current node is already the requested root, return a copy
        if self.label == from_node:
            return self.clone()
            
        # Clone the tree to avoid modifying the original
        cloned = self.clone()
        
        # Find path from root to target node
        path = cloned._find_path(from_node)
        if not path:
            raise ValueError(f"Tree could not be reoriented")
            
        # The last node in the path is our new root
        new_root = path[-1]
        
        # Reverse the path from new_root to old_root
        current = new_root
        for i in range(len(path) - 2, -1, -1):
            parent = path[i]
            # Remove the current node from parent's children
            parent.children.remove(current)
            # Add parent as a child of current node
            current.children.append(parent)
            current = parent
            
        return new_root

    def path_to(self, from_node, to_node):
        """Find the path from from_node to to_node"""
        # First reorient the tree with from_node as root
        try:
            reoriented = self.from_pov(from_node)
        except ValueError:
            raise ValueError("No path found")
            
        # Find path from new root to target
        path = reoriented._find_path(to_node)
        if not path:
            raise ValueError("No path found")
            
        # Return the labels of nodes in the path
        return [node.label for node in path]
