def _parse_text(value):
    result = []
    escape = False
    for c in value:
        if escape:
            if c == '\n':
                # Remove escaped newline completely
                pass
            elif c in ('\t', '\r', '\f', '\v', ' '):
                # Escaped whitespace becomes single space
                result.append(' ')
            else:
                # Escaped non-whitespace character
                result.append(c)
            escape = False
        elif c == '\\':
            escape = True
        else:
            if c in ('\t', '\r', '\f', '\v', ' '):
                # Convert whitespace to space
                result.append(' ')
            elif c == '\n':
                # Keep unescaped newlines
                result.append('\n')
            else:
                result.append(c)
    # Handle trailing backslash that wasn't escaped
    if escape:
        result.append('\\')
    return ''.join(result).rstrip()

def _parse_properties(s, pos):
    props = {}
    while pos < len(s):
        if s[pos] == ';' or s[pos] == '(':
            break
            
        # Get key
        key_end = pos
        while key_end < len(s) and s[key_end] != '[':
            key_end += 1
            
        if key_end == pos or key_end >= len(s):
            raise ValueError("properties without delimiter")
            
        key = s[pos:key_end]
        if not key.isupper():
            raise ValueError("property must be in uppercase")
            
        pos = key_end
        values = []
        
        while pos < len(s) and s[pos] == '[':
            pos += 1
            value_start = pos
            while pos < len(s):
                if s[pos] == '\\':
                    pos += 2  # Skip escaped character
                elif s[pos] == ']':
                    break
                else:
                    pos += 1
                    
            if pos >= len(s):
                raise ValueError("properties without delimiter")
                
            values.append(_parse_text(s[value_start:pos]))
            pos += 1
            
        if not values:
            raise ValueError("properties without delimiter")
            
        props[key] = values
        
    return props, pos

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
    if input_string[0] != '(' or input_string[-1] != ')':
        raise ValueError("tree missing")
        
    content = input_string[1:-1].strip()
    if not content:
        raise ValueError("tree with no nodes")
        
    nodes = []
    pos = 0
    
    while pos < len(content):
        if content[pos] != ';':
            raise ValueError("tree missing")
            
        pos += 1
        props, pos = _parse_properties(content, pos)
        
        children = []
        # Look for child trees
        while pos < len(content) and content[pos] == '(':
            end = pos + 1
            depth = 1
            while end < len(content):
                if content[end] == '(':
                    depth += 1
                elif content[end] == ')':
                    depth -= 1
                if depth == 0:
                    break
                end += 1
                
            if depth != 0:
                raise ValueError("tree missing")
                
            child_str = content[pos:end+1]
            children.append(parse(child_str))
            pos = end + 1
        
        nodes.append(SgfTree(props, children))
        
    if not nodes:
        raise ValueError("tree with no nodes")
        
    root = nodes[0]
    # Add any sibling nodes as children of root
    if len(nodes) > 1:
        root.children.extend(nodes[1:])
        
    return root
