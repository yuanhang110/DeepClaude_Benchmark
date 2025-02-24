/// `InputCellId` is a unique identifier for an input cell.
#[derive(Clone, Copy, Debug, PartialEq, Eq, Hash)]
pub struct InputCellId(u64);
/// `ComputeCellId` is a unique identifier for a compute cell.
/// Values of type `InputCellId` and `ComputeCellId` should not be mutually assignable,
/// demonstrated by the following tests:
///
/// ```compile_fail
/// let mut r = react::Reactor::new();
/// let input: react::ComputeCellId = r.create_input(111);
/// ```
///
/// ```compile_fail
/// let mut r = react::Reactor::new();
/// let input = r.create_input(111);
/// let compute: react::InputCellId = r.create_compute(&[react::CellId::Input(input)], |_| 222).unwrap();
/// ```
#[derive(Clone, Copy, Debug, PartialEq, Eq, Hash)]
pub struct ComputeCellId(u64);
#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub struct CallbackId(u64);

#[derive(Clone, Copy, Debug, PartialEq, Eq, Hash)]
pub enum CellId {
    Input(InputCellId),
    Compute(ComputeCellId),
}

#[derive(Debug, PartialEq, Eq)]
pub enum RemoveCallbackError {
    NonexistentCell,
    NonexistentCallback,
}

struct ComputeCell<T> {
    dependencies: Vec<CellId>,
    compute_func: Box<dyn Fn(&[T]) -> T>,
    value: T,
    callbacks: std::collections::HashMap<CallbackId, Box<dyn FnMut(T)>>,
}

pub struct Reactor<T> {
    // Just so that the compiler doesn't complain about an unused type parameter.
    // You probably want to delete this field.
    input_cells: std::collections::HashMap<InputCellId, T>,
    compute_cells: std::collections::HashMap<ComputeCellId, ComputeCell<T>>,
    dependents: std::collections::HashMap<CellId, Vec<ComputeCellId>>,
    next_input_id: u64,
    next_compute_id: u64,
    next_callback_id: u64,
    phantom: ::std::marker::PhantomData<T>,
}

// You are guaranteed that Reactor will only be tested against types that are Copy + PartialEq.
impl<T: Copy + PartialEq> Reactor<T> {
    pub fn new() -> Self {
        Reactor {
            input_cells: std::collections::HashMap::new(),
            compute_cells: std::collections::HashMap::new(),
            dependents: std::collections::HashMap::new(),
            next_input_id: 1,
            next_compute_id: 1,
            next_callback_id: 1,
            phantom: ::std::marker::PhantomData,
        }
    }

    // Creates an input cell with the specified initial value, returning its ID.
    pub fn create_input(&mut self, initial: T) -> InputCellId {
        let id = InputCellId(self.next_input_id);
        self.next_input_id += 1;
        self.input_cells.insert(id, initial);
        id
    }

    // Creates a compute cell with the specified dependencies and compute function.
    // The compute function is expected to take in its arguments in the same order as specified in
    // `dependencies`.
    // You do not need to reject compute functions that expect more arguments than there are
    // dependencies (how would you check for this, anyway?).
    //
    // If any dependency doesn't exist, returns an Err with that nonexistent dependency.
    // (If multiple dependencies do not exist, exactly which one is returned is not defined and
    // will not be tested)
    //
    // Notice that there is no way to *remove* a cell.
    // This means that you may assume, without checking, that if the dependencies exist at creation
    // time they will continue to exist as long as the Reactor exists.
    pub fn create_compute<F: Fn(&[T]) -> T + 'static>(
        &mut self,
        dependencies: &[CellId],
        compute_func: F,
    ) -> Result<ComputeCellId, CellId> {
        // Verify all dependencies exist
        for &dep in dependencies {
            match dep {
                CellId::Input(id) if self.input_cells.contains_key(&id) => (),
                CellId::Compute(id) if self.compute_cells.contains_key(&id) => (),
                _ => return Err(dep),
            }
        }
        
        // Collect current dependency values
        let values: Vec<T> = dependencies.iter().map(|&id| {
            match id {
                CellId::Input(input_id) => *self.input_cells.get(&input_id).unwrap(),
                CellId::Compute(compute_id) => self.compute_cells.get(&compute_id).unwrap().value,
            }
        }).collect();
        
        // Compute initial value
        let initial_value = compute_func(&values);
        
        // Create compute cell
        let id = ComputeCellId(self.next_compute_id);
        self.next_compute_id += 1;
        let cell = ComputeCell {
            dependencies: dependencies.to_vec(),
            compute_func: Box::new(compute_func),
            value: initial_value,
            callbacks: std::collections::HashMap::new(),
        };
        self.compute_cells.insert(id, cell);
        
        // Register dependencies in dependents map
        for &dep in dependencies {
            self.dependents.entry(dep)
                .or_default()
                .push(id);
        }
        
        Ok(id)
    }

    // Retrieves the current value of the cell, or None if the cell does not exist.
    //
    // You may wonder whether it is possible to implement `get(&self, id: CellId) -> Option<&Cell>`
    // and have a `value(&self)` method on `Cell`.
    //
    // It turns out this introduces a significant amount of extra complexity to this exercise.
    // We chose not to cover this here, since this exercise is probably enough work as-is.
    pub fn value(&self, id: CellId) -> Option<T> {
        match id {
            CellId::Input(input_id) => self.input_cells.get(&input_id).copied(),
            CellId::Compute(compute_id) => self.compute_cells.get(&compute_id).map(|c| c.value),
        }
    }

    // Sets the value of the specified input cell.
    //
    // Returns false if the cell does not exist.
    //
    // Similarly, you may wonder about `get_mut(&mut self, id: CellId) -> Option<&mut Cell>`, with
    // a `set_value(&mut self, new_value: T)` method on `Cell`.
    //
    // As before, that turned out to add too much extra complexity.
    pub fn set_value(&mut self, id: InputCellId, new_value: T) -> bool {
        // Get current value and check if it's changing
        let Some(current_value) = self.input_cells.get(&id) else {
            return false;
        };
        if current_value == &new_value {
            return true; // No change needed
        }
        
        // Update the input cell's value
        *self.input_cells.get_mut(&id).unwrap() = new_value;
        
        // Process dependents using BFS
        let mut queue = std::collections::VecDeque::new();
        if let Some(deps) = self.dependents.get(&CellId::Input(id)) {
            queue.extend(deps.iter().copied());
        }
        
        while let Some(compute_id) = queue.pop_front() {
            let cell = self.compute_cells.get(&compute_id).unwrap();
            let dependencies = &cell.dependencies;
            
            // Collect current dependency values
            let values: Vec<T> = dependencies.iter().map(|&dep_id| {
                match dep_id {
                    CellId::Input(input_id) => *self.input_cells.get(&input_id).unwrap(),
                    CellId::Compute(comp_id) => self.compute_cells.get(&comp_id).unwrap().value,
                }
            }).collect();
            
            // Compute new value
            let new_val = (cell.compute_func)(&values);
            let cell = self.compute_cells.get_mut(&compute_id).unwrap();
            
            if cell.value != new_val {
                // Update value and run callbacks
                cell.value = new_val;
                for callback in cell.callbacks.values_mut() {
                    callback(new_val);
                }
                
                // Schedule dependents for processing
                if let Some(deps) = self.dependents.get(&CellId::Compute(compute_id)) {
                    queue.extend(deps.iter().copied());
                }
            }
        }
        
        true
    }

    // Adds a callback to the specified compute cell.
    //
    // Returns the ID of the just-added callback, or None if the cell doesn't exist.
    //
    // Callbacks on input cells will not be tested.
    //
    // The semantics of callbacks (as will be tested):
    // For a single set_value call, each compute cell's callbacks should each be called:
    // * Zero times if the compute cell's value did not change as a result of the set_value call.
    // * Exactly once if the compute cell's value changed as a result of the set_value call.
    //   The value passed to the callback should be the final value of the compute cell after the
    //   set_value call.
    pub fn add_callback<F: FnMut(T) + 'static>(
        &mut self,
        id: ComputeCellId,
        mut callback: F,
    ) -> Option<CallbackId> {
        let cell = self.compute_cells.get_mut(&id)?;
        let callback_id = CallbackId(self.next_callback_id);
        self.next_callback_id += 1;
        
        cell.callbacks.insert(callback_id, Box::new(callback));
        Some(callback_id)
    }

    // Removes the specified callback, using an ID returned from add_callback.
    //
    // Returns an Err if either the cell or callback does not exist.
    //
    // A removed callback should no longer be called.
    pub fn remove_callback(
        &mut self,
        cell: ComputeCellId,
        callback: CallbackId,
    ) -> Result<(), RemoveCallbackError> {
        let Some(compute_cell) = self.compute_cells.get_mut(&cell) else {
            return Err(RemoveCallbackError::NonexistentCell);
        };
        
        if compute_cell.callbacks.remove(&callback).is_none() {
            return Err(RemoveCallbackError::NonexistentCallback);
        }
        
        Ok(())
    }
}
