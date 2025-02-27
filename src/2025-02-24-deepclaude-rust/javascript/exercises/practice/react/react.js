//
// This is only a SKELETON file for the 'React' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class InputCell {
  constructor(value) {
    this.value = value;
    this.dependents = [];
  }

  setValue(value) {
    if (value !== this.value) {
      this.value = value;
      for (const dep of this.dependents) {
        dep.update();
      }
    }
  }
}

export class ComputeCell {
  constructor(inputCells, fn) {
    this.inputCells = inputCells;
    this.fn = fn;
    this.dependents = [];
    this.callbacks = new Set();
    this.value = this.compute();

    for (const input of inputCells) {
      input.dependents.push(this);
    }
  }

  compute() {
    return this.fn(this.inputCells.map(cell => cell.value));
  }

  update() {
    const oldValue = this.value;
    const newValue = this.compute();
    if (oldValue !== newValue) {
      this.value = newValue;
      this.notifyDependents();
      this.fireCallbacks(oldValue);
    }
  }

  notifyDependents() {
    for (const dep of this.dependents) {
      dep.update();
    }
  }

  addCallback(cb) {
    this.callbacks.add(cb);
  }

  removeCallback(cb) {
    this.callbacks.delete(cb);
  }

  fireCallbacks(oldValue) {
    for (const cb of this.callbacks) {
      cb.fn(this, oldValue);
    }
  }
}

export class CallbackCell {
  constructor(fn) {
    this.fn = fn;
  }
}
