//
// This is only a SKELETON file for the 'Palindrome Products' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Palindromes {
  static generate({ minFactor, maxFactor }) {
    if (minFactor > maxFactor) {
      throw new Error('min must be <= max');
    }

    const palindromes = new Map();

    // Generate all products and find palindromes
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

    // Find smallest and largest palindromes
    let smallest = { value: null, factors: [] };
    let largest = { value: null, factors: [] };

    if (palindromes.size > 0) {
      const sortedProducts = Array.from(palindromes.keys()).sort((a, b) => a - b);
      smallest = {
        value: sortedProducts[0],
        factors: palindromes.get(sortedProducts[0])
      };
      largest = {
        value: sortedProducts[sortedProducts.length - 1],
        factors: palindromes.get(sortedProducts[sortedProducts.length - 1])
      };
    }

    return { smallest, largest };
  }
}

// Helper function to check if a number is palindrome
function isPalindrome(num) {
  const str = num.toString();
  return str === str.split('').reverse().join('');
}
