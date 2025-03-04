class InputCell:
    def __init__(self, initial_value):
        self._value = initial_value
        self.dependents = []

    @property
    def value(self):
        return self._value

    @value.setter
    def value(self, new_value):
        if self._value != new_value:
            self._value = new_value
            for dependent in self.dependents:
                dependent.update()


class ComputeCell:
    def __init__(self, inputs, compute_function):
        self.inputs = inputs
        self.compute_function = compute_function
        self.callbacks = []
        self.dependents = []
        self.value = self.compute_function([input.value for input in inputs])
        for input in inputs:
            input.dependents.append(self)

    def add_callback(self, callback):
        self.callbacks.append(callback)

    def remove_callback(self, callback):
        if callback in self.callbacks:
            self.callbacks.remove(callback)

    def update(self):
        new_value = self.compute_function([input.value for input in self.inputs])
        if new_value != self.value:
            self.value = new_value
            for callback in self.callbacks:
                callback(self.value)
            for dependent in self.dependents:
                dependent.update()
    
