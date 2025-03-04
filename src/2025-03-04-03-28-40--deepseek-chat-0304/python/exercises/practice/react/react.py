class InputCell:
    def __init__(self, initial_value):
        self._value = initial_value
        self._dependents = []

    @property
    def value(self):
        return self._value

    @value.setter
    def value(self, new_value):
        if self._value != new_value:
            self._value = new_value
            for cell in self._dependents:
                cell._update()

    def _add_dependent(self, cell):
        self._dependents.append(cell)


class ComputeCell:
    def __init__(self, inputs, compute_function):
        self._inputs = inputs
        self._compute = compute_function
        self._callbacks = set()
        self._dependents = []
        self._value = None
        
        # Register as dependent of all inputs
        for input_cell in inputs:
            input_cell._add_dependent(self)
            
        # Compute initial value
        self._update()

    @property
    def value(self):
        return self._value

    def _update(self):
        # Get current input values
        input_values = [input_cell.value for input_cell in self._inputs]
        # Compute new value
        new_value = self._compute(input_values)
        
        # Only update and notify if value changed
        if new_value != self._value:
            old_value = self._value
            self._value = new_value
            # Notify callbacks
            for callback in self._callbacks:
                callback(new_value)
            # Propagate to dependents
            for dependent in self._dependents:
                dependent._update()

    def add_callback(self, callback):
        self._callbacks.add(callback)

    def remove_callback(self, callback):
        self._callbacks.discard(callback)

    def _add_dependent(self, cell):
        self._dependents.append(cell)
    
