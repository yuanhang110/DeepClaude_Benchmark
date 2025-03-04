export class Palindromes {
  static generate({ minFactor, maxFactor }) {
    if (minFactor > maxFactor) {
      throw new Error('min must be <= max');
    }

    // Helper function to check if a number is a palindrome
    const isPalindrome = (num) => {
      const str = num.toString();
      return str === str.split('').reverse().join('');
    };

    const palindromes = new Map();

    // Iterate through all possible products
    for (let i = minFactor; i <= maxFactor; i++) {
      for (let j = i; j <= maxFactor; j++) {
        const product = i * j;
        if (isPalindrome(product)) {
          if (!palindromes.has(product)) {
            palindromes.set(product, []);
          }
          palindromes.get(product).push([i, j]);
        }
      }
    }

    // Get all palindrome products
    const products = Array.from(palindromes.keys());

    if (products.length === 0) {
      return {
        smallest: { value: null, factors: [] },
        largest: { value: null, factors: [] }
      };
    }

    const minProduct = Math.min(...products);
    const maxProduct = Math.max(...products);

    return {
      smallest: {
        value: minProduct,
        factors: palindromes.get(minProduct)
      },
      largest: {
        value: maxProduct,
        factors: palindromes.get(maxProduct)
      }
    };
  }
}
