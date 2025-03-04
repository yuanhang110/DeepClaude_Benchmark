//
// This is only a SKELETON file for the 'Simple Linked List' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Element {
  constructor(value, next = null) {
    this._value = value;
    this._next = next;
  }

  get value() {
    return this._value;
  }

  get next() {
    return this._next;
  }
}

export class List {
  constructor(arr = []) {
    this._head = null;
    this._length = 0;
    arr.forEach(value => this.add(value));
  }

  add(nextValue) {
    this._head = new Element(nextValue, this._head);
    this._length++;
  }

  get length() {
    return this._length;
  }

  get head() {
    return this._head;
  }

  toArray() {
    let result = [];
    let current = this._head;
    while (current) {
      result.push(current.value);
      current = current.next;
    }
    return result;
  }

  reverse() {
    let prev = null;
    let current = this._head;
    while (current) {
      const next = current.next;
      current._next = prev;
      prev = current;
      current = next;
    }
    this._head = prev;
  }
}
