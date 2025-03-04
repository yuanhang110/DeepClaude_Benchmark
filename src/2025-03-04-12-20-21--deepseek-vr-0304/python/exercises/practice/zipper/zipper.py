class Zipper:
    def __init__(self, tree, breadcrumbs):
        self.tree = tree
        self.breadcrumbs = breadcrumbs

    @staticmethod
    def from_tree(tree):
        return Zipper(tree, [])

    def value(self):
        return self.tree['value']

    def set_value(self, value):
        new_tree = {'value': value, 'left': self.tree.get('left'), 'right': self.tree.get('right')}
        return Zipper(new_tree, self.breadcrumbs)

    def left(self):
        if self.tree.get('left') is None:
            return None
        return Zipper(
            self.tree['left'],
            [('left', self.tree['value'], self.tree.get('right'))] + self.breadcrumbs
        )

    def set_left(self, left):
        new_tree = {'value': self.tree['value'], 'left': left, 'right': self.tree.get('right')}
        return Zipper(new_tree, self.breadcrumbs)

    def right(self):
        if self.tree.get('right') is None:
            return None
        return Zipper(
            self.tree['right'],
            [('right', self.tree['value'], self.tree.get('left'))] + self.breadcrumbs
        )

    def set_right(self, right):
        new_tree = {'value': self.tree['value'], 'left': self.tree.get('left'), 'right': right}
        return Zipper(new_tree, self.breadcrumbs)

    def up(self):
        if not self.breadcrumbs:
            return None
        direction, value, other = self.breadcrumbs[0]
        if direction == 'left':
            new_tree = {'value': value, 'left': self.tree, 'right': other}
        else:
            new_tree = {'value': value, 'left': other, 'right': self.tree}
        return Zipper(new_tree, self.breadcrumbs[1:])

    def to_tree(self):
        current = self
        while current.breadcrumbs:
            current = current.up()
        return current.tree
