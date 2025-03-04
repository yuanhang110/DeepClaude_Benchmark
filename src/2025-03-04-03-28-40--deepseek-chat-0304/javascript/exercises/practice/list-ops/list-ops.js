export class List {
  constructor(values = []) {
    this.values = values;
  }

  append(otherList) {
    const newValues = [...this.values];
    for (const value of otherList.values) {
      newValues.push(value);
    }
    return new List(newValues);
  }

  concat(lists) {
    let newValues = [...this.values];
    for (const list of lists.values) {
      for (const value of list.values) {
        newValues.push(value);
      }
    }
    return new List(newValues);
  }

  filter(predicate) {
    const newValues = [];
    for (const value of this.values) {
      if (predicate(value)) {
        newValues.push(value);
      }
    }
    return new List(newValues);
  }

  map(fn) {
    const newValues = [];
    for (const value of this.values) {
      newValues.push(fn(value));
    }
    return new List(newValues);
  }

  length() {
    let count = 0;
    for (const _ of this.values) {
      count++;
    }
    return count;
  }

  foldl(fn, initial) {
    let accumulator = initial;
    for (const value of this.values) {
      accumulator = fn(accumulator, value);
    }
    return accumulator;
  }

  foldr(fn, initial) {
    let accumulator = initial;
    for (let i = this.values.length - 1; i >= 0; i--) {
      accumulator = fn(accumulator, this.values[i]);
    }
    return accumulator;
  }

  reverse() {
    const newValues = [];
    for (let i = this.values.length - 1; i >= 0; i--) {
      newValues.push(this.values[i]);
    }
    return new List(newValues);
  }
}
