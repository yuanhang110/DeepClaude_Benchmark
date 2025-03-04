class Zipper:
    def __init__(self, node, path=None):
        self.node = node
        self.path = path or []

    @staticmethod
    def from_tree(tree):
        return Zipper(tree)

    def value(self):
        return self.node['value'] if self.node else None

    def set_value(self, value):
        if not self.node:
            return self
        new_node = {'value': value, 'left': self.node.get('left'), 'right': self.node.get('right')}
        return self._make_zipper(new_node)

    def left(self):
        if not self.node or not self.node.get('left'):
            return None
        new_path = [{'dir': 'left', 'value': self.node['value'], 'right': self.node.get('right')}] + self.path
        return Zipper(self.node['left'], new_path)

    def set_left(self, left):
        if not self.node:
            return self
        new_node = {'value': self.node['value'], 'left': left, 'right': self.node.get('right')}
        return self._make_zipper(new_node)

    def right(self):
        if not self.node or not self.node.get('right'):
            return None
        new_path = [{'dir': 'right', 'value': self.node['value'], 'left': self.node.get('left')}] + self.path
        return Zipper(self.node['right'], new_path)

    def set_right(self, right):
        if not self.node:
            return self
        new_node = {'value': self.node['value'], 'left': self.node.get('left'), 'right': right}
        return self._make_zipper(new_node)

    def up(self):
        if not self.path:
            return None
        top = self.path[0]
        if top['dir'] == 'left':
            new_node = {'value': top['value'], 'left': self.node, 'right': top.get('right')}
        else:
            new_node = {'value': top['value'], 'left': top.get('left'), 'right': self.node}
        return Zipper(new_node, self.path[1:])

    def to_tree(self):
        zipper = self
        while zipper.path:
            zipper = zipper.up()
        return zipper.node

    def _make_zipper(self, new_node):
        if not self.path:
            return Zipper(new_node)
        parent = self.path[0]
        if parent['dir'] == 'left':
            return Zipper(new_node, self.path).up()
        else:
            return Zipper(new_node, self.path).up()
