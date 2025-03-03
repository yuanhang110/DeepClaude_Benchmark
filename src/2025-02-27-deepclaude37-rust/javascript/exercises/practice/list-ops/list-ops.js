//
// This is only a SKELETON file for the 'List Ops' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class List {
  constructor(values = []) {
    this.values = values;
  }

  append(otherList) {
    this.values = [...this.values, ...otherList.values];
    return this;
  }

  concat(listOfLists) {
    const newValues = [...this.values];
    for (const list of listOfLists.values || []) {
      for (const element of list.values) {
        newValues[newValues.length] = element;
      }
    }
    return new List(newValues);
  }

  filter(predicate) {
    const filtered = [];
    for (const element of this.values) {
      if (predicate(element)) {
        filtered[filtered.length] = element;
      }
    }
    return new List(filtered);
  }

  map(func) {
    const mapped = [];
    for (const element of this.values) {
      mapped[mapped.length] = func(element);
    }
    return new List(mapped);
  }

  length() {
    let count = 0;
    for (const _ of this.values) {
      count += 1;
    }
    return count;
  }

  foldl(func, accumulator) {
    let result = accumulator;
    for (const element of this.values) {
      result = func(result, element);
    }
    return result;
  }

  foldr(func, accumulator) {
    let result = accumulator;
    // We need to iterate from right to left without using reverse()
    for (let i = this.length() - 1; i >= 0; i--) {
      result = func(result, this.values[i]);
    }
    return result;
  }

  reverse() {
    const reversed = [];
    for (let i = this.length() - 1; i >= 0; i--) {
      reversed[reversed.length] = this.values[i];
    }
    return new List(reversed);
  }
}
