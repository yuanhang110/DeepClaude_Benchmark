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
    if (value !== this.value) {
      this.value = value;
      // Notify all subscribers of the change
      this.subscribers.forEach(subscriber => subscriber.update());
    }
  }
}

export class ComputeCell {
  constructor(inputCells, fn) {
    this.inputCells = inputCells;
    this.fn = fn;
    this.value = fn(...inputCells.map(cell => cell.value));
    this.subscribers = [];
    this.callbacks = [];
    
    // Subscribe to all input cells
    inputCells.forEach(inputCell => {
      inputCell.subscribers.push(this);
    });
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

  update() {
    const newValue = this.fn(...this.inputCells.map(cell => cell.value));
    if (newValue !== this.value) {
      this.value = newValue;
      // Notify both subscribers and callbacks
      this.subscribers.forEach(subscriber => subscriber.update());
      this.callbacks.forEach(cb => {
        cb.values.push(this.value);
        cb.fn(this.value);
      });
    }
  }
}

export class CallbackCell {
  constructor(fn) {
    this.fn = fn;
    this.values = [];
  }
}
