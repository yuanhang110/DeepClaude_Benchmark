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
            self.propagate()
            
    def propagate(self):
        visited = set()
        to_process = self.dependents.copy()
        
        while to_process:
            cell = to_process.pop(0)
            if cell in visited:
                continue
            visited.add(cell)
            
            old_value = cell.value
            new_value = cell.compute()
            if old_value != new_value:
                cell.value = new_value
                to_process.extend(cell.dependents)
                cell.notify_callbacks(old_value)


class ComputeCell:
    def __init__(self, inputs, compute_function):
        self.inputs = inputs
        self.compute_function = compute_function
        self.callbacks = []
        self.dependents = []
        self.value = self.compute()
        
        # Register as dependent with all inputs
        for input_cell in self.inputs:
            input_cell.dependents.append(self)
            
    def compute(self):
        return self.compute_function([cell.value for cell in self.inputs])
        
    def add_callback(self, callback):
        self.callbacks.append(callback)
        
    def remove_callback(self, callback):
        if callback in self.callbacks:
            self.callbacks.remove(callback)
            
    def notify_callbacks(self, old_value):
        if self.value != old_value:
            current_value = self.value  # Capture current value before callbacks
            for callback in self.callbacks.copy():  # Iterate copy in case callbacks remove themselves
                callback(current_value)
    
