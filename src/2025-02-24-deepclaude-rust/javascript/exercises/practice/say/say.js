//
// This is only a SKELETON file for the 'Say' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const say = (n) => {
  if (n < 0 || n > 999999999999) {
    throw new Error('Number must be between 0 and 999,999,999,999.');
  }

  if (n === 0) return 'zero';

  const ones = [
    '', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine',
    'ten', 'eleven', 'twelve', 'thirteen', 'fourteen', 'fifteen', 'sixteen',
    'seventeen', 'eighteen', 'nineteen'
  ];
  
  const tens = [
    '', '', 'twenty', 'thirty', 'forty', 'fifty', 'sixty', 'seventy', 'eighty', 'ninety'
  ];

  const scales = ['', 'thousand', 'million', 'billion'];

  function sayUnder100(num) {
    if (num === 0) return '';
    if (num < 20) return ones[num];
    
    const tensValue = Math.floor(num / 10);
    const remainder = num % 10;
    return remainder === 0 ? tens[tensValue] : `${tens[tensValue]}-${ones[remainder]}`;
  }

  function sayUnder1000(num) {
    if (num === 0) return '';
    
    const hundreds = Math.floor(num / 100);
    const remainder = num % 100;
    
    let result = '';
    if (hundreds > 0) {
      result = `${ones[hundreds]} hundred`;
      if (remainder > 0) result += ' ';
    }
    
    if (remainder > 0) {
      result += sayUnder100(remainder);
    }
    
    return result;
  }

  // Split number into groups of three digits
  const chunks = [];
  let remaining = n;
  while (remaining > 0) {
    chunks.push(remaining % 1000);
    remaining = Math.floor(remaining / 1000);
  }

  // Process each chunk with its scale
  const parts = chunks
    .map((chunk, index) => {
      if (chunk === 0) return '';
      const words = sayUnder1000(chunk);
      return index === 0 ? words : `${words} ${scales[index]}`;
    })
    .filter(part => part !== '')
    .reverse();

  return parts.join(' ');
};
