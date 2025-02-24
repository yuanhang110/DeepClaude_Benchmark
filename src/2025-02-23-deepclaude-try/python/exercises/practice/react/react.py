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
            # First collect all cells that might need updating
            from queue import Queue
            all_cells = set()
            queue = Queue()
            
            # Seed queue with direct observers
            for observer in self.observers:
                queue.put(observer)
                all_cells.add(observer)
            
            # Expand to all downstream cells
            while not queue.empty():
                cell = queue.get()
                for obs in cell.observers:
                    if obs not in all_cells:
                        all_cells.add(obs)
                        queue.put(obs)
            
            # Capture initial values before any changes
            initial_values = {cell: cell.value for cell in all_cells}
            
            # Process updates in dependency order
            processed = set()
            queue = Queue()
            for observer in self.observers:
                queue.put(observer)
            
            while not queue.empty():
                cell = queue.get()
                if cell in processed:
                    continue
                processed.add(cell)
                
                new_val = cell.compute_function([inp.value for inp in cell.inputs])
                if new_val != cell.value:
                    cell.value = new_val
                    for obs in cell.observers:
                        queue.put(obs)
            
            # Trigger callbacks for cells that changed from their initial value
            for cell in all_cells:
                if cell.value != initial_values[cell]:
                    for callback in cell.callbacks:
                        callback(cell.value)


class ComputeCell:
    def __init__(self, inputs, compute_function):
        self.inputs = inputs
        self.compute_function = compute_function
        self.value = self.compute_function([inp.value for inp in inputs])
        self.observers = []
        self.callbacks = []
        
        for inp in self.inputs:
            inp.observers.append(self)

    def add_callback(self, callback):
        self.callbacks.append(callback)

    def remove_callback(self, callback):
        if callback in self.callbacks:
            self.callbacks.remove(callback)
    
