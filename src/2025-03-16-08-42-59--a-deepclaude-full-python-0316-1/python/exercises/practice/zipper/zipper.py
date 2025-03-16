class Zipper:
    def __init__(self, focus, path=None):
        self.focus = focus
        self.path = path or []

    @staticmethod
    def from_tree(tree):
        return Zipper(tree)

    def value(self):
        return self.focus["value"]

    def set_value(self, new_value):
        new_focus = dict(self.focus)
        new_focus["value"] = new_value
        return Zipper(new_focus, self.path)

    def left(self):
        if self.focus["left"] is None:
            return None
        
        breadcrumb = {
            "node": self.focus,
            "direction": "left"
        }
        return Zipper(self.focus["left"], self.path + [breadcrumb])

    def set_left(self, left_tree):
        new_focus = dict(self.focus)
        new_focus["left"] = left_tree
        return Zipper(new_focus, self.path)

    def right(self):
        if self.focus["right"] is None:
            return None
        
        breadcrumb = {
            "node": self.focus,
            "direction": "right"
        }
        return Zipper(self.focus["right"], self.path + [breadcrumb])

    def set_right(self, right_tree):
        new_focus = dict(self.focus)
        new_focus["right"] = right_tree
        return Zipper(new_focus, self.path)

    def up(self):
        if not self.path:
            return None
        
        last_breadcrumb = self.path[-1]
        parent = dict(last_breadcrumb["node"])
        
        if last_breadcrumb["direction"] == "left":
            parent["left"] = self.focus
        else:  # right
            parent["right"] = self.focus
            
        return Zipper(parent, self.path[:-1])

    def to_tree(self):
        if not self.path:
            return self.focus
            
        # Navigate all the way up to get the complete tree
        return self.up().to_tree()
