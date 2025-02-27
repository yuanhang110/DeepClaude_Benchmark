class InputCell:
    def __init__(self, initial_value):
        self._value = initial_value
        self.dependents = set()

    @property
    def value(self):
        return self._value

    @value.setter
    def value(self, new_value):
        if self._value == new_value:
            return
        self._value = new_value
        changed = set()
        for dependent in self.dependents:
            dependent.recalculate(changed)
        for cell in changed:
            for callback in cell.callbacks:
                callback(cell.value)


class ComputeCell:
    def __init__(self, inputs, compute_function):
        self.inputs = inputs
        self.compute_function = compute_function
        self.value = self.compute_function([input.value for input in inputs])
        self.dependents = set()
        self.callbacks = []
        for input in inputs:
            input.dependents.add(self)

    def recalculate(self, changed):
        new_value = self.compute_function([input.value for input in self.inputs])
        if new_value != self.value:
            self.value = new_value
            changed.add(self)
            for dependent in self.dependents:
                dependent.recalculate(changed)

    def add_callback(self, callback):
        self.callbacks.append(callback)

    def remove_callback(self, callback):
        if callback in self.callbacks:
            self.callbacks.remove(callback)
    
