class EmptyListException(Exception):
    """Exception raised when attempting to access elements from an empty linked list."""
    def __init__(self, message="The linked list is empty."):
        self.message = message
        super().__init__(self.message)


class Node:
    def __init__(self, value, next_node=None):
        self._value = value
        self._next = next_node

    def value(self):
        return self._value

    def next(self):
        return self._next


class LinkedList:
    def __init__(self, values=None):
        self._head = None
        self._length = 0
        
        # If values are provided, add them to the list
        if values is not None:
            # Convert to list if it's not already (e.g., if it's a range)
            values_list = list(values)
            # Add in order so the last element becomes the head (LIFO)
            for value in values_list:
                self.push(value)

    def __iter__(self):
        """Allow iteration through the linked list."""
        current = self._head
        while current is not None:
            yield current.value()
            current = current.next()

    def __len__(self):
        """Return the length of the linked list."""
        return self._length

    def head(self):
        """Return the node at the head of the list."""
        if self._head is None:
            raise EmptyListException("The list is empty.")
        return self._head

    def push(self, value):
        """Add a new value to the beginning of the list."""
        new_node = Node(value, self._head)
        self._head = new_node
        self._length += 1

    def pop(self):
        """Remove and return the value at the beginning of the list."""
        if self._head is None:
            raise EmptyListException("The list is empty.")
        
        value = self._head.value()
        self._head = self._head.next()
        self._length -= 1
        return value

    def reversed(self):
        """Return a new LinkedList with elements in reverse order."""
        values = list(self)
        return LinkedList(reversed(values))
