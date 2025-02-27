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
    
    // If values are provided, create elements in reverse order
    // to maintain the expected order
    for (let i = values.length - 1; i >= 0; i--) {
      this.add(values[i]);
    }
  }

  add(nextValue) {
    // Create a new element that points to the current head
    const newElement = new Element(nextValue, this._head);
    // Update head to the new element
    this._head = newElement;
    return this;
  }

  get length() {
    let count = 0;
    let current = this._head;
    
    // Traverse the list and count elements
    while (current !== null) {
      count++;
      current = current.next;
    }
    
    return count;
  }

  get head() {
    return this._head;
  }

  toArray() {
    const result = [];
    let current = this._head;
    
    // Traverse the list and collect values into an array
    while (current !== null) {
      result.push(current.value);
      current = current.next;
    }
    
    return result;
  }

  reverse() {
    // Create a new list
    const newList = new List();
    
    // Get all values from current list
    const values = this.toArray();
    
    // Add each value to the new list
    // Since add prepends, this will reverse the order
    values.forEach(value => {
      newList.add(value);
    });
    
    return newList;
  }
}
