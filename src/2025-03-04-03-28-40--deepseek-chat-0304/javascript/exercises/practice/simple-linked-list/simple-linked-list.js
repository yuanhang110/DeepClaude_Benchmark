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
    
    // Initialize list with array of song IDs
    for (const value of values) {
      this.add(value);
    }
  }

  add(nextValue) {
    const element = new Element(nextValue, this._head);
    this._head = element;
    this._length++;
  }

  get length() {
    return this._length;
  }

  get head() {
    return this._head;
  }

  toArray() {
    const array = [];
    let current = this._head;
    
    while (current) {
      array.push(current.value);
      current = current.next;
    }
    
    return array;
  }

  reverse() {
    const values = this.toArray();
    this._head = null;
    this._length = 0;
    
    // Rebuild the list in reverse order
    for (const value of values) {
      this.add(value);
    }
    
    return this;
  }
}
