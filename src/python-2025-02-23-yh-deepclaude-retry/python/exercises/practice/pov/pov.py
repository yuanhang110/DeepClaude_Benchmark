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

    def from_pov(self, from_node):
        def find_path(node, target, path):
            if node.label == target:
                return path + [node]
            for child in node.children:
                result = find_path(child, target, path + [node])
                if result is not None:
                    return result
            return None

        path = find_path(self, from_node, [])
        if not path or path[-1].label != from_node:
            raise ValueError("Tree could not be reoriented")
        
        # Reverse the path to start from the target node
        reversed_path = list(reversed(path))
        new_root = Tree(reversed_path[0].label)
        current = new_root
        
        # Rebuild the tree by adding parents from the reversed path
        for i in range(1, len(reversed_path)):
            original_node = reversed_path[i]
            new_node = Tree(original_node.label)
            new_node.children.append(current)
            
            # Add other children of the original node except the next in the original path
            if i < len(reversed_path) - 1:
                next_in_path = reversed_path[i+1]
            else:
                next_in_path = None
                
            for child in original_node.children:
                if next_in_path is None or child.label != next_in_path.label:
                    new_node.children.append(Tree(child.label, child.children))
            
            new_node.children.sort()
            current = new_node
        
        return new_root

    def path_to(self, from_node, to_node):
        # First reorient the tree to get the new root
        try:
            new_tree = self.from_pov(from_node)
        except ValueError:
            raise ValueError("No path found")
            
        # Now find path from new root to target node
        def find_path(node, target, path):
            new_path = path + [node.label]
            if node.label == target:
                return new_path
            for child in node.children:
                result = find_path(child, target, new_path)
                if result:
                    return result
            return None
        
        result = find_path(new_tree, to_node, [])
        if not result:
            raise ValueError("No path found")
        return result
