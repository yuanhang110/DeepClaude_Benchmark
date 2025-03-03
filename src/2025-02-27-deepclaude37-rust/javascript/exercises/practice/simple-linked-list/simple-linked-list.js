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
  constructor(values = []) {
    this._head = null;
    this._length = 0;
    for (const value of values) {
      this.add(value);
    }
  }

  add(nextValue) {
    // Add a new element to the front of the list
    // Extract value if nextValue is an Element
    const value = nextValue instanceof Element ? nextValue.value : nextValue;
    const element = new Element(value, this._head);
    this._head = element;
    this._length++;
    return this;
  }

  get length() {
    return this._length;
  }

  get head() {
    return this._head;
  }

  toArray() {
    const result = [];
    let current = this._head;
    
    while (current) {
      result.push(current.value);
      current = current.next;
    }
    
    return result;
  }

  reverse() {
    const reversed = new List();
    const values = this.toArray();
    
    // Since add prepends elements, we need to add them in the same order
    // to get the reverse (3,2,1 would become 1->2->3)
    for (const value of values) {
      reversed.add(value);
    }
    
    return reversed;
  }
}
