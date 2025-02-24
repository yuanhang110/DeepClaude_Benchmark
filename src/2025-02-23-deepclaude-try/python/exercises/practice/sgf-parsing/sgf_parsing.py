class SgfTree:
    def __init__(self, properties=None, children=None):
        self.properties = properties or {}
        self.children = children or []

    def __eq__(self, other):
        if not isinstance(other, SgfTree):
            return False
        for key, value in self.properties.items():
            if key not in other.properties:
                return False
            if other.properties[key] != value:
                return False
        for key in other.properties.keys():
            if key not in self.properties:
                return False
        if len(self.children) != len(other.children):
            return False
        for child, other_child in zip(self.children, other.children):
            if child != other_child:
                return False
        return True

    def __ne__(self, other):
        return not self == other


def parse(input_string):
    def parse_properties(content, index):
        """Parse SGF properties starting at index, return (properties, new_index)"""
        properties = {}
        while index < len(content):
            char = content[index]
            if char in ';()':
                break
            # Parse key
            if not char.isupper():
                if char == '[':
                    raise ValueError("properties without delimiter")
                raise ValueError("property must be in uppercase")
            
            key_end = index
            while key_end < len(content) and content[key_end].isupper():
                key_end += 1
            key = content[index:key_end]
            if not key.isupper():
                raise ValueError("property must be in uppercase")
            index = key_end
            
            # Parse values
            if index >= len(content) or content[index] != '[':
                raise ValueError("properties without delimiter")
            
            values = []
            while index < len(content) and content[index] == '[':
                index += 1
                value_end = index
                escaped = False
                while value_end < len(content):
                    if escaped:
                        escaped = False
                        value_end += 1
                    else:
                        if content[value_end] == '\\':
                            escaped = True
                            value_end += 1
                        elif content[value_end] == ']':
                            break
                        else:
                            value_end += 1
                    if value_end >= len(content):
                        raise ValueError("property without delimiter")
                
                # Process value according to SGF Text rules
                raw_value = content[index:value_end]
                processed = []
                i = 0
                while i < len(raw_value):
                    if raw_value[i] == '\\':
                        if i + 1 < len(raw_value):
                            next_char = raw_value[i + 1]
                            if next_char == '\n':
                                i += 2
                                continue
                            # Convert escaped whitespace to space
                            if next_char.isspace():
                                processed.append(' ')
                            else:
                                processed.append(next_char)
                            i += 2
                        else:
                            processed.append('\\')
                            i += 1
                    elif raw_value[i] == '\n':
                        processed.append('\n')
                        i += 1
                    elif raw_value[i].isspace():
                        processed.append(' ')
                        i += 1
                    else:
                        processed.append(raw_value[i])
                        i += 1
                
                values.append(''.join(processed))
                index = value_end + 1
            
            if not values:
                raise ValueError("property without delimiter")
            properties[key] = values
        
        return properties, index

    def parse_node(content, index):
        """Parse a single node starting at index, return (node, new_index)"""
        if index >= len(content) or content[index] != ';':
            raise ValueError("tree missing")
        
        index += 1  # Skip ';'
        properties, index = parse_properties(content, index)
        return SgfTree(properties, []), index

    def parse_tree(content, index):
        """Parse a tree or subtree starting at index, return (tree, new_index)"""
        if index >= len(content):
            raise ValueError("tree missing")
            
        # Parse root node
        root, index = parse_node(content, index)
        
        # Parse children
        children = []
        while index < len(content):
            char = content[index]
            if char == '(':
                # Parse variation
                depth = 1
                start = index
                index += 1
                while index < len(content) and depth > 0:
                    if content[index] == '(':
                        depth += 1
                    elif content[index] == ')':
                        depth -= 1
                    index += 1
                if depth > 0:
                    raise ValueError("tree missing")
                subtree = parse(content[start:index])
                children.append(subtree)
            elif char == ';':
                # Parse sequential node
                node, new_index = parse_node(content, index)
                if not children:
                    children.append(node)
                else:
                    children[-1].children.append(node)
                index = new_index
            elif char == ')':
                break
            else:
                raise ValueError("tree missing")
        
        root.children = children
        return root, index

    # Main parsing logic
    if not input_string:
        raise ValueError("tree missing")
    
    if not input_string.startswith('(') or not input_string.endswith(')'):
        raise ValueError("tree missing")
    
    content = input_string[1:-1]
    if not content:
        raise ValueError("tree with no nodes")
    
    tree, index = parse_tree(content, 0)
    if index < len(content):
        raise ValueError("tree missing")
    
    return tree
