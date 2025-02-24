//
// This is only a SKELETON file for the 'Variable Length Quantity' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const encode = (numbers) => {
  return numbers.flatMap(number => {
    if (number === 0) return [0x00];
    
    const chunks = [];
    while (number > 0) {
      chunks.push(number & 0x7f);
      number = number >>> 7;
    }
    
    chunks.reverse();
    
    for (let i = 0; i < chunks.length - 1; i++) {
      chunks[i] |= 0x80;
    }
    
    return chunks;
  });
};

export const decode = (bytes) => {
  const result = [];
  let currentValue = 0;
  let continuation = false;

  for (const byte of bytes) {
    if (byte & 0x80) {
      continuation = true;
    } else {
      continuation = false;
    }

    currentValue = (currentValue << 7) | (byte & 0x7f);

    if (!continuation) {
      result.push(currentValue >>> 0); // Convert to unsigned 32-bit
      currentValue = 0;
    } else if (byte === bytes[bytes.length - 1]) {
      throw new Error('Incomplete sequence');
    }
  }

  if (continuation) {
    throw new Error('Incomplete sequence');
  }

  return result;
};
