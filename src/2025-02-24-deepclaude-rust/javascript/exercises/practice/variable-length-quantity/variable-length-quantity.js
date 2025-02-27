//
// This is only a SKELETON file for the 'Variable Length Quantity' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const encode = (numbers) => {
  const result = [];
  for (const num of numbers) {
    let n = num >>> 0;
    const chunks = [];
    do {
      const chunk = n & 0x7f;
      chunks.push(chunk);
      n = n >>> 7;
    } while (n > 0);

    chunks.reverse();

    for (let i = 0; i < chunks.length; i++) {
      let byte = chunks[i];
      if (i !== chunks.length - 1) {
        byte |= 0x80;
      }
      result.push(byte);
    }
  }
  return result;
};

export const decode = (bytes) => {
  const result = [];
  let current = 0;
  let hasData = false;

  for (const byte of bytes) {
    current = (current << 7) | (byte & 0x7f);
    hasData = true;

    if ((byte & 0x80) === 0) {
      result.push(current >>> 0);
      current = 0;
      hasData = false;
    }
  }

  if (hasData) {
    throw new Error('Incomplete sequence');
  }

  return result;
};
