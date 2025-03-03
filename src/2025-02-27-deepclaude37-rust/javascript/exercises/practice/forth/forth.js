//
// This is only a SKELETON file for the 'Forth' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Forth {
  constructor() {
    this._stack = [];
    this.customWords = new Map();
  }

  evaluate(input) {
    const tokens = this.tokenize(input);
    this.processTokens(tokens);
  }

  tokenize(input) {
    return input.split(/\s+/g)
      .filter(t => t !== '')
      .map(t => {
        if (/^-?\d+$/.test(t)) {
          return t;
        } else {
          return t.toUpperCase();
        }
      });
  }

  processTokens(tokens) {
    let i = 0;
    while (i < tokens.length) {
      const token = tokens[i];
      if (token === ':') {
        i = this.parseDefinition(tokens, i);
      } else {
        this.handleToken(token);
        i++;
      }
    }
  }

  parseDefinition(tokens, i) {
    if (i + 1 >= tokens.length) {
      throw new Error('Invalid definition');
    }
    const name = tokens[i + 1];
    if (/^-?\d+$/.test(name)) {
      throw new Error('Cannot redefine numbers');
    }
    let j = i + 2;
    const definition = [];
    while (j < tokens.length && tokens[j] !== ';') {
      definition.push(tokens[j]);
      j++;
    }
    if (j >= tokens.length || tokens[j] !== ';') {
      throw new Error('Unclosed definition');
    }
    this.customWords.set(name.toUpperCase(), definition);
    return j + 1;
  }

  handleToken(token) {
    if (/^-?\d+$/.test(token)) {
      this._stack.push(parseInt(token, 10));
    } else {
      if (this.customWords.has(token)) {
        const definition = this.customWords.get(token);
        this.processTokens(definition);
      } else {
        this.executeBuiltIn(token);
      }
    }
  }

  executeBuiltIn(token) {
    switch (token) {
      case '+':
        this.ensureStackSize(2);
        const a1 = this._stack.pop();
        const b1 = this._stack.pop();
        this._stack.push(b1 + a1);
        break;
      case '-':
        this.ensureStackSize(2);
        const a2 = this._stack.pop();
        const b2 = this._stack.pop();
        this._stack.push(b2 - a2);
        break;
      case '*':
        this.ensureStackSize(2);
        const a3 = this._stack.pop();
        const b3 = this._stack.pop();
        this._stack.push(b3 * a3);
        break;
      case '/':
        this.ensureStackSize(2);
        const a4 = this._stack.pop();
        if (a4 === 0) {
          throw new Error('Division by zero');
        }
        const b4 = this._stack.pop();
        this._stack.push(Math.trunc(b4 / a4));
        break;
      case 'DUP':
        this.ensureStackSize(1);
        const top = this._stack[this._stack.length - 1];
        this._stack.push(top);
        break;
      case 'DROP':
        this.ensureStackSize(1);
        this._stack.pop();
        break;
      case 'SWAP':
        this.ensureStackSize(2);
        const a5 = this._stack.pop();
        const b5 = this._stack.pop();
        this._stack.push(a5);
        this._stack.push(b5);
        break;
      case 'OVER':
        this.ensureStackSize(2);
        const second = this._stack[this._stack.length - 2];
        this._stack.push(second);
        break;
      default:
        throw new Error(`Unknown word: ${token}`);
    }
  }

  ensureStackSize(size) {
    if (this._stack.length < size) {
      throw new Error('Stack underflow');
    }
  }

  get stack() {
    return [...this._stack];
  }
}
