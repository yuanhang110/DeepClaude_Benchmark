//
// This is only a SKELETON file for the 'Say' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const say = (n) => {
  if (n < 0 || n > 999999999999) {
    throw new Error('Number must be between 0 and 999,999,999,999.');
  }

  if (n === 0) return 'zero';

  const numbers0to19 = ['', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine',
                      'ten', 'eleven', 'twelve', 'thirteen', 'fourteen', 'fifteen', 'sixteen',
                      'seventeen', 'eighteen', 'nineteen'];
  const tens = ['twenty', 'thirty', 'forty', 'fifty', 'sixty', 'seventy', 'eighty', 'ninety'];

  const processChunk = (num) => {
    if (num === 0) return '';
    const parts = [];
    const hundreds = Math.floor(num / 100);
    const remainder = num % 100;
    
    if (hundreds > 0) {
      parts.push(`${numbers0to19[hundreds]} hundred`);
    }
    
    if (remainder > 0) {
      if (remainder < 20) {
        parts.push(numbers0to19[remainder]);
      } else {
        const ten = Math.floor(remainder / 10);
        const unit = remainder % 10;
        parts.push(unit === 0 ? tens[ten - 2] : `${tens[ten - 2]}-${numbers0to19[unit]}`);
      }
    }
    
    return parts.join(' ');
  };

  const splitNumber = (num) => {
    const chunks = [];
    while (num > 0) {
      chunks.push(num % 1000);
      num = Math.floor(num / 1000);
    }
    return chunks;
  };

  const chunks = splitNumber(n);
  const scales = ['', 'thousand', 'million', 'billion'];
  const resultParts = [];
  
  chunks.forEach((chunk, index) => {
    const chunkText = processChunk(chunk);
    if (chunkText) {
      const scale = scales[index];
      resultParts.unshift(`${chunkText}${scale ? ' ' + scale : ''}`);
    }
  });

  return resultParts.join(' ');
};
