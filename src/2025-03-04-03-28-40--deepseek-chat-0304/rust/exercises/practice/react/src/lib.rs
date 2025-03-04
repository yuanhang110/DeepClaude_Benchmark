/// `InputCellId` is a unique identifier for an input cell.
#[derive(Clone, Copy, Debug, PartialEq, Eq, Hash)]
pub struct InputCellId(usize);
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
pub struct ComputeCellId(usize);
#[derive(Clone, Copy, Debug, PartialEq, Eq, Hash)]
pub struct CallbackId(usize);

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

use std::collections::HashMap;

struct InputCell<T> {
    value: T,
}

struct ComputeCell<T> {
    value: T,
    dependencies: Vec<CellId>,
    compute_func: Box<dyn Fn(&[T]) -> T>,
    callbacks: HashMap<CallbackId, Box<dyn FnMut(T)>>,
}

pub struct Reactor<T> {
    input_cells: HashMap<InputCellId, InputCell<T>>,
    compute_cells: HashMap<ComputeCellId, ComputeCell<T>>,
    next_input_id: usize,
    next_compute_id: usize,
    next_callback_id: usize,
}

// You are guaranteed that Reactor will only be tested against types that are Copy + PartialEq.
impl<T: Copy + PartialEq> Reactor<T> {
    pub fn new() -> Self {
        Reactor {
            input_cells: HashMap::new(),
            compute_cells: HashMap::new(),
            next_input_id: 0,
            next_compute_id: 0,
            next_callback_id: 0,
        }
    }

    // Creates an input cell with the specified initial value, returning its ID.
    pub fn create_input(&mut self, initial: T) -> InputCellId {
        let id = InputCellId(self.next_input_id);
        self.next_input_id += 1;
        self.input_cells.insert(id, InputCell { value: initial });
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
        // Check all dependencies exist
        for &dep in dependencies {
            match dep {
                CellId::Input(id) if !self.input_cells.contains_key(&id) => return Err(dep),
                CellId::Compute(id) if !self.compute_cells.contains_key(&id) => return Err(dep),
                _ => {}
            }
        }

        // Get initial values
        let values: Vec<T> = dependencies.iter()
            .map(|&id| self.value(id).unwrap())
            .collect();

        // Compute initial value
        let value = compute_func(&values);

        let id = ComputeCellId(self.next_compute_id);
        self.next_compute_id += 1;

        self.compute_cells.insert(id, ComputeCell {
            value,
            dependencies: dependencies.to_vec(),
            compute_func: Box::new(compute_func),
            callbacks: HashMap::new(),
        });

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
            CellId::Input(input_id) => self.input_cells.get(&input_id).map(|c| c.value),
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
        if let Some(cell) = self.input_cells.get_mut(&id) {
            if cell.value == new_value {
                return true;
            }
            cell.value = new_value;
            self.propagate();
            true
        } else {
            false
        }
    }

    fn propagate(&mut self) {
        let mut changed = true;
        while changed {
            changed = false;
            let mut new_values = HashMap::new();

            // Compute new values for all compute cells
            for (&id, cell) in &self.compute_cells {
                let values: Vec<T> = cell.dependencies.iter()
                    .map(|&dep_id| self.value(dep_id).unwrap())
                    .collect();
                let new_value = (cell.compute_func)(&values);
                if new_value != cell.value {
                    new_values.insert(id, new_value);
                    changed = true;
                }
            }

            // Update values and trigger callbacks
            for (id, new_value) in new_values {
                if let Some(cell) = self.compute_cells.get_mut(&id) {
                    let _old_value = cell.value;
                    cell.value = new_value;
                    for callback in cell.callbacks.values_mut() {
                        callback(cell.value);
                    }
                }
            }
        }
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
        callback: F,
    ) -> Option<CallbackId> {
        if let Some(cell) = self.compute_cells.get_mut(&id) {
            let callback_id = CallbackId(self.next_callback_id);
            self.next_callback_id += 1;
            cell.callbacks.insert(callback_id, Box::new(callback));
            Some(callback_id)
        } else {
            None
        }
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
        if let Some(cell) = self.compute_cells.get_mut(&cell) {
            if cell.callbacks.remove(&callback).is_some() {
                Ok(())
            } else {
                Err(RemoveCallbackError::NonexistentCallback)
            }
        } else {
            Err(RemoveCallbackError::NonexistentCell)
        }
    }
}
