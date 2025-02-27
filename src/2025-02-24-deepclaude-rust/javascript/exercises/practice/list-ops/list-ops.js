//
// This is only a SKELETON file for the 'List Ops' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class List {
  constructor(elements = []) {
    this.elements = [...elements];
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
    let filtered = [];
    for (const el of this.elements) {
      if (predicate(el)) {
        filtered = [...filtered, el];
      }
    }
    return new List(filtered);
  }

  map(fn) {
    let mapped = [];
    for (const el of this.elements) {
      mapped = [...mapped, fn(el)];
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
    let reversed = [];
    for (let i = this.elements.length - 1; i >= 0; i--) {
      reversed = [...reversed, this.elements[i]];
    }
    return new List(reversed);
  }
}
