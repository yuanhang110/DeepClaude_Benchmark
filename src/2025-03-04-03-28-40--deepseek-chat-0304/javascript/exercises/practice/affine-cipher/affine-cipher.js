// Greatest Common Divisor
const gcd = (a, b) => b === 0 ? a : gcd(b, a % b);

// Modular Multiplicative Inverse
const mmi = (a, m) => {
  for (let x = 1; x < m; x++) {
    if ((a * x) % m === 1) return x;
  }
  return null;
};

// Only process letters, keep case and digits
const processChar = (char, a, b, m, encode) => {
  if (/[0-9]/.test(char)) return char;
  if (!/[a-zA-Z]/.test(char)) return '';
  
  const offset = 'a'.charCodeAt(0);
  const i = char.toLowerCase().charCodeAt(0) - offset;
  
  let y;
  if (encode) {
    y = (a * i + b) % m;
  } else {
    const aInverse = mmi(a, m);
    if (aInverse === null) throw new Error('a and m must be coprime.');
    y = (aInverse * (i - b)) % m;
    if (y < 0) y += m; // Handle negative results
  }
  
  return String.fromCharCode(y + offset);
};

export const encode = (phrase, {a, b}) => {
  const m = 26;
  if (gcd(a, m) !== 1) throw new Error('a and m must be coprime.');
  
  const processed = phrase
    .toLowerCase()
    .split('')
    .map(char => processChar(char, a, b, m, true))
    .join('');
  
  // Split into 5-character groups
  return processed
    .match(/.{1,5}/g)
    .join(' ')
    .trim();
};

export const decode = (phrase, {a, b}) => {
  const m = 26;
  if (gcd(a, m) !== 1) throw new Error('a and m must be coprime.');
  
  return phrase
    .toLowerCase()
    .replace(/\s+/g, '')
    .split('')
    .map(char => processChar(char, a, b, m, false))
    .join('');
};
