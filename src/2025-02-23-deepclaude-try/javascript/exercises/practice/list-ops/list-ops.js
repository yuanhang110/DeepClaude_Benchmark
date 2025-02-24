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

  concat(...lists) {
    let newElements = [...this.elements];
    for (const list of lists) {
      newElements = [...newElements, ...list.elements];
    }
    return new List(newElements);
  }

  filter(predicate) {
    const newElements = [];
    let index = 0;
    for (const el of this.elements) {
      if (predicate(el)) {
        newElements[index] = el;
        index++;
      }
    }
    return new List(newElements);
  }

  map(fn) {
    const newElements = [];
    let index = 0;
    for (const el of this.elements) {
      newElements[index] = fn(el);
      index++;
    }
    return new List(newElements);
  }

  length() {
    let count = 0;
    for (const _ of this.elements) {
      count++;
    }
    return count;
  }

  foldl(fn, acc) {
    let result = acc;
    for (const el of this.elements) {
      result = fn(result, el);
    }
    return result;
  }

  foldr(fn, acc) {
    let result = acc;
    for (let i = this.elements.length - 1; i >= 0; i--) {
      result = fn(result, this.elements[i]);
    }
    return result;
  }

  reverse() {
    const reversed = [];
    for (let i = this.elements.length - 1; i >= 0; i--) {
      reversed[reversed.length] = this.elements[i];
    }
    return new List(reversed);
  }
}
