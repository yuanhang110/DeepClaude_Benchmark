import { Forth } from './forth';

describe('Forth', () => {
  let forth;

  beforeEach(() => {
    forth = new Forth();
  });

  describe('parsing and numbers', () => {
    test('numbers just get pushed onto the stack', () => {
      forth.evaluate('1 2 3 4 5');
      expect(forth.stack).toEqual([1, 2, 3, 4, 5]);
    });

    test('pushes negative numbers onto the stack', () => {
      forth.evaluate('-1 -2 -3 -4 -5');
      expect(forth.stack).toEqual([-1, -2, -3, -4, -5]);
    });
  });

  describe('addition', () => {
    test('can add two numbers', () => {
      forth.evaluate('1 2 +');
      expect(forth.stack).toEqual([3]);
    });

    test('errors if there is nothing on the stack', () => {
      expect(() => {
        forth.evaluate('+');
      }).toThrow(new Error('Stack empty'));
    });

    test('errors if there is only one value on the stack', () => {
      expect(() => {
        forth.evaluate('1 +');
      }).toThrow(new Error('Stack empty'));
    });
  });

  describe('subtraction', () => {
    test('can subtract two numbers', () => {
      forth.evaluate('3 4 -');
      expect(forth.stack).toEqual([-1]);
    });

    test('errors if there is nothing on the stack', () => {
      expect(() => {
        forth.evaluate('-');
      }).toThrow(new Error('Stack empty'));
    });

    test('errors if there is only one value on the stack', () => {
      expect(() => {
        forth.evaluate('1 -');
      }).toThrow(new Error('Stack empty'));
    });
  });

  describe('multiplication', () => {
    test('can multiply two numbers', () => {
      forth.evaluate('2 4 *');
      expect(forth.stack).toEqual([8]);
    });

    test('errors if there is nothing on the stack', () => {
      expect(() => {
        forth.evaluate('*');
      }).toThrow(new Error('Stack empty'));
    });

    test('errors if there is only one value on the stack', () => {
      expect(() => {
        forth.evaluate('1 *');
      }).toThrow(new Error('Stack empty'));
    });
  });

  describe('division', () => {
    test('can divide two numbers', () => {
      forth.evaluate('12 3 /');
      expect(forth.stack).toEqual([4]);
    });

    test('performs integer division', () => {
      forth.evaluate('8 3 /');
      expect(forth.stack).toEqual([2]);
    });

    test('errors if dividing by zero', () => {
      expect(() => {
        forth.evaluate('4 0 /');
      }).toThrow(new Error('Division by zero'));
    });

    test('errors if there is nothing on the stack', () => {
      expect(() => {
        forth.evaluate('/');
      }).toThrow(new Error('Stack empty'));
    });

    test('errors if there is only one value on the stack', () => {
      expect(() => {
        forth.evaluate('1 /');
      }).toThrow(new Error('Stack empty'));
    });
  });

  describe('combined arithmetic', () => {
    test('addition and subtraction', () => {
      forth.evaluate('1 2 + 4 -');
      expect(forth.stack).toEqual([-1]);
    });

    test('multiplication and division', () => {
      forth.evaluate('2 4 * 3 /');
      expect(forth.stack).toEqual([2]);
    });
  });

  describe('dup', () => {
    test('copies a value on the stack', () => {
      forth.evaluate('1 dup');
      expect(forth.stack).toEqual([1, 1]);
    });

    test('copies the top value on the stack', () => {
      forth.evaluate('1 2 dup');
      expect(forth.stack).toEqual([1, 2, 2]);
    });

    test('errors if there is nothing on the stack', () => {
      expect(() => {
        forth.evaluate('dup');
      }).toThrow(new Error('Stack empty'));
    });
  });

  describe('drop', () => {
    test('removes the top value on the stack if it is the only one', () => {
      forth.evaluate('1 drop');
      expect(forth.stack).toEqual([]);
    });

    test('removes the top value on the stack if it is not the only one', () => {
      forth.evaluate('1 2 drop');
      expect(forth.stack).toEqual([1]);
    });

    test('errors if there is nothing on the stack', () => {
      expect(() => {
        forth.evaluate('drop');
      }).toThrow(new Error('Stack empty'));
    });
  });

  describe('swap', () => {
    test('swaps the top two values on the stack if they are the only ones', () => {
      forth.evaluate('1 2 swap');
      expect(forth.stack).toEqual([2, 1]);
    });

    test('swaps the top two values on the stack if they are not the only ones', () => {
      forth.evaluate('1 2 3 swap');
      expect(forth.stack).toEqual([1, 3, 2]);
    });

    test('errors if there is nothing on the stack', () => {
      expect(() => {
        forth.evaluate('swap');
      }).toThrow(new Error('Stack empty'));
    });

    test('errors if there is only one value on the stack', () => {
      expect(() => {
        forth.evaluate('1 swap');
      }).toThrow(new Error('Stack empty'));
    });
  });

  describe('over', () => {
    test('copies the second element if there are only two', () => {
      forth.evaluate('1 2 over');
      expect(forth.stack).toEqual([1, 2, 1]);
    });

    test('copies the second element if there are more than two', () => {
      forth.evaluate('1 2 3 over');
      expect(forth.stack).toEqual([1, 2, 3, 2]);
    });

    test('errors if there is nothing on the stack', () => {
      expect(() => {
        forth.evaluate('over');
      }).toThrow(new Error('Stack empty'));
    });

    test('errors if there is only one value on the stack', () => {
      expect(() => {
        forth.evaluate('1 over');
      }).toThrow(new Error('Stack empty'));
    });
  });

  describe('user-defined words', () => {
    test('can consist of built-in words', () => {
      forth.evaluate(': dup-twice dup dup ;');
      forth.evaluate('1 dup-twice');
      expect(forth.stack).toEqual([1, 1, 1]);
    });

    test('execute in the right order', () => {
      forth.evaluate(': countup 1 2 3 ;');
      forth.evaluate('countup');
      expect(forth.stack).toEqual([1, 2, 3]);
    });

    test('can override other user-defined words', () => {
      forth.evaluate(': foo dup ;');
      forth.evaluate(': foo dup dup ;');
      forth.evaluate('1 foo');
      expect(forth.stack).toEqual([1, 1, 1]);
    });

    test('can override built-in words', () => {
      forth.evaluate(': swap dup ;');
      forth.evaluate('1 swap');
      expect(forth.stack).toEqual([1, 1]);
    });

    test('can override built-in operators', () => {
      forth.evaluate(': + * ;');
      forth.evaluate('3 4 +');
      expect(forth.stack).toEqual([12]);
    });

    test('can use different words with the same name', () => {
      forth.evaluate(': foo 5 ;');
      forth.evaluate(': bar foo ;');
      forth.evaluate(': foo 6 ;');
      forth.evaluate('bar foo');
      expect(forth.stack).toEqual([5, 6]);
    });

    test('can define word that uses word with the same name', () => {
      forth.evaluate(': foo 10 ;');
      forth.evaluate(': foo foo 1 + ;');
      forth.evaluate('foo');
      expect(forth.stack).toEqual([11]);
    });

    test('cannot redefine numbers', () => {
      expect(() => {
        forth.evaluate(': 1 2 ;');
      }).toThrow(new Error('Invalid definition'));
    });
    test('cannot redefine negative numbers', () => {
      expect(() => {
        forth.evaluate(': -1 2 ;');
      }).toThrow(new Error('Invalid definition'));
    });

    test('errors if executing a non-existent word', () => {
      expect(() => {
        forth.evaluate('foo');
      }).toThrow(new Error('Unknown command'));
    });

    test('only defines locally', () => {
      const first = new Forth();
      const second = new Forth();
      first.evaluate(': + - ;');
      first.evaluate('1 1 +');
      second.evaluate('1 1 +');
      expect(first.stack).toEqual([0]);
      expect(second.stack).toEqual([2]);
    });
  });

  describe('case-insensitivity', () => {
    test('DUP is case-insensitive', () => {
      forth.evaluate('1 DUP Dup dup');
      expect(forth.stack).toEqual([1, 1, 1, 1]);
    });

    test('DROP is case-insensitive', () => {
      forth.evaluate('1 2 3 4 DROP Drop drop');
      expect(forth.stack).toEqual([1]);
    });

    test('SWAP is case-insensitive', () => {
      forth.evaluate('1 2 SWAP 3 Swap 4 swap');
      expect(forth.stack).toEqual([2, 3, 4, 1]);
    });

    test('OVER is case-insensitive', () => {
      forth.evaluate('1 2 OVER Over over');
      expect(forth.stack).toEqual([1, 2, 1, 2, 1]);
    });

    test('user-defined words are case-insensitive', () => {
      forth.evaluate(': foo dup ;');
      forth.evaluate('1 FOO Foo foo');
      expect(forth.stack).toEqual([1, 1, 1, 1]);
    });

    test('definitions are case-insensitive', () => {
      forth.evaluate(': SWAP DUP Dup dup ;');
      forth.evaluate('1 swap');
      expect(forth.stack).toEqual([1, 1, 1, 1]);
    });
  });
});
