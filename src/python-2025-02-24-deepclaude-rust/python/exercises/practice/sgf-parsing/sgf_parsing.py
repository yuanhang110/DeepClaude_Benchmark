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
    def process_text(value):
        processed = []
        escape = False
        for c in value:
            if escape:
                if c == '\n':
                    pass  # remove escaped newline
                elif c in ('\t', '\r', '\f', '\v', ' '):
                    processed.append(' ')  # convert escaped whitespace to space
                else:
                    processed.append(c)
                escape = False
            else:
                if c == '\\':
                    escape = True
                else:
                    if c in ('\t', '\r', '\f', '\v', ' '):
                        processed.append(' ')
                    elif c == '\n':
                        processed.append(c)
                    else:
                        processed.append(c)
        return ''.join(processed)

    def parse_node(s):
        s = s.lstrip()
        if not s.startswith(';'):
            return None, s
        s = s[1:].lstrip()
        properties = {}
        # Allow nodes without properties
        if not s or s.startswith(';') or s.startswith('('):
            return SgfTree(properties), s
            
        while s and not s.startswith(';') and not s.startswith('('):
            # Check for uppercase property name
            key_end = 0
            while key_end < len(s) and s[key_end].isupper():
                key_end += 1
            # Verify no lowercase letters before [
            rest = s[key_end:].lstrip()
            if not rest.startswith('['):
                if any(c.islower() for c in s[:key_end]):
                    raise ValueError("property must be in uppercase")
                raise ValueError("properties without delimiter")
            if key_end == 0:
                raise ValueError("property must be in uppercase")
            key = s[:key_end]
            s = rest
            if not s.startswith('['):
                raise ValueError("properties without delimiter")
            values = []
            while s.startswith('['):
                s = s[1:]
                end = s.find(']')
                if end == -1:
                    raise ValueError("properties without delimiter")
                value = process_text(s[:end])
                values.append(value)
                s = s[end+1:].lstrip()
            properties[key] = values
            if not s:
                break
            if s[0] not in (';', '('):
                break
        return SgfTree(properties), s

    def parse_group(s):
        s = s.lstrip()
        if not s:
            raise ValueError("tree with no nodes")
        try:
            node, remaining = parse_node(s)
        except ValueError as e:
            if "properties without delimiter" in str(e):
                raise
            elif "property must be in uppercase" in str(e):
                raise
            else:
                raise ValueError("tree with no nodes") from e
        current = node
        while True:
            remaining = remaining.lstrip()
            if not remaining:
                break
            if remaining.startswith(';'):
                try:
                    next_node, remaining = parse_node(remaining)
                except ValueError as e:
                    if "properties without delimiter" in str(e):
                        raise
                    elif "property must be in uppercase" in str(e):
                        raise
                    else:
                        raise
                current.children.append(next_node)
                current = next_node
            elif remaining.startswith('('):
                depth = 1
                pos = 1
                while pos < len(remaining) and depth > 0:
                    if remaining[pos] == '(':
                        depth += 1
                    elif remaining[pos] == ')':
                        depth -= 1
                    pos += 1
                if depth != 0:
                    raise ValueError("tree missing")
                child_content = remaining[1:pos-1]
                try:
                    child_node, _ = parse_group(child_content)
                except ValueError as e:
                    if "tree with no nodes" in str(e):
                        raise ValueError("tree missing")
                    else:
                        raise
                current.children.append(child_node)
                remaining = remaining[pos:].lstrip()
            else:
                break
        return node, remaining

    if not input_string:
        raise ValueError("tree missing")
    if len(input_string) < 2 or input_string[0] != '(' or input_string[-1] != ')':
        raise ValueError("tree missing")
    content = input_string[1:-1].strip()
    if not content:
        raise ValueError("tree with no nodes")
    try:
        root, remaining = parse_group(content)
    except ValueError as e:
        if str(e) == "tree with no nodes":
            raise ValueError("tree missing") from e
        else:
            raise
    remaining = remaining.strip()
    if remaining and not remaining.startswith('('):
        raise ValueError("tree missing")
    return root
