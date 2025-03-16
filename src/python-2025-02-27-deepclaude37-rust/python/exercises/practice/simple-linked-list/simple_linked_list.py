class EmptyListException(Exception):
    def __init__(self, message):
        super().__init__(message)


class Node:
    def __init__(self, value):
        self._value = value
        self._next = None

    def value(self):
        return self._value

    def next(self):
        return self._next


class LinkedList:
    def __init__(self, values=None):
        self.head_node = None
        self._length = 0
        if values is not None:
            for value in values:
                self.push(value)

    def __iter__(self):
        current = self.head_node
        while current is not None:
            yield current.value()
            current = current.next()

    def __len__(self):
        return self._length

    def head(self):
        if self.head_node is None:
            raise EmptyListException("The list is empty.")
        return self.head_node

    def push(self, value):
        new_node = Node(value)
        new_node._next = self.head_node
        self.head_node = new_node
        self._length += 1

    def pop(self):
        if self.head_node is None:
            raise EmptyListException("The list is empty.")
        value = self.head_node.value()
        self.head_node = self.head_node.next()
        self._length -= 1
        return value

    def reversed(self):
        reversed_list = LinkedList()
        current = self.head_node
        while current is not None:
            reversed_list.push(current.value())
            current = current.next()
        return reversed_list
