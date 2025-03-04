package react

type reactor struct {
	cells []*cell
}

type cell struct {
	value      int
	compute    func() int
	deps       []*cell
	callbacks  map[int]func(int)
	callbackID int
	reactor    *reactor
}

type canceler struct {
	cell *cell
	id   int
}

func New() Reactor {
	return &reactor{}
}

func (r *reactor) CreateInput(initial int) InputCell {
	c := &cell{
		value:     initial,
		callbacks: make(map[int]func(int)),
		reactor:   r,
	}
	r.cells = append(r.cells, c)
	return c
}

func (r *reactor) CreateCompute1(dep Cell, compute func(int) int) ComputeCell {
	c := &cell{
		compute:    func() int { return compute(dep.Value()) },
		deps:       []*cell{dep.(*cell)},
		callbacks:  make(map[int]func(int)),
		reactor:    r,
	}
	c.value = c.compute()
	r.cells = append(r.cells, c)
	return c
}

func (r *reactor) CreateCompute2(dep1, dep2 Cell, compute func(int, int) int) ComputeCell {
	c := &cell{
		compute:    func() int { return compute(dep1.Value(), dep2.Value()) },
		deps:       []*cell{dep1.(*cell), dep2.(*cell)},
		callbacks:  make(map[int]func(int)),
		reactor:    r,
	}
	c.value = c.compute()
	r.cells = append(r.cells, c)
	return c
}

func (c *cell) Value() int {
	return c.value
}

func (c *cell) SetValue(value int) {
	if c.value == value {
		return
	}
	c.value = value
	// Mark input cell as changed
	changed := make(map[*cell]bool)
	changed[c] = true
	
	// Trigger callbacks for input cell
	for _, callback := range c.callbacks {
		callback(value)
	}
	
	// Update dependent cells
	c.reactor.update()
}

func (c *cell) AddCallback(callback func(int)) Canceler {
	c.callbackID++
	id := c.callbackID
	c.callbacks[id] = callback
	return &canceler{cell: c, id: id}
}

func (c *canceler) Cancel() {
	delete(c.cell.callbacks, c.id)
}

func (r *reactor) update() {
	// Track changed cells
	changed := make(map[*cell]bool)
	
	// First pass: update all cells and mark changed ones
	for _, c := range r.cells {
		if c.compute != nil {
			newValue := c.compute()
			if newValue != c.value {
				c.value = newValue
				changed[c] = true
			}
		}
	}

	// Second pass: trigger callbacks for all changed cells
	for c := range changed {
		for _, callback := range c.callbacks {
			callback(c.value)
		}
	}
}
