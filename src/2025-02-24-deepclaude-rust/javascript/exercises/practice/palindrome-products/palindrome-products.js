//
// This is only a SKELETON file for the 'Palindrome Products' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Palindromes {
  static generate({ minFactor, maxFactor }) {
    if (minFactor === undefined || maxFactor === undefined) {
      throw new Error('minFactor and maxFactor must be specified');
    }
    if (minFactor > maxFactor) {
      throw new Error('min must be <= max');
    }

    const isPalindrome = (n) => {
      const s = String(n);
      return s === s.split('').reverse().join('');
    };

    const palindromes = {};

    for (let a = minFactor; a <= maxFactor; a++) {
      for (let b = a; b <= maxFactor; b++) {
        const product = a * b;
        if (isPalindrome(product)) {
          if (!palindromes[product]) {
            palindromes[product] = [];
          }
          palindromes[product].push([a, b]);
        }
      }
    }

    const palindromeValues = Object.keys(palindromes).map(Number);
    if (palindromeValues.length === 0) {
      return {
        smallest: { value: null, factors: [] },
        largest: { value: null, factors: [] }
      };
    }

    const smallestValue = Math.min(...palindromeValues);
    const largestValue = Math.max(...palindromeValues);

    return {
      smallest: { value: smallestValue, factors: palindromes[smallestValue] },
      largest: { value: largestValue, factors: palindromes[largestValue] }
    };
  }
}
