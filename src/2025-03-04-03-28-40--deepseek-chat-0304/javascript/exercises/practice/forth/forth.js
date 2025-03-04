export class Forth {
  constructor() {
    this._stack = [];
    this._words = {};
  }

  evaluate(input) {
    const tokens = input.toUpperCase().split(/\s+/).filter(t => t.length > 0);
    let i = 0;
    
    while (i < tokens.length) {
      const token = tokens[i];
      
      if (this._words[token]) {
        // Evaluate custom word
        const wordTokens = this._words[token].split(' ');
        tokens.splice(i, 1, ...wordTokens);
        continue;
      }
      
      if (token === ':') {
        // Word definition
        if (i + 3 >= tokens.length || tokens[i + 2] !== ';') {
          throw new Error('Invalid definition');
        }
        const wordName = tokens[i + 1].toUpperCase();
        if (!isNaN(wordName)) {
          throw new Error('Invalid definition');
        }
        // Get definition between : and ;
        const definition = [];
        let j = i + 2;
        while (j < tokens.length && tokens[j] !== ';') {
          definition.push(tokens[j]);
          j++;
        }
        this._words[wordName] = definition.join(' ');
        i = j + 1;
        continue;
      }
      
      if (!isNaN(token)) {
        // Number
        this._stack.push(Number(token));
      } else {
        // Operation
        switch (token) {
          case '+':
            this._checkStackSize(2);
            this._stack.push(this._stack.pop() + this._stack.pop());
            break;
          case '-':
            this._checkStackSize(2);
            const b = this._stack.pop();
            const a = this._stack.pop();
            this._stack.push(a - b);
            break;
          case '*':
            this._checkStackSize(2);
            this._stack.push(this._stack.pop() * this._stack.pop());
            break;
          case '/':
            this._checkStackSize(2);
            const divisor = this._stack.pop();
            if (divisor === 0) throw new Error('Division by zero');
            this._stack.push(Math.floor(this._stack.pop() / divisor));
            break;
          case 'DUP':
            this._checkStackSize(1);
            this._stack.push(this._stack[this._stack.length - 1]);
            break;
          case 'DROP':
            this._checkStackSize(1);
            this._stack.pop();
            break;
          case 'SWAP':
            this._checkStackSize(2);
            [this._stack[this._stack.length - 1], this._stack[this._stack.length - 2]] = 
              [this._stack[this._stack.length - 2], this._stack[this._stack.length - 1]];
            break;
          case 'OVER':
            this._checkStackSize(2);
            this._stack.push(this._stack[this._stack.length - 2]);
            break;
          default:
            throw new Error('Unknown command');
        }
      }
      i++;
    }
  }

  _checkStackSize(required) {
    if (this._stack.length < required) {
      throw new Error('Stack empty');
    }
  }

  get stack() {
    return [...this._stack];
  }
}
