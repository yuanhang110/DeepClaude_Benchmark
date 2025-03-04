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
    if not input_string:
        raise ValueError("tree missing")
    
    if not (input_string.startswith('(') and input_string.endswith(')')):
        raise ValueError("tree missing")
    
    input_string = input_string[1:-1]  # Remove outer parentheses
    
    if not input_string:
        raise ValueError("tree with no nodes")
    
    def parse_properties(s):
        props = {}
        while s:
            if not s[0].isupper():
                if s[0] == ';' or s[0] == '(':
                    break
                raise ValueError("property must be in uppercase")
            
            key = ''
            while s and s[0].isupper():
                key += s[0]
                s = s[1:]
                
            if not s or s[0] != '[':
                raise ValueError("properties without delimiter")
                
            values = []
            while s and s[0] == '[':
                s = s[1:]
                value = ''
                escaped = False
                while s and (escaped or s[0] != ']'):
                    if escaped:
                        value += s[0]
                        escaped = False
                    else:
                        if s[0] == '\\':
                            escaped = True
                        else:
                            if s[0] == '\n':
                                if value and value[-1] == '\\':
                                    value = value[:-1]
                            value += s[0]
                    s = s[1:]
                    
                if not s:
                    raise ValueError("properties without delimiter")
                s = s[1:]  # Skip closing bracket
                
                # Convert tabs to spaces but preserve newlines
                value = value.replace('\t', ' ')
                values.append(value)
                
            props[key] = values
            
        return props, s
    
    def parse_node(s):
        if not s or s[0] != ';':
            return None, s
        s = s[1:]  # Skip semicolon
        props, s = parse_properties(s)
        return SgfTree(props), s
    
    def parse_sequence(s):
        nodes = []
        while s:
            if s[0] == '(':
                break
            node, s = parse_node(s)
            if not node:
                break
            nodes.append(node)
        return nodes, s
    
    def parse_tree(s):
        if not s or s[0] != '(':
            return None, s
        s = s[1:]  # Skip opening paren
        
        nodes, s = parse_sequence(s)
        if not nodes:
            return None, s
            
        # Handle children
        children = []
        while s and s[0] == '(':
            child, s = parse_tree(s)
            if child:
                children.append(child)
                
        if not s or s[0] != ')':
            raise ValueError("tree missing")
        s = s[1:]  # Skip closing paren
        
        # Link nodes
        for i in range(len(nodes) - 1):
            nodes[i].children = [nodes[i + 1]]
        if children:
            nodes[-1].children = children
            
        return nodes[0], s
    
    # Parse the root tree
    root, remaining = parse_tree(input_string)
    if not root:
        raise ValueError("tree with no nodes")
    if remaining:
        raise ValueError("tree missing")
    
    return root
