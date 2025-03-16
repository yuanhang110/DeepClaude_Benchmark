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
        self.dependents = []
        self.callbacks = []
        self._previous_value = None
        self._value = None
        
        # Register this cell as dependent of each input
        for input_cell in inputs:
            if hasattr(input_cell, 'dependents'):
                input_cell.dependents.append(self)
        
        # Calculate initial value
        self.update()
    
    @property
    def value(self):
        return self._value
    
    def update(self):
        new_value = self.compute_function([input_cell.value for input_cell in self.inputs])
        
        if self._value != new_value:
            # Store previous value before updating
            self._previous_value = self._value
            self._value = new_value
            
            # Notify dependents about the change
            for dependent in self.dependents:
                dependent.update()
            
            # Call callbacks if this is not the initial calculation
            if self._previous_value is not None:
                for callback in self.callbacks[:]:  # Use a copy of the list to avoid issues if callbacks modify the list
                    callback(new_value)

    def add_callback(self, callback):
        self.callbacks.append(callback)

    def remove_callback(self, callback):
        if callback in self.callbacks:
            self.callbacks.remove(callback)
