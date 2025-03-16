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
    
    # Check if the input forms a proper tree structure
    if not (input_string.startswith("(") and input_string.endswith(")")):
        raise ValueError("tree missing")
    
    # Remove outer parentheses
    content = input_string[1:-1]
    
    # Empty tree
    if not content:
        raise ValueError("tree with no nodes")
    
    # Tree must start with a node (which starts with semicolon)
    if not content.startswith(";"):
        raise ValueError("tree missing")
    
    # Parse the content and return the root node
    tree, _ = parse_node(content, 0)
    return tree

def parse_node(content, pos):
    """Parse a node and its children, return the node and the new position."""
    if content[pos] != ';':
        raise ValueError("tree missing")
    
    pos += 1  # Skip semicolon
    properties = {}
    children = []
    
    # Parse properties
    while pos < len(content) and content[pos] not in ";()":
        # Parse property key
        key_start = pos
        while pos < len(content) and content[pos].isalpha():
            pos += 1
        
        if pos == key_start:  # No key found
            break
            
        key = content[key_start:pos]
        
        # Validate property key is uppercase
        if not key.isupper():
            raise ValueError("property must be in uppercase")
        
        # Parse property values
        values = []
        while pos < len(content) and content[pos] == '[':
            pos += 1  # Skip opening bracket
            value, pos = parse_property_value(content, pos)
            values.append(value)
        
        if not values:
            raise ValueError("properties without delimiter")
        
        properties[key] = values
    
    # Parse children
    while pos < len(content):
        if content[pos] == ';':
            # Direct child node
            child, pos = parse_node(content, pos)
            children.append(child)
        elif content[pos] == '(':
            # Variation
            pos += 1  # Skip opening parenthesis
            depth = 1
            start = pos
            
            # Find matching closing parenthesis
            while pos < len(content) and depth > 0:
                if content[pos] == '(':
                    depth += 1
                elif content[pos] == ')':
                    depth -= 1
                pos += 1
            
            if depth != 0:
                raise ValueError("tree missing")
            
            # Parse the variation
            variation_content = content[start:pos-1]
            if variation_content and variation_content.startswith(';'):
                child, _ = parse_node(variation_content, 0)
                children.append(child)
        elif content[pos] == ')':
            # End of current node
            pos += 1
            break
        else:
            pos += 1
    
    return SgfTree(properties, children), pos

def parse_property_value(content, pos):
    """Parse a property value, handling escapes and whitespace conversion."""
    value = []
    escape = False
    
    while pos < len(content):
        char = content[pos]
        
        if escape:
            # Handle escaped character
            if char == '\n':
                # Escaped newline is removed
                pass
            elif char in " \t\v\f\r":
                # Escaped whitespace becomes space
                value.append(' ')
            else:
                # Any other escaped character is kept as is
                value.append(char)
            escape = False
        elif char == '\\':
            # Start of escape sequence
            escape = True
        elif char == ']':
            # End of property value
            pos += 1
            break
        elif char in "\t\v\f\r":
            # Convert tabs and other whitespace to space
            value.append(' ')
        else:
            # Keep other characters as is
            value.append(char)
        
        pos += 1
    
    return ''.join(value), pos
