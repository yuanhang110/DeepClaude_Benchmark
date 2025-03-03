package react

type reactor struct {
	cells []*cell
}

type callbackEntry struct {
	id       int
	callback func(int)
}

type cell struct {
	value      int
	callbacks  []callbackEntry
	nextCallbackID int
	dependents []*cell
	compute    func() int // nil for input cells
}

type canceler struct {
	cell  *cell
	id    int
}

func (c *canceler) Cancel() {
	for i, entry := range c.cell.callbacks {
		if entry.id == c.id {
			// Remove the callback with the matching ID
			c.cell.callbacks = append(c.cell.callbacks[:i], c.cell.callbacks[i+1:]...)
			c.id = -1 // Mark as invalid to prevent multiple cancellations
			return
		}
	}
}

func (c *cell) Value() int {
	return c.value
}

func (c *cell) SetValue(value int) {
	if c.compute != nil {
		panic("SetValue called on a compute cell")
	}
	
	if c.value == value {
		return // No change, no need to propagate
	}
	
	c.value = value
	
	// Propagate changes through the dependency graph
	var queue []*cell
	changed := make(map[*cell]struct{})
	
	// Start by queueing this cell's dependents
	queue = append(queue, c.dependents...)
	
	// Process the queue until empty
	for len(queue) > 0 {
		current := queue[0]
		queue = queue[1:]
		
		// Calculate new value
		oldValue := current.value
		newValue := current.compute()
		
		// If value changed, update and queue dependents
		if newValue != oldValue {
			current.value = newValue
			changed[current] = struct{}{}
			queue = append(queue, current.dependents...)
		}
	}
	
	// After reaching stable state, call callbacks for changed cells
	for cell := range changed {
		for _, entry := range cell.callbacks {
			entry.callback(cell.value)
		}
	}
}

func (c *cell) AddCallback(callback func(int)) Canceler {
	c.nextCallbackID++
	id := c.nextCallbackID
	c.callbacks = append(c.callbacks, callbackEntry{id, callback})
	return &canceler{
		cell: c,
		id:   id,
	}
}

func New() Reactor {
	return &reactor{}
}

func (r *reactor) CreateInput(initial int) InputCell {
	c := &cell{
		value: initial,
	}
	r.cells = append(r.cells, c)
	return c
}

func (r *reactor) CreateCompute1(dep Cell, compute func(int) int) ComputeCell {
	depCell := dep.(*cell)
	c := &cell{
		compute: func() int {
			return compute(depCell.Value())
		},
	}
	
	// Initial computation
	c.value = c.compute()
	
	// Add this cell as a dependent of its dependency
	depCell.dependents = append(depCell.dependents, c)
	
	r.cells = append(r.cells, c)
	return c
}

func (r *reactor) CreateCompute2(dep1, dep2 Cell, compute func(int, int) int) ComputeCell {
	dep1Cell := dep1.(*cell)
	dep2Cell := dep2.(*cell)
	
	c := &cell{
		compute: func() int {
			return compute(dep1Cell.Value(), dep2Cell.Value())
		},
	}
	
	// Initial computation
	c.value = c.compute()
	
	// Add this cell as a dependent of both dependencies
	dep1Cell.dependents = append(dep1Cell.dependents, c)
	dep2Cell.dependents = append(dep2Cell.dependents, c)
	
	r.cells = append(r.cells, c)
	return c
}
