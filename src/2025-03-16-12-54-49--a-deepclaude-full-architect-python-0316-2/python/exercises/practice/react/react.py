class InputCell:
    def __init__(self, initial_value):
        self._value = initial_value
        self.subscribers = []
    
    @property
    def value(self):
        return self._value
    
    @value.setter
    def value(self, new_value):
        self.set_value(new_value)
    
    def set_value(self, new_value):
        if self._value == new_value:
            return
            
        self._value = new_value
        changed_cells = []
        for subscriber in self.subscribers:
            subscriber.update(changed_cells)
            
        # Execute callbacks after the system has stabilized
        for cell in changed_cells:
            cell.notify_callbacks()


class ComputeCell:
    def __init__(self, inputs, compute_function):
        self.inputs = inputs
        self.compute_function = compute_function
        self.callbacks = []
        self.subscribers = []
        
        # Subscribe to all input cells
        for input_cell in self.inputs:
            if hasattr(input_cell, 'subscribers'):
                input_cell.subscribers.append(self)
                
        # Calculate initial value
        self.value = self.compute_function([input_cell.value for input_cell in self.inputs])
        self.last_value = self.value

    def update(self, changed_cells):
        old_value = self.value
        new_value = self.compute_function([input_cell.value for input_cell in self.inputs])
        
        if new_value != old_value:
            self.value = new_value
            changed_cells.append(self)
            
            # Propagate changes to subscribers
            for subscriber in self.subscribers:
                subscriber.update(changed_cells)
    
    def notify_callbacks(self):
        if self.last_value != self.value:
            for callback in self.callbacks:
                callback(self.value)
            self.last_value = self.value

    def add_callback(self, callback):
        self.callbacks.append(callback)

    def remove_callback(self, callback):
        if callback in self.callbacks:
            self.callbacks.remove(callback)
