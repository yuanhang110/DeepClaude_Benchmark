export const encode = (numbers) => {
  if (!numbers || numbers.length === 0) return [];
  
  const result = [];
  
  for (const num of numbers) {
    if (num < 0 || num > 0xFFFFFFFF) {
      throw new Error('Invalid number. Must be 32-bit unsigned integer');
    }
    
    let value = num;
    let bytes = [];
    
    do {
      let byte = value & 0x7F;
      value >>>= 7;
      if (bytes.length > 0) {
        byte |= 0x80;
      }
      bytes.unshift(byte);
    } while (value > 0);
    
    result.push(...bytes);
  }
  
  return result;
};

export const decode = (bytes) => {
  if (!bytes || bytes.length === 0) return [];
  
  const result = [];
  let num = 0;
  
  for (let i = 0; i < bytes.length; i++) {
    const byte = bytes[i];
    
    if ((byte & 0x80) === 0) {
      // Last byte in sequence
      num = (num << 7) | (byte >>> 0);
      result.push(num);
      num = 0;
    } else {
      // More bytes to come
      num = (num << 7) | ((byte & 0x7F) >>> 0);
      
      // Check for incomplete sequence
      if (i === bytes.length - 1) {
        throw new Error('Incomplete sequence');
      }
    }
  }
  
  return result;
};
