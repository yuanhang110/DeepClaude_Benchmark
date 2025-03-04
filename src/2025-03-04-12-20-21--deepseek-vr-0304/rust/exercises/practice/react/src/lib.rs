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

#[derive(Clone, Copy, Debug, PartialEq, Eq)]
pub enum CellId {
    Input(InputCellId),
    Compute(ComputeCellId),
}

#[derive(Debug, PartialEq, Eq)]
pub enum RemoveCallbackError {
    NonexistentCell,
    NonexistentCallback,
}

struct InputCell<T> {
    value: T,
    dependents: Vec<ComputeCellId>,
}

struct ComputeCell<T> {
    dependencies: Vec<CellId>,
    compute_func: Box<dyn Fn(&[T]) -> T>,
    value: T,
    callbacks: std::collections::HashMap<CallbackId, Box<dyn FnMut(T)>>,
    dependents: Vec<ComputeCellId>,
}

pub struct Reactor<T> {
    input_cells: std::collections::HashMap<InputCellId, InputCell<T>>,
    compute_cells: std::collections::HashMap<ComputeCellId, ComputeCell<T>>,
    next_input_id: u64,
    next_compute_id: u64,
    next_callback_id: u64,
    phantom: std::marker::PhantomData<T>,
}

// You are guaranteed that Reactor will only be tested against types that are Copy + PartialEq.
impl<T: Copy + PartialEq> Reactor<T> {
    pub fn new() -> Self {
        Reactor {
            input_cells: std::collections::HashMap::new(),
            compute_cells: std::collections::HashMap::new(),
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
        self.input_cells.insert(id, InputCell {
            value: initial,
            dependents: Vec::new(),
        });
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
    pub fn create_compute<F: Fn(&[T]) -> T>(
        &mut self,
        _dependencies: &[CellId],
        _compute_func: F,
    ) -> Result<ComputeCellId, CellId> {
        // Check all dependencies exist
        for dep in _dependencies {
            match dep {
                CellId::Input(id) => {
                    if !self.input_cells.contains_key(id) {
                        return Err(*dep);
                    }
                }
                CellId::Compute(id) => {
                    if !self.compute_cells.contains_key(id) {
                        return Err(*dep);
                    }
                }
            }
        }

        // Collect current values of dependencies
        let current_values: Vec<T> = _dependencies.iter().map(|dep| {
            match dep {
                CellId::Input(id) => self.input_cells.get(id).unwrap().value,
                CellId::Compute(id) => self.compute_cells.get(id).unwrap().value,
            }
        }).collect();

        let initial_value = _compute_func(&current_values);

        let compute_id = ComputeCellId(self.next_compute_id);
        self.next_compute_id += 1;

        let dependencies = _dependencies.to_vec();

        // Add this compute cell to dependencies' dependents
        for dep in &dependencies {
            match dep {
                CellId::Input(id) => {
                    self.input_cells.get_mut(id).unwrap().dependents.push(compute_id);
                }
                CellId::Compute(id) => {
                    self.compute_cells.get_mut(id).unwrap().dependents.push(compute_id);
                }
            }
        }

        self.compute_cells.insert(compute_id, ComputeCell {
            dependencies,
            compute_func: Box::new(_compute_func),
            value: initial_value,
            callbacks: std::collections::HashMap::new(),
            dependents: Vec::new(),
        });

        Ok(compute_id)
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
            CellId::Input(input_id) => self.input_cells.get(&input_id).map(|cell| cell.value),
            CellId::Compute(compute_id) => self.compute_cells.get(&compute_id).map(|cell| cell.value),
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
        // Check if the input cell exists
        let cell = match self.input_cells.get_mut(&id) {
            Some(cell) => cell,
            None => return false,
        };

        if cell.value == new_value {
            // No change, return true but no propagation needed
            return true;
        }

        cell.value = new_value;

        // Propagate changes to dependents
        let mut to_process = std::collections::VecDeque::new();
        to_process.extend(cell.dependents.iter().cloned());

        while let Some(compute_id) = to_process.pop_front() {
            let compute_cell = match self.compute_cells.get_mut(&compute_id) {
                Some(cell) => cell,
                None => continue, // Cell might have been removed, but the problem says cells are never removed
            };

            // Recompute the value based on current dependencies
            let old_value = compute_cell.value;
            let new_value = {
                let dependencies_values: Vec<T> = compute_cell.dependencies.iter().map(|dep| {
                    match dep {
                        CellId::Input(id) => self.input_cells.get(id).unwrap().value,
                        CellId::Compute(id) => self.compute_cells.get(id).unwrap().value,
                    }
                }).collect();
                (compute_cell.compute_func)(&dependencies_values)
            };

            if new_value == old_value {
                // No change, no need to propagate further
                continue;
            }

            compute_cell.value = new_value;

            // Call callbacks
            for callback in compute_cell.callbacks.values_mut() {
                (callback)(new_value);
            }

            // Add dependents to the queue
            to_process.extend(compute_cell.dependents.iter().cloned());
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
    pub fn add_callback<F: FnMut(T)>(
        &mut self,
        id: ComputeCellId,
        callback: F,
    ) -> Option<CallbackId> {
        let compute_cell = self.compute_cells.get_mut(&id)?;

        let callback_id = CallbackId(self.next_callback_id);
        self.next_callback_id += 1;

        compute_cell.callbacks.insert(callback_id, Box::new(callback));
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
        let compute_cell = self.compute_cells.get_mut(&cell)
            .ok_or(RemoveCallbackError::NonexistentCell)?;

        compute_cell.callbacks.remove(&callback)
            .ok_or(RemoveCallbackError::NonexistentCallback)?;

        Ok(())
    }
}
