const gcd = (a, b) => {
  while (b !== 0) {
    [a, b] = [b, a % b];
  }
  return a;
};

const areCoprime = (a, m) => gcd(a, m) === 1;

const modInverse = (a, m) => {
  a = ((a % m) + m) % m; // Ensure a is positive
  for (let x = 1; x < m; x++) {
    if ((a * x) % m === 1) {
      return x;
    }
  }
  throw new Error('Modular inverse does not exist');
};

const processText = (text) => text.toLowerCase().replace(/[^a-z0-9]/g, '');

const groupText = (text) => {
  const chunks = [];
  for (let i = 0; i < text.length; i += 5) {
    chunks.push(text.slice(i, i + 5));
  }
  return chunks.join(' ');
};

export const encode = (phrase, key) => {
  const { a, b } = key;
  if (!areCoprime(a, 26)) {
    throw new Error('a and m must be coprime.');
  }

  const processed = processText(phrase);
  let encrypted = '';
  for (const char of processed) {
    if (/\d/.test(char)) {
      encrypted += char;
    } else {
      const i = char.charCodeAt(0) - 97;
      const e = (a * i + b) % 26;
      encrypted += String.fromCharCode(e + 97);
    }
  }

  return groupText(encrypted);
};

export const decode = (phrase, key) => {
  const { a, b } = key;
  if (!areCoprime(a, 26)) {
    throw new Error('a and m must be coprime.');
  }

  const processed = processText(phrase);
  const aInv = modInverse(a, 26);
  let decrypted = '';

  for (const char of processed) {
    if (/\d/.test(char)) {
      decrypted += char;
    } else {
      const y = char.charCodeAt(0) - 97;
      // Calculate (aInv * (y - b)) mod 26, ensuring no negative numbers
      const bMod = ((b % 26) + 26) % 26;
      const d = (aInv * (y - bMod + 26)) % 26;
      decrypted += String.fromCharCode(d + 97);
    }
  }

  return decrypted;
};
