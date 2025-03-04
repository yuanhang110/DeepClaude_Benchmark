export class InputCell {
  constructor(value) {
    this.value = value;
    this.dependents = new Set();
  }

  setValue(value) {
    if (this.value !== value) {
      this.value = value;
      this.notifyDependents();
    }
  }

  addDependent(cell) {
    this.dependents.add(cell);
  }

  notifyDependents() {
    for (const cell of this.dependents) {
      cell.update();
    }
  }
}

export class ComputeCell {
  constructor(inputCells, fn) {
    this.inputCells = inputCells;
    this.computeFn = fn;
    this.callbacks = new Set();
    this.value = this.compute();
    
    for (const inputCell of inputCells) {
      if (inputCell instanceof InputCell) {
        inputCell.addDependent(this);
      }
    }
  }

  compute() {
    const values = this.inputCells.map(cell => cell.value);
    return this.computeFn(values);
  }

  update() {
    const newValue = this.compute();
    if (newValue !== this.value) {
      const oldValue = this.value;
      this.value = newValue;
      this.notifyCallbacks(oldValue);
    }
  }

  addCallback(cb) {
    this.callbacks.add(cb);
  }

  removeCallback(cb) {
    this.callbacks.delete(cb);
  }

  notifyCallbacks(oldValue) {
    for (const cb of this.callbacks) {
      cb(this, oldValue);
    }
  }
}

export class CallbackCell {
  constructor(fn) {
    this.fn = fn;
    this.values = [];
  }

  call(cell, oldValue) {
    this.values.push(this.fn(cell, oldValue));
  }
}
