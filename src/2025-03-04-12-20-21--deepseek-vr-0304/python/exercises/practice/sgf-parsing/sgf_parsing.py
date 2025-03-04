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
    if len(input_string) < 2 or input_string[0] != '(' or input_string[-1] != ')':
        if input_string == '()':
            raise ValueError("tree with no nodes")
        else:
            raise ValueError("tree missing")
    content = input_string[1:-1]
    if not content:
        raise ValueError("tree with no nodes")
    index = 0
    # Parse the root node
    if content[index] != ';':
        raise ValueError("tree missing")
    root, index = parse_node(content, index)
    # Parse children
    children = []
    while index < len(content):
        if content[index] == '(':
            # Find the matching closing parenthesis
            start = index
            depth = 1
            index += 1
            while index < len(content) and depth > 0:
                if content[index] == '(':
                    depth +=1
                elif content[index] == ')':
                    depth -=1
                index +=1
            if depth != 0:
                raise ValueError("tree missing")
            child_str = content[start:index]
            child = parse(child_str)
            children.append(child)
        else:
            raise ValueError(f"Unexpected character: {content[index]}")
    root.children = children
    return root

def parse_node(s, index):
    properties = {}
    index +=1  # skip the ';'
    while index < len(s):
        # Parse key
        key_start = index
        while index < len(s) and s[index].isupper():
            index +=1
        key = s[key_start:index]
        if not key:
            break  # no more keys
        # Check key is uppercase
        if not key.isupper():
            raise ValueError("property must be in uppercase")
        # Check for values
        values = []
        while index < len(s) and s[index] == '[':
            index +=1
            value = []
            escaped = False
            while index < len(s):
                c = s[index]
                if escaped:
                    if c.isspace():
                        if c == '\n':
                            # Escaped newline: skip it
                            pass
                        else:
                            # Convert to space
                            value.append(' ')
                    else:
                        value.append(c)
                    escaped = False
                    index +=1
                elif c == '\\':
                    escaped = True
                    index +=1
                elif c == ']':
                    break
                else:
                    # Convert whitespace (except newline) to space
                    if c.isspace() and c != '\n':
                        value.append(' ')
                    else:
                        value.append(c)
                    index +=1
            if index >= len(s) or s[index] != ']':
                raise ValueError("properties without delimiter")
            index +=1  # consume ']'
            values.append(''.join(value))
        if not values:
            raise ValueError("properties without delimiter")
        properties[key] = values
        # Check if next character is ';' or '(' or end
        if index < len(s) and s[index] == ';':
            # Another node in the same branch (child)
            # Parse it and add as a child
            child, index = parse_node(s, index)
            if not properties:
                # If no properties, replace with child
                properties = child.properties
                children = child.children
            else:
                # Add child as a child of this node
                children = [child]
        elif index < len(s) and s[index] == '(':
            # Child variation, handled by parse function
            break
        else:
            # End of node
            break
    return SgfTree(properties), index
