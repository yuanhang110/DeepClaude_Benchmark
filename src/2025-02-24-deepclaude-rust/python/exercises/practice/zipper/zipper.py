class Zipper:
    def __init__(self, focus, crumbs):
        self.focus = focus
        self.crumbs = crumbs

    @staticmethod
    def from_tree(tree):
        return Zipper(tree, [])

    def value(self):
        return self.focus['value']

    def set_value(self, value):
        new_focus = {'value': value, 'left': self.focus.get('left'), 'right': self.focus.get('right')}
        return Zipper(new_focus, self.crumbs)

    def left(self):
        if not self.focus.get('left'):
            return None
        new_crumbs = [{'value': self.focus['value'], 'right': self.focus.get('right'), 'dir': 'left'}] + self.crumbs
        return Zipper(self.focus['left'], new_crumbs)

    def set_left(self, left):
        new_focus = {'value': self.focus['value'], 'left': left, 'right': self.focus.get('right')}
        return Zipper(new_focus, self.crumbs)

    def right(self):
        if not self.focus.get('right'):
            return None
        new_crumbs = [{'value': self.focus['value'], 'left': self.focus.get('left'), 'dir': 'right'}] + self.crumbs
        return Zipper(self.focus['right'], new_crumbs)

    def set_right(self, right):
        new_focus = {'value': self.focus['value'], 'left': self.focus.get('left'), 'right': right}
        return Zipper(new_focus, self.crumbs)

    def up(self):
        if not self.crumbs:
            return None
        crumb = self.crumbs[0]
        if crumb['dir'] == 'left':
            new_focus = {'value': crumb['value'], 'left': self.focus, 'right': crumb['right']}
        else:
            new_focus = {'value': crumb['value'], 'left': crumb['left'], 'right': self.focus}
        return Zipper(new_focus, self.crumbs[1:])

    def to_tree(self):
        current = self
        while current.crumbs:
            current = current.up()
        return current.focus
