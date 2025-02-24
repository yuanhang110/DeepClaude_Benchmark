package react

type reactor struct {
	cells []*cell
}

type cell struct {
	reactor      *reactor
	value        int
	prevValue    int
	callbacks    []func(int)
	dependencies []*cell
	compute      func() int
	observers    []*cell
}

type canceler struct {
	cell  *cell
	index int
}

func (c *canceler) Cancel() {
	// Remove callback by setting it to nil to preserve indices
	if c.index < len(c.cell.callbacks) {
		c.cell.callbacks[c.index] = nil 
	}
}

func (c *cell) Value() int {
	return c.value
}

func (c *cell) SetValue(value int) {
	if value == c.value {
		return // Skip if value isn't changing
	}
	c.reactor.savePrevValues()
	c.value = value
	c.notifyObservers()
	c.reactor.triggerCallbacks()
}

func (c *cell) AddCallback(callback func(int)) Canceler {
	index := len(c.callbacks)
	c.callbacks = append(c.callbacks, callback)
	return &canceler{cell: c, index: index}
}

func New() Reactor {
	return &reactor{}
}

func (r *reactor) CreateInput(initial int) InputCell {
	c := &cell{reactor: r, value: initial}
	r.cells = append(r.cells, c)
	return c
}

func (r *reactor) CreateCompute1(dep Cell, compute func(int) int) ComputeCell {
	depCell := dep.(*cell)
	c := &cell{
		reactor:      r,
		dependencies: []*cell{depCell},
		compute:      func() int { return compute(depCell.value) },
	}
	c.value = c.compute() // Calculate initial value
	depCell.observers = append(depCell.observers, c)
	r.cells = append(r.cells, c)
	return c
}

func (r *reactor) CreateCompute2(dep1, dep2 Cell, compute func(int, int) int) ComputeCell {
	depCell1 := dep1.(*cell)
	depCell2 := dep2.(*cell)
	c := &cell{
		reactor:      r,
		dependencies: []*cell{depCell1, depCell2},
		compute:      func() int { return compute(depCell1.value, depCell2.value) },
	}
	c.value = c.compute() // Calculate initial value
	depCell1.observers = append(depCell1.observers, c)
	depCell2.observers = append(depCell2.observers, c)
	r.cells = append(r.cells, c)
	return c
}

func (r *reactor) savePrevValues() {
	for _, cell := range r.cells {
		cell.prevValue = cell.value
	}
}

func (r *reactor) triggerCallbacks() {
	for _, cell := range r.cells {
		if cell.compute != nil && cell.value != cell.prevValue {
			for _, cb := range cell.callbacks {
				if cb != nil { // Skip canceled callbacks
					cb(cell.value)
				}
			}
		}
	}
}

func (c *cell) notifyObservers() {
	for _, observer := range c.observers {
		observer.update()
	}
}

func (c *cell) update() {
	newValue := c.compute()
	if newValue != c.value {
		c.value = newValue
		c.notifyObservers()
	} else {
		// If value didn't change, cancel propagation
		c.reactor.triggerCallbacks()
	}
}
