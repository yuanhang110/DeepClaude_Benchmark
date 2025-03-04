const ONES = [
  'zero', 'one', 'two', 'three', 'four',
  'five', 'six', 'seven', 'eight', 'nine'
];

const TEENS = [
  'ten', 'eleven', 'twelve', 'thirteen', 'fourteen',
  'fifteen', 'sixteen', 'seventeen', 'eighteen', 'nineteen'
];

const TENS = [
  '', 'ten', 'twenty', 'thirty', 'forty',
  'fifty', 'sixty', 'seventy', 'eighty', 'ninety'
];

const SCALES = ['', 'thousand', 'million', 'billion', 'trillion'];

function chunkNumber(n) {
  const chunks = [];
  while (n > 0) {
    chunks.push(n % 1000);
    n = Math.floor(n / 1000);
  }
  return chunks;
}

function sayChunk(n) {
  if (n === 0) return '';
  
  let words = [];
  const hundreds = Math.floor(n / 100);
  if (hundreds > 0) {
    words.push(ONES[hundreds], 'hundred');
  }
  
  const remainder = n % 100;
  if (remainder < 10) {
    if (remainder > 0) words.push(ONES[remainder]);
  } else if (remainder < 20) {
    words.push(TEENS[remainder - 10]);
  } else {
    const tens = Math.floor(remainder / 10);
    const ones = remainder % 10;
    if (ones > 0) {
      words.push(`${TENS[tens]}-${ONES[ones]}`);
    } else {
      words.push(TENS[tens]);
    }
  }
  
  return words.join(' ');
}

export const say = (n) => {
  if (n < 0 || n > 999999999999) {
    throw new Error('Number must be between 0 and 999,999,999,999.');
  }
  
  if (n === 0) return ONES[0];
  
  const chunks = chunkNumber(n);
  let result = [];
  
  chunks.forEach((chunk, i) => {
    if (chunk > 0) {
      result.unshift(sayChunk(chunk), SCALES[i]);
    }
  });
  
  return result.filter(x => x).join(' ').trim();
};
