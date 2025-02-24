//
// This is only a SKELETON file for the 'Forth' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Forth {
  constructor() {
    this._stack = [];
    this._customWords = new Map();
  }

  evaluate(input) {
    const tokens = input.toUpperCase().split(/[\s]+/).filter(t => t !== '');
    let definingCustom = false;
    let customWordName = '';
    let customWordDef = [];

    let i = 0;
    while (i < tokens.length) {
      const token = tokens[i];
      i++;
      
      if (definingCustom) {
        if (token === ';') {
          this._customWords.set(customWordName, customWordDef);
          definingCustom = false;
        } else if (!customWordName) {
          if (/^\d+$/.test(token)) {
            throw new Error('Invalid word name');
          }
          customWordName = token;
        } else {
          customWordDef.push(token);
        }
        continue;
      }

      if (token === ':') {
        definingCustom = true;
        customWordName = '';
        customWordDef = [];
        continue;
      }

      // Handle numbers
      if (/^\d+$/.test(token)) {
        this._stack.push(parseInt(token, 10));
        continue;
      }

      // Resolve custom words first
      if (this._customWords.has(token)) {
        const def = this._customWords.get(token);
        tokens.splice(i, 1, ...def);  // Replace current token with definition
        i = 0;  // Restart processing from beginning of modified tokens
        continue;
      }

      // Built-in operations
      switch(token) {
        case '+': this._checkStackSize(2); this._stack.push(this._stack.pop() + this._stack.pop()); break;
        case '-': this._checkStackSize(2); { const a = this._stack.pop(); this._stack.push(this._stack.pop() - a); } break;
        case '*': this._checkStackSize(2); this._stack.push(this._stack.pop() * this._stack.pop()); break;
        case '/': this._checkStackSize(2); { const a = this._stack.pop(); if (a === 0) throw new Error('Division by zero'); this._stack.push(Math.floor(this._stack.pop() / a)); } break;
        case 'DUP': this._checkStackSize(1); this._stack.push(this._stack[this._stack.length - 1]); break;
        case 'DROP': this._checkStackSize(1); this._stack.pop(); break;
        case 'SWAP': this._checkStackSize(2); { const a = this._stack.pop(), b = this._stack.pop(); this._stack.push(a, b); } break;
        case 'OVER': this._checkStackSize(2); this._stack.push(this._stack[this._stack.length - 2]); break;
        default: throw new Error(`Unknown word: ${token}`);
      }
    }
  }

  _checkStackSize(n) {
    if (this._stack.length < n) {
      throw new Error('Stack underflow');
    }
  }

  get stack() {
    return [...this._stack];
  }
}
