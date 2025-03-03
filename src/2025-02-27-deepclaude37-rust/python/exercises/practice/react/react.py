class InputCell:
    def __init__(self, initial_value):
        self._value = initial_value
        self.observers = []

    @property
    def value(self):
        return self._value

    @value.setter
    def value(self, new_value):
        if self._value != new_value:
            self._value = new_value
            for observer in self.observers[:]:
                observer.notify()


class ComputeCell:
    def __init__(self, inputs, compute_function):
        self.inputs = inputs
        self.compute_function = compute_function
        self.callbacks = []
        self.observers = []
        self._value = None  # Initialize before computing to avoid potential recursion
        self._value = self.compute()  # Now compute initial value
        for cell in self.inputs:  # Add as observer to all inputs
            cell.observers.append(self)

    @property
    def value(self):
        return self._value

    def compute(self):
        input_values = [cell.value for cell in self.inputs]
        return self.compute_function(input_values)

    def notify(self):
        previous_value = self._value
        new_value = self.compute()
        if new_value != previous_value:
            self._value = new_value
            for callback in self.callbacks[:]:
                callback(new_value)
            for observer in self.observers[:]:
                observer.notify()

    def add_callback(self, callback):
        self.callbacks.append(callback)

    def remove_callback(self, callback):
        if callback in self.callbacks:
            self.callbacks.remove(callback)
    
