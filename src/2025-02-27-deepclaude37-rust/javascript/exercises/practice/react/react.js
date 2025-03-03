//
// This is only a SKELETON file for the 'React' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class InputCell {
  constructor(value) {
    this.value = value;
    this.subscribers = [];
  }

  setValue(value) {
    if (value === this.value) return;
    this.value = value;
    const context = {
      originalValues: new Map(),
      changedCells: new Set()
    };
    this.subscribers.forEach(sub => sub.update(context));
    context.changedCells.forEach(cell => {
      const originalValue = context.originalValues.get(cell);
      if (cell.value !== originalValue) {
        cell.callbacks.forEach(cb => cb(cell.value));
      }
    });
  }
}

export class ComputeCell {
  constructor(inputCells, fn) {
    this.inputCells = inputCells;
    this.fn = fn;
    this.subscribers = [];
    this.callbacks = new Set();
    this.value = this.compute();

    // Subscribe to all input cells
    inputCells.forEach(inputCell => {
      inputCell.subscribers.push(this);
    });
  }

  compute() {
    return this.fn(this.inputCells);
  }

  update(context) {
    if (!context.originalValues.has(this)) {
      context.originalValues.set(this, this.value);
    }

    const newValue = this.compute();
    if (newValue !== this.value) {
      this.value = newValue;
      context.changedCells.add(this);
      this.subscribers.forEach(sub => sub.update(context));
    }
  }

  addCallback(cb) {
    this.callbacks.add(cb);
  }

  removeCallback(cb) {
    this.callbacks.delete(cb);
  }
}

export class CallbackCell {
  constructor(fn) {
    this.values = [];
    this.fn = (value) => {
      this.values.push(value);
      fn(value);
    };
  }
}
