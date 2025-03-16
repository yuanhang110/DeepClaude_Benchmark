class Zipper:
    def __init__(self, focus, path):
        self.focus = focus
        self.path = path

    @staticmethod
    def from_tree(tree):
        return Zipper(tree, [])

    def value(self):
        return self.focus['value']

    def set_value(self, value):
        new_focus = {'value': value, 'left': self.focus['left'], 'right': self.focus['right']}
        return Zipper(new_focus, self.path)

    def left(self):
        if self.focus['left'] is None:
            return None
        new_focus = self.focus['left']
        new_path = [('left', self.focus['value'], self.focus['right'])] + self.path
        return Zipper(new_focus, new_path)

    def set_left(self, subtree):
        new_focus = {'value': self.focus['value'], 'left': subtree, 'right': self.focus['right']}
        return Zipper(new_focus, self.path)

    def right(self):
        if self.focus['right'] is None:
            return None
        new_focus = self.focus['right']
        new_path = [('right', self.focus['value'], self.focus['left'])] + self.path
        return Zipper(new_focus, new_path)

    def set_right(self, subtree):
        new_focus = {'value': self.focus['value'], 'left': self.focus['left'], 'right': subtree}
        return Zipper(new_focus, self.path)

    def up(self):
        if not self.path:
            return None
        direction, parent_val, sibling = self.path[0]
        if direction == 'left':
            new_focus = {'value': parent_val, 'left': self.focus, 'right': sibling}
        else:
            new_focus = {'value': parent_val, 'left': sibling, 'right': self.focus}
        return Zipper(new_focus, self.path[1:])

    def to_tree(self):
        z = self
        while z.path:
            z = z.up()
        return z.focus
