from json import dumps


class Tree:
    def __init__(self, label, children=None):
        self.label = label
        self.children = children if children is not None else []
        self.parent = None
        for child in self.children:
            child.parent = self

    def __dict__(self):
        return {self.label: [c.__dict__() for c in sorted(self.children)]}

    def __str__(self, indent=None):
        return dumps(self.__dict__(), indent=indent)

    def __lt__(self, other):
        return self.label < other.label

    def __eq__(self, other):
        return self.__dict__() == other.__dict__()

    def find_node(self, target_label):
        if self.label == target_label:
            return self
        for child in self.children:
            result = child.find_node(target_label)
            if result:
                return result
        return None

    def from_pov(self, from_node):
        new_root = self.find_node(from_node)
        if not new_root:
            raise ValueError("Tree could not be reoriented")
        
        def reparent(node):
            for child in node.children:
                child.parent = node
            if node.parent:
                node.parent.children.remove(node)
                node.children.append(node.parent)
                reparent(node.parent)
                node.parent = None
        
        reparent(new_root)
        return new_root

    def path_to(self, from_node, to_node):
        start = self.find_node(from_node)
        if not start:
            raise ValueError("Tree could not be reoriented")
        end = self.find_node(to_node)
        if not end:
            raise ValueError("No path found")
        
        # Build path from start to root
        path = []
        current = start
        while current:
            path.append(current.label)
            current = current.parent
        
        # Build path from end to root
        end_path = []
        current = end
        while current:
            end_path.append(current.label)
            current = current.parent
        
        # Find lowest common ancestor
        lca = None
        for i in range(min(len(path), len(end_path))):
            if path[len(path)-1-i] != end_path[len(end_path)-1-i]:
                break
            lca = path[len(path)-1-i]
        
        if not lca:
            raise ValueError("No path found")
        
        # Build final path
        start_index = path.index(lca)
        end_index = end_path.index(lca)
        
        # Path from start to LCA (excluding LCA)
        result = path[:start_index]
        # Add LCA
        result.append(lca)
        # Path from LCA to end (excluding LCA)
        result.extend(reversed(end_path[:end_index]))
        
        return result
