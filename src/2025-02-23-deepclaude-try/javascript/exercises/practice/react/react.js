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
    if (this.value !== value) {
      this.value = value;
      this.dependents.forEach(dependent => dependent.update());
    }
  }
}

export class ComputeCell {
  constructor(inputCells, fn) {
    this.inputCells = inputCells;
    this.fn = fn;
    this.callbacks = [];
    this.dependents = [];
    this.value = this.fn(this.inputCells);
    
    inputCells.forEach(inputCell => {
      inputCell.dependents.push(this);
    });
  }

  update() {
    const newValue = this.fn(this.inputCells);
    if (newValue !== this.value) {
      this.value = newValue;
      this.callbacks.forEach(cb => cb.fn(this.value));
      this.dependents.forEach(dependent => dependent.update());
    }
  }

  addCallback(cb) {
    this.callbacks.push(cb);
  }

  removeCallback(cb) {
    const index = this.callbacks.indexOf(cb);
    if (index !== -1) {
      this.callbacks.splice(index, 1);
    }
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
