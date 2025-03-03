//
// This is only a SKELETON file for the 'Palindrome Products' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Palindromes {
  static generate({ minFactor, maxFactor }) {
    if (minFactor > maxFactor) {
      throw new Error('min must be <= max');
    }

    const products = new Map();

    for (let a = minFactor; a <= maxFactor; a++) {
      for (let b = a; b <= maxFactor; b++) {
        const product = a * b;
        if (Palindromes.isPalindrome(product)) {
          if (!products.has(product)) {
            products.set(product, []);
          }
          products.get(product).push([a, b]);
        }
      }
    }

    const palindromes = Array.from(products.keys());
    if (palindromes.length === 0) {
      return {
        smallest: { value: null, factors: [] },
        largest: { value: null, factors: [] }
      };
    }

    const min = Math.min(...palindromes);
    const max = Math.max(...palindromes);

    return {
      smallest: { value: min, factors: products.get(min) },
      largest: { value: max, factors: products.get(max) },
    };
  }

  static isPalindrome(num) {
    const str = num.toString();
    return str === str.split('').reverse().join('');
  }
}
