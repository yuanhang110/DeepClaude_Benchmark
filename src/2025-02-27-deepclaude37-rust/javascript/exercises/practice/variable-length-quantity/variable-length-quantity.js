//
// This is only a SKELETON file for the 'Variable Length Quantity' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const encode = (numbers) => {
  const result = [];
  
  for (const num of numbers) {
    let chunks = [];
    let current = num;
    
    // Handle zero as a special case
    if (current === 0) {
      chunks.push(0);
    } else {
      // Extract 7-bit chunks starting from the least significant bits
      while (current > 0) {
        chunks.push(current & 0x7F);
        current = current >>> 7; // Use unsigned right shift
      }
    }
    
    // Reverse to get most significant chunks first
    chunks.reverse();
    
    // Set the continuation bit (bit 7) for all except the last byte
    for (let i = 0; i < chunks.length; i++) {
      if (i !== chunks.length - 1) {
        chunks[i] |= 0x80; // Set the high bit
      }
      result.push(chunks[i]);
    }
  }
  
  return result;
};

export const decode = (bytes) => {
  const result = [];
  let current = 0;
  let inSequence = false;
  
  for (const byte of bytes) {
    // Extract the 7 data bits and add them to our current value
    // Use >>> 0 to force treatment as unsigned 32-bit integer
    current = ((current << 7) | (byte & 0x7F)) >>> 0;
    
    // Check if we're in a continuation sequence
    inSequence = (byte & 0x80) !== 0;
    
    if (!inSequence) {
      // This byte has no continuation bit set, so we've completed a number
      result.push(current);
      current = 0;
    }
  }
  
  // If we're still in a sequence after processing all bytes, the input is incomplete
  if (inSequence) {
    throw new Error('Incomplete sequence');
  }
  
  return result;
};
