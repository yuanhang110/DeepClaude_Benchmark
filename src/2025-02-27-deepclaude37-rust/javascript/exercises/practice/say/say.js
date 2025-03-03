//
// This is only a SKELETON file for the 'Say' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const say = (n) => {
  if (typeof n !== 'number' || n < 0 || n > 999999999999 || !Number.isInteger(n)) {
    throw new Error('Number must be between 0 and 999,999,999,999.');
  }

  if (n === 0) {
    return 'zero';
  }

  const ones = [
    'zero', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine',
    'ten', 'eleven', 'twelve', 'thirteen', 'fourteen', 'fifteen', 'sixteen',
    'seventeen', 'eighteen', 'nineteen'
  ];
  
  const tens = [
    '', '', 'twenty', 'thirty', 'forty', 'fifty', 'sixty', 'seventy', 'eighty', 'ninety'
  ];

  const convertLessThanOneHundred = (number) => {
    if (number < 20) {
      return ones[number];
    }
    const tenPart = tens[Math.floor(number / 10)];
    const remainder = number % 10;
    if (remainder === 0) {
      return tenPart;
    }
    return tenPart + '-' + ones[remainder];
  };

  const convertLessThanOneThousand = (number) => {
    if (number < 100) {
      return convertLessThanOneHundred(number);
    }
    const hundredPart = ones[Math.floor(number / 100)] + ' hundred';
    const remainder = number % 100;
    if (remainder === 0) {
      return hundredPart;
    }
    return hundredPart + ' ' + convertLessThanOneHundred(remainder);
  };

  // Split into chunks of thousands
  const chunks = [];
  let num = n;
  while (num > 0) {
    chunks.push(num % 1000);
    num = Math.floor(num / 1000);
  }
  chunks.reverse();

  const scales = ['', 'thousand', 'million', 'billion'];
  const parts = [];
  
  for (let i = 0; i < chunks.length; i++) {
    const chunk = chunks[i];
    if (chunk === 0) continue; // Skip zero chunks
    
    const chunkText = convertLessThanOneThousand(chunk);
    const scaleIndex = chunks.length - 1 - i;
    const scale = scales[scaleIndex];
    
    parts.push(chunkText + (scale ? ' ' + scale : ''));
  }

  return parts.join(' ');
};
