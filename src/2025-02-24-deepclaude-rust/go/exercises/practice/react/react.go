package react


type reactor struct {
	queue        []*computeCell
	changedCells []*computeCell
}

type inputCell struct {
	reactor    *reactor
	value      int
	dependents []*computeCell
}

type computeCell struct {
	reactor      *reactor
	dependencies []Cell
	computeFunc  interface{}
	value        int
	dependents   []*computeCell
	callbacks    map[int]func(int)
	callbackID   int
}

type canceler struct {
	cell *computeCell
	id   int
}

func (c *canceler) Cancel() {
	delete(c.cell.callbacks, c.id)
}

func (c *inputCell) Value() int {
	return c.value
}

func (c *inputCell) SetValue(value int) {
	if c.value == value {
		return
	}
	c.value = value
	c.reactor.propagateFromInput(c)
}

func (c *inputCell) AddCallback(func(int)) Canceler {
	panic("input cells do not support callbacks")
}

func (cc *computeCell) Value() int {
	return cc.value
}

func (cc *computeCell) SetValue(int) {
	panic("compute cells cannot be set directly")
}

func (cc *computeCell) AddCallback(callback func(int)) Canceler {
	cc.callbackID++
	id := cc.callbackID
	cc.callbacks[id] = callback
	return &canceler{cell: cc, id: id}
}

func New() Reactor {
	return &reactor{}
}

func (r *reactor) CreateInput(initial int) InputCell {
	return &inputCell{
		reactor: r,
		value:   initial,
	}
}

func (r *reactor) CreateCompute1(dep Cell, compute func(int) int) ComputeCell {
	cc := &computeCell{
		reactor:      r,
		dependencies: []Cell{dep},
		computeFunc:  compute,
		value:        compute(dep.Value()),
		callbacks:    make(map[int]func(int)),
	}
	if d, ok := dep.(interface{ addDependent(*computeCell) }); ok {
		d.addDependent(cc)
	}
	return cc
}

func (r *reactor) CreateCompute2(dep1, dep2 Cell, compute func(int, int) int) ComputeCell {
	cc := &computeCell{
		reactor:      r,
		dependencies: []Cell{dep1, dep2},
		computeFunc:  compute,
		value:        compute(dep1.Value(), dep2.Value()),
		callbacks:    make(map[int]func(int)),
	}
	if d, ok := dep1.(interface{ addDependent(*computeCell) }); ok {
		d.addDependent(cc)
	}
	if d, ok := dep2.(interface{ addDependent(*computeCell) }); ok {
		d.addDependent(cc)
	}
	return cc
}

func (c *inputCell) addDependent(cc *computeCell) {
	c.dependents = append(c.dependents, cc)
}

func (cc *computeCell) addDependent(c *computeCell) {
	cc.dependents = append(cc.dependents, c)
}

func (r *reactor) propagateFromInput(c *inputCell) {
	r.queue = r.queue[:0]
	r.changedCells = r.changedCells[:0]

	for _, dep := range c.dependents {
		r.queue = append(r.queue, dep)
	}

	for len(r.queue) > 0 {
		cell := r.queue[0]
		r.queue = r.queue[1:]

		oldValue := cell.value
		newValue := cell.recompute()

		if newValue != oldValue {
			cell.value = newValue
			r.changedCells = append(r.changedCells, cell)
			r.queue = append(r.queue, cell.dependents...)
		}
	}

	for _, cell := range r.changedCells {
		cell.triggerCallbacks()
	}
}

func (cc *computeCell) recompute() int {
	switch len(cc.dependencies) {
	case 1:
		return cc.computeFunc.(func(int) int)(cc.dependencies[0].Value())
	case 2:
		return cc.computeFunc.(func(int, int) int)(cc.dependencies[0].Value(), cc.dependencies[1].Value())
	default:
		panic("invalid number of dependencies")
	}
}

func (cc *computeCell) triggerCallbacks() {
	for _, cb := range cc.callbacks {
		cb(cc.value)
	}
}
