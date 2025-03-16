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

    def find_path(self, target_label):
        """Find path from current node to a node with target_label."""
        if self.label == target_label:
            return [self]
        for child in self.children:
            path = child.find_path(target_label)
            if path is not None:
                return [self] + path
        return None

    def clone_subtree(self):
        """Create a deep copy of this tree and its children."""
        cloned_children = [child.clone_subtree() for child in self.children]
        return Tree(self.label, cloned_children)

    def from_pov(self, from_node):
        """Reorient the tree with from_node as the new root."""
        # If current root is the target node, return a copy of self
        if self.label == from_node:
            return self.clone_subtree()
            
        # Find path from current root to from_node
        path = self.find_path(from_node)
        if not path:
            raise ValueError("Tree could not be reoriented")
            
        # Reverse the path to get order from target to original root
        reversed_path = list(reversed(path))
        
        # Create new tree with from_node as root
        new_root = Tree(reversed_path[0].label)
        current_new_node = new_root
        
        # Clone original children of target node (excluding next in path)
        original_target = reversed_path[0]
        next_in_path = reversed_path[1] if len(reversed_path) > 1 else None
        for child in original_target.children:
            if next_in_path is None or child.label != next_in_path.label:
                cloned_child = child.clone_subtree()
                new_root.children.append(cloned_child)
        
        # Build the new tree structure
        for i in range(len(reversed_path) - 1):
            original_current = reversed_path[i]
            original_next = reversed_path[i+1]
            
            # Create new node for the next node in reversed path
            new_child = Tree(original_next.label)
            current_new_node.children.append(new_child)
            
            # Collect and clone other children of original_next (excluding original_current)
            other_children = []
            for child in original_next.children:
                if child.label != original_current.label:
                    cloned_child = child.clone_subtree()
                    other_children.append(cloned_child)
            
            # Add the other children to the new child's children
            new_child.children.extend(other_children)
            
            # Move to next level in the new tree
            current_new_node = new_child
        
        return new_root

    def path_to(self, from_node, to_node):
        """Find the path from from_node to to_node."""
        # Reorient the tree to from_node's perspective - let original error propagate
        reoriented = self.from_pov(from_node)
        
        # Find path from from_node (now root) to to_node
        path = reoriented.find_path(to_node)
        if not path:
            raise ValueError("No path found")
            
        # Return just the labels of nodes in the path
        return [node.label for node in path]
