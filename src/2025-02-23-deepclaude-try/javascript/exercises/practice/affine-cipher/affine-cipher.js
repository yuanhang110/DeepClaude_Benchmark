// Helper functions for text processing
const cleanText = (text) => text.toLowerCase().replace(/[^a-z0-9]/g, '');
const groupText = (text) => text.match(/.{1,5}/g)?.join(' ') || '';

// Check if a number is coprime with 26 (has no common factors with 26 other than 1)
const isCoprime = (a) => (a % 2 !== 0) && (a % 13 !== 0);

// Find modular multiplicative inverse of a mod 26
const modInverse = (a) => {
  a = a % 26;
  for (let x = 1; x < 26; x++) {
    if ((a * x) % 26 === 1) return x;
  }
  throw new Error('No modular inverse exists');
};

export const encode = (phrase, key) => {
  const { a, b } = key;
  if (!isCoprime(a)) throw new Error('a and m must be coprime.');
  
  const cleaned = cleanText(phrase);
  let encrypted = '';
  
  for (const char of cleaned) {
    if (/\d/.test(char)) {
      encrypted += char;
    } else {
      const i = char.charCodeAt(0) - 97; // 'a' is 97
      const e = (a * i + b) % 26;
      encrypted += String.fromCharCode(e + 97);
    }
  }
  
  return groupText(encrypted);
};

export const decode = (phrase, key) => {
  const { a, b } = key;
  if (!isCoprime(a)) throw new Error('a and m must be coprime.');
  const invA = modInverse(a);
  
  const cleaned = cleanText(phrase.replace(/ /g, ''));
  let decrypted = '';
  
  for (const char of cleaned) {
    if (/\d/.test(char)) {
      decrypted += char;
    } else {
      const y = char.charCodeAt(0) - 97;
      const d = (invA * ((y - b + 26) % 26)) % 26;
      decrypted += String.fromCharCode(d + 97);
    }
  }
  
  return decrypted;
};
