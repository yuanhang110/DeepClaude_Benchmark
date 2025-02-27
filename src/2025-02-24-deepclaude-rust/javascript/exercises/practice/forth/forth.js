//
// This is only a SKELETON file for the 'Forth' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Forth {
  constructor() {
    this._stack = [];
    this._dictionary = new Map();
    // Initialize built-in words
    this._dictionary.set('+', this.add.bind(this));
    this._dictionary.set('-', this.subtract.bind(this));
    this._dictionary.set('*', this.multiply.bind(this));
    this._dictionary.set('/', this.divide.bind(this));
    this._dictionary.set('dup', this.dup.bind(this));
    this._dictionary.set('drop', this.drop.bind(this));
    this._dictionary.set('swap', this.swap.bind(this));
    this._dictionary.set('over', this.over.bind(this));
  }

  add() {
    if (this._stack.length < 2) throw new Error('Stack empty');
    const b = this._stack.pop();
    const a = this._stack.pop();
    this._stack.push(a + b);
  }

  subtract() {
    if (this._stack.length < 2) throw new Error('Stack empty');
    const b = this._stack.pop();
    const a = this._stack.pop();
    this._stack.push(a - b);
  }

  multiply() {
    if (this._stack.length < 2) throw new Error('Stack empty');
    const b = this._stack.pop();
    const a = this._stack.pop();
    this._stack.push(a * b);
  }

  divide() {
    if (this._stack.length < 2) throw new Error('Stack empty');
    const b = this._stack.pop();
    const a = this._stack.pop();
    if (b === 0) throw new Error('Division by zero');
    this._stack.push(Math.floor(a / b));
  }

  dup() {
    if (this._stack.length < 1) throw new Error('Stack empty');
    this._stack.push(this._stack[this._stack.length - 1]);
  }

  drop() {
    if (this._stack.length < 1) throw new Error('Stack empty');
    this._stack.pop();
  }

  swap() {
    if (this._stack.length < 2) throw new Error('Stack empty');
    const b = this._stack.pop();
    const a = this._stack.pop();
    this._stack.push(b);
    this._stack.push(a);
  }

  over() {
    if (this._stack.length < 2) throw new Error('Stack empty');
    this._stack.push(this._stack[this._stack.length - 2]);
  }

  tokenize(input) {
    return input.split(/\s+/).filter(t => t !== '').map(t => {
      if (/^-?\d+$/.test(t)) {
        return parseInt(t, 10);
      }
      return t.toLowerCase();
    });
  }

  evaluate(input) {
    const tokens = this.tokenize(input);
    const tokenStack = [{ tokens, index: 0 }];
    const tempDefinitions = new Map();
    
    while (tokenStack.length > 0) {
      const currentFrame = tokenStack[tokenStack.length - 1];
      const { tokens } = currentFrame;
      
      if (currentFrame.index >= tokens.length) {
        tokenStack.pop();
        continue;
      }
      
      const token = tokens[currentFrame.index++];
      
      if (token === ':') {
        if (currentFrame.index >= tokens.length) {
          throw new Error('Invalid definition');
        }
        const wordName = tokens[currentFrame.index++];
        if (typeof wordName !== 'string' || /^-?\d+$/.test(wordName)) {
          throw new Error('Invalid definition');
        }
        
        let j = currentFrame.index;
        while (j < tokens.length && tokens[j] !== ';') {
          j++;
        }
        if (j >= tokens.length) {
          throw new Error('Definition not terminated');
        }
        
        const definitionTokens = tokens.slice(currentFrame.index, j);
        const name = wordName.toLowerCase();
        this._dictionary.set(name, definitionTokens);
        currentFrame.index = j + 1;
      } else {
        if (typeof token === 'number') {
          this._stack.push(token);
        } else {
          const word = token.toLowerCase();
          const definition = this._dictionary.get(word);
          if (!definition) {
            throw new Error('Unknown command');
          }
          if (typeof definition === 'function') {
            definition.call(this);
          } else {
            tokenStack.push({ tokens: definition, index: 0 });
          }
        }
      }
    }
  }

  get stack() {
    return [...this._stack];
  }
}
