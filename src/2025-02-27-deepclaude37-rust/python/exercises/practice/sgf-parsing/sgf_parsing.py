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
    if not input_string.startswith('(') or not input_string.endswith(')'):
        raise ValueError("tree missing")
    content = input_string[1:-1]
    if not content:
        raise ValueError("tree with no nodes")
    
    def parse_property(s, i):
        key_start = i
        while i < len(s) and s[i].isalpha():
            i += 1
        key = s[key_start:i]
        if not key:
            raise ValueError("properties without delimiter")
        if not key.isupper():
            raise ValueError("property must be in uppercase")
        
        values = []
        while i < len(s) and s[i] == '[':
            i += 1
            value = []
            escape = False
            while i < len(s) and (s[i] != ']' or escape):
                if i >= len(s):
                    raise ValueError("properties without delimiter")
                if escape:
                    if s[i] == '\n':
                        pass  # Escaped newline is removed
                    elif s[i].isspace():
                        value.append(' ')  # Other whitespace becomes space
                    else:
                        value.append(s[i])  # Any other char is kept as-is
                    escape = False
                else:
                    if s[i] == '\\':
                        escape = True
                    elif s[i] == '\n':
                        value.append('\n')  # Keep unescaped newlines
                    elif s[i].isspace():
                        value.append(' ')  # Other whitespace becomes space
                    else:
                        value.append(s[i])
                i += 1
            if i >= len(s) or s[i] != ']':
                raise ValueError("properties without delimiter")
            i += 1
            values.append(''.join(value))
        
        if not values:
            raise ValueError("properties without delimiter")
        return key, values, i

    def parse_node(s, i):
        if i >= len(s) or s[i] != ';':
            raise ValueError("tree missing")
        i += 1
        
        properties = {}
        while i < len(s) and s[i] not in (';', '(', ')'):
            key, values, i = parse_property(s, i)
            properties[key] = values
        
        children = []
        while i < len(s) and s[i] == '(':
            i += 1
            variation, i = parse_branch(s, i)
            if not variation:
                raise ValueError("tree with no nodes")
            if i >= len(s) or s[i] != ')':
                raise ValueError("tree missing")
            i += 1
            
            # Link nodes in the variation
            var_root = variation[0]
            current = var_root
            for child in variation[1:]:
                current.children.append(child)
                current = child
            
            children.append(var_root)
        
        return SgfTree(properties, children), i

    def parse_branch(s, i):
        nodes = []
        while i < len(s) and s[i] == ';':
            node, i = parse_node(s, i)
            nodes.append(node)
        return nodes, i

    # Parse main branch
    main_branch, index = parse_branch(content, 0)
    if index != len(content):
        raise ValueError("tree missing")
    if not main_branch:
        raise ValueError("tree with no nodes")
    
    # Link nodes in main branch
    root = main_branch[0]
    current = root
    for node in main_branch[1:]:
        current.children.append(node)
        current = node
    
    return root
