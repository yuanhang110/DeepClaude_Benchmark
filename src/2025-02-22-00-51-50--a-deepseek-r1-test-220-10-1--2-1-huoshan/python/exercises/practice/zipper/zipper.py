class Zipper:
    def __init__(self, node, crumbs):
        self._node = node
        self._crumbs = crumbs

    @staticmethod
    def from_tree(tree):
        return Zipper(tree, [])

    def value(self):
        return self._node['value']

    def set_value(self, value):
        new_node = {'value': value, 'left': self._node['left'], 'right': self._node['right']}
        return Zipper(new_node, self._crumbs)

    def left(self):
        if not self._node.get('left'):
            return None
        return Zipper(
            self._node['left'],
            self._crumbs + [{'dir': 'left', 'value': self._node['value'], 'right': self._node['right']}]
        )

    def set_left(self, left):
        new_node = {'value': self._node['value'], 'left': left, 'right': self._node['right']}
        return Zipper(new_node, self._crumbs)

    def right(self):
        if not self._node.get('right'):
            return None
        return Zipper(
            self._node['right'],
            self._crumbs + [{'dir': 'right', 'value': self._node['value'], 'left': self._node['left']}]
        )

    def set_right(self, right):
        new_node = {'value': self._node['value'], 'left': self._node['left'], 'right': right}
        return Zipper(new_node, self._crumbs)

    def up(self):
        if not self._crumbs:
            return None
            
        parent_crumb = self._crumbs[-1]
        parent_node = {'value': parent_crumb['value']}
        
        if parent_crumb['dir'] == 'left':
            parent_node['left'] = self._node
            parent_node['right'] = parent_crumb['right']
        else:
            parent_node['left'] = parent_crumb['left']
            parent_node['right'] = self._node
            
        return Zipper(parent_node, self._crumbs[:-1])

    def to_tree(self):
        current = self
        while current._crumbs:
            current = current.up()
        return current._node
