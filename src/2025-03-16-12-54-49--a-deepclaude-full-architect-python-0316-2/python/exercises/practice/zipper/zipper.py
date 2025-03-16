class Zipper:
    def __init__(self, focus, path):
        self._focus = focus  # Current focused node
        self._path = path    # Path to parent nodes [(parent_node, direction)]

    @staticmethod
    def from_tree(tree):
        """Create a zipper focused on the root of the given tree"""
        return Zipper(tree, [])

    def value(self):
        """Get the value of the focus node"""
        return self._focus["value"]

    def set_value(self, new_value):
        """Set the value of the focus node, returns a new zipper"""
        new_focus = {"value": new_value, "left": self._focus["left"], "right": self._focus["right"]}
        return Zipper(new_focus, self._path)

    def left(self):
        """Move focus to the left child, returns a new zipper or None if no left child"""
        if self._focus["left"] is None:
            return None
        return Zipper(self._focus["left"], self._path + [(self._focus, 'left')])

    def set_left(self, new_left):
        """Set the left child of the focus node, returns a new zipper"""
        new_focus = {"value": self._focus["value"], "left": new_left, "right": self._focus["right"]}
        return Zipper(new_focus, self._path)

    def right(self):
        """Move focus to the right child, returns a new zipper or None if no right child"""
        if self._focus["right"] is None:
            return None
        return Zipper(self._focus["right"], self._path + [(self._focus, 'right')])

    def set_right(self, new_right):
        """Set the right child of the focus node, returns a new zipper"""
        new_focus = {"value": self._focus["value"], "left": self._focus["left"], "right": new_right}
        return Zipper(new_focus, self._path)

    def up(self):
        """Move focus to the parent, returns a new zipper or None if at root"""
        if not self._path:
            return None
            
        parent, direction = self._path[-1]
        new_path = self._path[:-1]
        
        if direction == 'left':
            new_parent = {"value": parent["value"], "left": self._focus, "right": parent["right"]}
        else:  # direction == 'right'
            new_parent = {"value": parent["value"], "left": parent["left"], "right": self._focus}
            
        return Zipper(new_parent, new_path)

    def to_tree(self):
        """Get the root tree from the zipper"""
        if not self._path:
            return self._focus
            
        # Navigate back to the root
        zipper = self
        while zipper._path:
            zipper = zipper.up()
        return zipper._focus

