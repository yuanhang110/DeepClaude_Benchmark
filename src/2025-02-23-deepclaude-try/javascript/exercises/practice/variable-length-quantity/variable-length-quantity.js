//
// This is only a SKELETON file for the 'Variable Length Quantity' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const encode = (numbers) => {
  if (typeof number !== 'number' || !Number.isInteger(number) || number < 0 || number > 0xFFFFFFFF) {
    throw new Error('Invalid number');
  }

  if (number === 0) {
    return [0x00];
  }

  let chunks = [];
  while (number > 0) {
    chunks.push(number & 0x7F);
    number = Math.floor(number / 128);
  }
  chunks = chunks.reverse();

  for (let i = 0; i < chunks.length - 1; i++) {
    chunks[i] |= 0x80;
  }

  return chunks;
};

export const decode = (bytes) => {
  if (!Array.isArray(bytes) || bytes.length === 0) {
    throw new Error('Invalid input');
  }

  for (let i = 0; i < bytes.length; i++) {
    const byte = bytes[i];
    if (i < bytes.length - 1 && (byte & 0x80) !== 0x80) {
      throw new Error('Invalid continuation byte');
    }
    if (i === bytes.length - 1 && (byte & 0x80) !== 0) {
      throw new Error('Last byte has continuation bit set');
    }
  }

  const result = [];
  let currentValue = 0;
  for (const byte of bytes) {
    result = (result << 7) | (byte & 0x7F);
  }

  if (result > 0xFFFFFFFF) {
    throw new Error('Number exceeds 32-bit unsigned integer');
  }

  return result;
};
