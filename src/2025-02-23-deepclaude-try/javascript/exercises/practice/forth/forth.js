//
// This is only a SKELETON file for the 'Forth' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Forth {
  constructor() {
    this._stack = [];
    this.customWords = {};
  }

  evaluate(input) {
    const tokens = input.toUpperCase().split(/\s+/).filter(t => t !== '');
    let i = 0;
    while (i < tokens.length) {
      const token = tokens[i];
      if (token === ':') {
        i++;
        if (i >= tokens.length) {
          throw new Error('Invalid word definition');
        }
        const wordName = tokens[i];
        if (/^\d+$/.test(wordName)) {
          throw new Error('Invalid word name');
        }
        i++;
        const definition = [];
        while (i < tokens.length && tokens[i] !== ';') {
          definition.push(tokens[i]);
          i++;
        }
        if (i >= tokens.length) {
          throw new Error('Unclosed word definition');
        }
        i++;
        this.customWords[wordName.toUpperCase()] = definition;
      } else {
        this.processToken(token);
        i++;
      }
    }
  }

  processToken(token) {
    if (/^-?\d+$/.test(token)) {
      this._stack.push(parseInt(token, 10));
    } else if (this.customWords[token]) {
      this.evaluate(this.customWords[token].join(' '));
    } else {
      this.executeBuiltIn(token);
    }
  }

  executeBuiltIn(token) {
    switch (token) {
      case '+':
        this.ensureStackSize(2);
        this._stack.push(this._stack.pop() + this._stack.pop());
        break;
      case '-':
        this.ensureStackSize(2);
        const sub = this._stack.pop();
        this._stack.push(this._stack.pop() - sub);
        break;
      case '*':
        this.ensureStackSize(2);
        this._stack.push(this._stack.pop() * this._stack.pop());
        break;
      case '/':
        this.ensureStackSize(2);
        const divisor = this._stack.pop();
        if (divisor === 0) {
          throw new Error('Division by zero');
        }
        this._stack.push(Math.trunc(this._stack.pop() / divisor));
        break;
      case 'DUP':
        this.ensureStackSize(1);
        const dup = this._stack.pop();
        this._stack.push(dup, dup);
        break;
      case 'DROP':
        this.ensureStackSize(1);
        this._stack.pop();
        break;
      case 'SWAP':
        this.ensureStackSize(2);
        const a = this._stack.pop();
        const b = this._stack.pop();
        this._stack.push(a, b);
        break;
      case 'OVER':
        this.ensureStackSize(2);
        this._stack.push(this._stack[this._stack.length - 2]);
        break;
      default:
        throw new Error(`Unknown command: ${token}`);
    }
  }

  ensureStackSize(n) {
    if (this._stack.length < n) {
      throw new Error('Stack underflow');
    }
  }

  get stack() {
    return [...this._stack];
  }
}
