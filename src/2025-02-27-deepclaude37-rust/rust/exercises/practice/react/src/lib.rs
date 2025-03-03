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

pub struct Reactor<T> {
    inputs: std::collections::HashMap<InputCellId, T>,
    computes: std::collections::HashMap<ComputeCellId, ComputeCell<T>>,
    dependents: std::collections::HashMap<CellId, Vec<ComputeCellId>>,
    next_input_id: usize,
    next_compute_id: usize,
    _phantom: std::marker::PhantomData<T>,
}

struct ComputeCell<T> {
    dependencies: Vec<CellId>,
    compute_func: Box<dyn Fn(&[T]) -> T>,
    value: T,
    callbacks: std::collections::HashMap<CallbackId, Box<dyn FnMut(T)>>,
    next_callback_id: usize,
}

// You are guaranteed that Reactor will only be tested against types that are Copy + PartialEq.
impl<T: Copy + PartialEq> Reactor<T> {
    pub fn new() -> Self {
        Reactor {
            inputs: std::collections::HashMap::new(),
            computes: std::collections::HashMap::new(),
            dependents: std::collections::HashMap::new(),
            next_input_id: 0,
            next_compute_id: 0,
            _phantom: std::marker::PhantomData,
        }
    }

    // Creates an input cell with the specified initial value, returning its ID.
    pub fn create_input(&mut self, initial: T) -> InputCellId {
        let id = InputCellId(self.next_input_id);
        self.next_input_id += 1;
        self.inputs.insert(id, initial);
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
        // Check if all dependencies exist
        for &dep in dependencies {
            match dep {
                CellId::Input(id) => if !self.inputs.contains_key(&id) {
                    return Err(dep);
                },
                CellId::Compute(id) => if !self.computes.contains_key(&id) {
                    return Err(dep);
                },
            }
        }

        let id = ComputeCellId(self.next_compute_id);
        self.next_compute_id += 1;

        // Compute initial value
        let values: Vec<T> = dependencies.iter()
            .map(|&dep| self.value(dep).unwrap())
            .collect();
        let value = compute_func(&values);

        // Create compute cell
        self.computes.insert(id, ComputeCell {
            dependencies: dependencies.to_vec(),
            compute_func: Box::new(compute_func),
            value,
            callbacks: std::collections::HashMap::new(),
            next_callback_id: 0,
        });

        // Add this compute cell to the dependents list of each dependency
        for &dep in dependencies {
            self.dependents.entry(dep)
                .or_insert_with(Vec::new)
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
            CellId::Input(input_id) => self.inputs.get(&input_id).copied(),
            CellId::Compute(compute_id) => self.computes.get(&compute_id).map(|c| c.value),
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
        // Check if input cell exists
        let Some(old_value) = self.inputs.get_mut(&id) else {
            return false;
        };

        // If value is not changing, return early
        if *old_value == new_value {
            return true;
        }

        *old_value = new_value;

        // Collect all affected compute cells
        let mut affected = Vec::new();
        let mut visited = std::collections::HashSet::new();
        let mut stack = vec![CellId::Input(id)];

        while let Some(cell_id) = stack.pop() {
            if let Some(dependents) = self.dependents.get(&cell_id) {
                for &compute_id in dependents {
                    if visited.insert(compute_id) {
                        affected.push(compute_id);
                        stack.push(CellId::Compute(compute_id));
                    }
                }
            }
        }

        // Topologically sort the affected compute cells
        let order = self.topological_sort(&affected);

        // Track cells whose values changed
        let mut changed = std::collections::HashMap::new();

        // Update compute cells in topological order
        for compute_id in order {
            let compute_cell = self.computes.get_mut(&compute_id).unwrap();
            let values: Vec<T> = compute_cell.dependencies.iter()
                .map(|&dep| self.value(dep).unwrap())
                .collect();
            let new_val = (compute_cell.compute_func)(&values);
            if new_val != compute_cell.value {
                changed.insert(compute_id, new_val);
                compute_cell.value = new_val;
            }
        }

        // Trigger callbacks for cells that changed
        for (compute_id, new_val) in changed {
            if let Some(compute_cell) = self.computes.get_mut(&compute_id) {
                for callback in compute_cell.callbacks.values_mut() {
                    callback(new_val);
                }
            }
        }

        true
    }

    // Helper method to perform topological sort of compute cells
    fn topological_sort(&self, affected: &[ComputeCellId]) -> Vec<ComputeCellId> {
        let mut in_degree = std::collections::HashMap::new();
        let mut graph = std::collections::HashMap::new();

        // Build the graph and in-degree counts
        for &compute_id in affected {
            let compute_cell = &self.computes[&compute_id];
            for &dep in &compute_cell.dependencies {
                if let CellId::Compute(dep_id) = dep {
                    if affected.contains(&dep_id) {
                        graph.entry(dep_id)
                            .or_insert_with(Vec::new)
                            .push(compute_id);
                        *in_degree.entry(compute_id).or_insert(0) += 1;
                    }
                }
            }
        }

        // Start with nodes that have no dependencies
        let mut queue = std::collections::VecDeque::new();
        for &compute_id in affected {
            if in_degree.get(&compute_id).copied().unwrap_or(0) == 0 {
                queue.push_back(compute_id);
            }
        }

        // Process the queue
        let mut result = Vec::new();
        while let Some(node) = queue.pop_front() {
            result.push(node);
            if let Some(neighbors) = graph.get(&node) {
                for &neighbor in neighbors {
                    let count = in_degree.get_mut(&neighbor).unwrap();
                    *count -= 1;
                    if *count == 0 {
                        queue.push_back(neighbor);
                    }
                }
            }
        }

        result
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
        let compute_cell = self.computes.get_mut(&id)?;
        let callback_id = CallbackId(compute_cell.next_callback_id);
        compute_cell.next_callback_id += 1;
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
        let compute_cell = self.computes.get_mut(&cell)
            .ok_or(RemoveCallbackError::NonexistentCell)?;
        
        if compute_cell.callbacks.remove(&callback).is_some() {
            Ok(())
        } else {
            Err(RemoveCallbackError::NonexistentCallback)
        }
    }
}
