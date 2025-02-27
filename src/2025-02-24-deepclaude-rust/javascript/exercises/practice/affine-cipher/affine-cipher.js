function gcd(a, b) {
  while (b !== 0) {
    [a, b] = [b, a % b];
  }
  return a;
}

function modInverse(a, m) {
  a = ((a % m) + m) % m;
  for (let x = 1; x < m; x++) {
    if ((a * x) % m === 1) {
      return x;
    }
  }
  throw new Error('No inverse exists');
}

export const encode = (phrase, key) => {
  const { a, b } = key;
  if (gcd(a, 26) !== 1) {
    throw new Error('a and m must be coprime.');
  }

  const processed = phrase.toLowerCase().replace(/[^a-z0-9]/g, '');
  let encrypted = '';
  for (const c of processed) {
    if (/\d/.test(c)) {
      encrypted += c;
    } else {
      const i = c.charCodeAt(0) - 'a'.charCodeAt(0);
      const encrypted_i = (a * i + b) % 26;
      encrypted += String.fromCharCode(encrypted_i + 97);
    }
  }

  return encrypted.replace(/(.{5})/g, '$1 ').trim();
};

export const decode = (phrase, key) => {
  const { a, b } = key;
  if (gcd(a, 26) !== 1) {
    throw new Error('a and m must be coprime.');
  }

  const aInverse = modInverse(a, 26);
  const processed = phrase.replace(/ /g, '');
  let decrypted = '';
  for (const c of processed) {
    if (/\d/.test(c)) {
      decrypted += c;
    } else {
      const y = c.charCodeAt(0) - 'a'.charCodeAt(0);
      const decrypted_i = ((aInverse * (y - b) % 26) + 26) % 26;
      decrypted += String.fromCharCode(decrypted_i + 97);
    }
  }
  return decrypted;
};
