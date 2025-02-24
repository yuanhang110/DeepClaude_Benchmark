//
// This is only a SKELETON file for the 'List Ops' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class List {
  constructor(elements = []) {
    this.elements = [...elements];
  }

  get values() {
    return this.elements;
  }

  append(otherList) {
    return new List([...this.elements, ...otherList.elements]);
  }

  concat(lists) {
    let newElements = [...this.elements];
    for (const list of lists.elements) {
      newElements = [...newElements, ...list.elements];
    }
    return new List(newElements);
  }

  filter(predicate) {
    let filtered = [];
    for (const element of this.elements) {
      if (predicate(element)) {
        filtered = [...filtered, element];
      }
    }
    return new List(filtered);
  }

  map(fn) {
    let mapped = [];
    for (const element of this.elements) {
      mapped = [...mapped, fn(element)];
    }
    return new List(mapped);
  }

  length() {
    let count = 0;
    for (const _ of this.elements) {
      count++;
    }
    return count;
  }

  foldl(fn, initial) {
    let accumulator = initial;
    for (const element of this.elements) {
      accumulator = fn(accumulator, element);
    }
    return accumulator;
  }

  foldr(fn, initial) {
    let accumulator = initial;
    for (let i = this.elements.length - 1; i >= 0; i--) {
      accumulator = fn(accumulator, this.elements[i]);
    }
    return accumulator;
  }

  reverse() {
    let reversed = [];
    for (let i = this.elements.length - 1; i >= 0; i--) {
      reversed = [...reversed, this.elements[i]];
    }
    return new List(reversed);
  }
}
