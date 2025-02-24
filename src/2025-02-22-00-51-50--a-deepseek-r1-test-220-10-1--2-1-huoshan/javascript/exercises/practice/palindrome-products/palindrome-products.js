//
// This is only a SKELETON file for the 'Palindrome Products' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Palindromes {
  static generate({ minFactor, maxFactor }) {
    if (minFactor > maxFactor) {
      throw new Error('min must be <= max');
    }

    const palindromes = {};

    for (let a = minFactor; a <= maxFactor; a++) {
      for (let b = a; b <= maxFactor; b++) {
        const product = a * b;
        const str = product.toString();
        if (str === str.split('').reverse().join('')) {
          if (!palindromes[product]) {
            palindromes[product] = [];
          }
          palindromes[product].push([a, b]);
        }
      }
    }

    const products = Object.keys(palindromes).map(Number);
    if (products.length === 0) {
      return {
        smallest: { value: null, factors: [] },
        largest: { value: null, factors: [] }
      };
    }

    const minProduct = Math.min(...products);
    const maxProduct = Math.max(...products);

    return {
      smallest: { value: minProduct, factors: palindromes[minProduct] },
      largest: { value: maxProduct, factors: palindromes[maxProduct] },
    };
  }
}
