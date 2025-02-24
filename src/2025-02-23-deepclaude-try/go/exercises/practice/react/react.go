package react

// Define reactor, cell and canceler types here.
// These types will implement the Reactor, Cell and Canceler interfaces, respectively.
type reactor struct {
	cells        []*cell
	queue        []*cell
	changedCells map[*cell]struct{}
}

type cell struct {
	reactor    *reactor
	value      int
	callbacks  []func(int)
	dependents []*cell
	computeFn  func() int
	deps       []*cell
}

type canceler struct {
	c  *cell
	id int
}

func (c *canceler) Cancel() {
	if c.id < len(c.c.callbacks) {
		c.c.callbacks[c.id] = nil
	}
}

func (c *cell) Value() int {
	return c.value
}

func (c *cell) SetValue(value int) {
	if c.computeFn != nil {
		panic("SetValue called on compute cell")
	}
	if c.value != value {
		c.value = value
		c.reactor.queue = append(c.reactor.queue, c.dependents...)
		c.reactor.processUpdates()
	}
}

func (c *cell) AddCallback(callback func(int)) Canceler {
	// Look for existing nil slot to reuse
	for i, cb := range c.callbacks {
		if cb == nil {
			c.callbacks[i] = callback
			return &canceler{c, i}
		}
	}
	// Append new callback
	c.callbacks = append(c.callbacks, callback)
	return &canceler{c, len(c.callbacks) - 1}
}

func New() Reactor {
	return &reactor{
		changedCells: make(map[*cell]struct{}),
	}
}

// processUpdates handles propagating changes through dependent cells and triggering callbacks
func (r *reactor) processUpdates() {
	r.changedCells = make(map[*cell]struct{})
	for len(r.queue) > 0 {
		current := r.queue[0]
		r.queue = r.queue[1:]
		if current.computeFn != nil {
			newVal := current.computeFn()
			if newVal != current.value {
				current.value = newVal
				r.changedCells[current] = struct{}{}
				r.queue = append(r.queue, current.dependents...)
			}
		}
	}
	
	for c := range r.changedCells {
		for _, cb := range c.callbacks {
			if cb != nil {
				cb(c.value)
			}
		}
	}
}

func (r *reactor) CreateInput(initial int) InputCell {
	c := &cell{
		reactor:   r,
		value:     initial,
		computeFn: nil,
	}
	r.cells = append(r.cells, c)
	return c
}

func (r *reactor) CreateCompute1(dep Cell, compute func(int) int) ComputeCell {
	depCell := dep.(*cell)
	c := &cell{
		reactor:   r,
		computeFn: func() int { return compute(depCell.value) },
		deps:      []*cell{depCell},
	}
	c.value = c.computeFn()
	depCell.dependents = append(depCell.dependents, c)
	r.cells = append(r.cells, c)
	return c
}

func (r *reactor) CreateCompute2(dep1, dep2 Cell, compute func(int, int) int) ComputeCell {
	depCell1 := dep1.(*cell)
	depCell2 := dep2.(*cell)
	c := &cell{
		reactor:   r,
		computeFn: func() int { return compute(depCell1.value, depCell2.value) },
		deps:      []*cell{depCell1, depCell2},
	}
	c.value = c.computeFn()
	depCell1.dependents = append(depCell1.dependents, c)
	depCell2.dependents = append(depCell2.dependents, c)
	r.cells = append(r.cells, c)
	return c
}
