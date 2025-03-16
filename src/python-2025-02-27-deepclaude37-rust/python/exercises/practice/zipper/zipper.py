class Zipper:
    def __init__(self, value, left, right, path):
        self._value = value
        self._left = left
        self._right = right
        self._path = path

    @staticmethod
    def from_tree(tree):
        """Create a zipper focused on the root node of a tree"""
        if tree is None:
            return None
        return Zipper(tree['value'], tree['left'], tree['right'], [])

    def value(self):
        """Get the value of the focus node"""
        return self._value

    def set_value(self, new_value):
        """Set the value of the focus node, returns a new zipper"""
        return Zipper(new_value, self._left, self._right, self._path)

    def left(self):
        """Move focus to the left child, returns a new zipper"""
        if self._left is None:
            return None
        return Zipper(
            self._left['value'],
            self._left['left'],
            self._left['right'],
            self._path + [{'dir': 'left', 'value': self._value, 'right': self._right}]
        )

    def set_left(self, left):
        """Set the left child of the focus node, returns a new zipper"""
        return Zipper(self._value, left, self._right, self._path)

    def right(self):
        """Move focus to the right child, returns a new zipper"""
        if self._right is None:
            return None
        return Zipper(
            self._right['value'],
            self._right['left'],
            self._right['right'],
            self._path + [{'dir': 'right', 'value': self._value, 'left': self._left}]
        )

    def set_right(self, right):
        """Set the right child of the focus node, returns a new zipper"""
        return Zipper(self._value, self._left, right, self._path)

    def up(self):
        """Move focus to the parent, returns a new zipper"""
        if not self._path:
            return None
        
        parent = self._path[-1]
        # Build current node as dictionary
        current_node = {
            'value': self._value,
            'left': self._left,
            'right': self._right
        }
        
        if parent['dir'] == 'left':
            return Zipper(
                parent['value'],
                current_node,  # Current node becomes left child of parent
                parent['right'],
                self._path[:-1]
            )
        else:  # dir == 'right'
            return Zipper(
                parent['value'],
                parent['left'],
                current_node,  # Current node becomes right child of parent
                self._path[:-1]
            )

    def to_tree(self):
        """Get the root tree from the zipper"""
        # Navigate to the root
        current = self
        while current._path:
            current = current.up()
        
        # Return the full tree
        return {
            'value': current._value,
            'left': current._left,
            'right': current._right
        }
