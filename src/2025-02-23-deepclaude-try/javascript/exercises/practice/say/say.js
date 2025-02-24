export const say = (n) => {
  const ones = ['', 'one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine'];
  const teens = ['ten', 'eleven', 'twelve', 'thirteen', 'fourteen', 'fifteen', 'sixteen', 'seventeen', 'eighteen', 'nineteen'];
  const tens = ['', '', 'twenty', 'thirty', 'forty', 'fifty', 'sixty', 'seventy', 'eighty', 'ninety'];
  const scaleNames = ['', 'thousand', 'million', 'billion', 'trillion'];

  function convertTens(num) {
    if (num === 0) return '';
    if (num < 10) return ones[num];
    if (num < 20) return teens[num - 10];
    const ten = Math.floor(num / 10);
    const one = num % 10;
    return tens[ten] + (one !== 0 ? '-' + ones[one] : '');
  }

  function convertChunk(num) {
    if (num === 0) return '';
    const hundred = Math.floor(num / 100);
    const remainder = num % 100;
    let result = '';
    if (hundred > 0) {
      result += ones[hundred] + ' hundred ';
    }
    const tensStr = convertTens(remainder);
    if (tensStr) {
      result += tensStr;
    }
    return result.trim().replace(/\s+/g, ' ');
  }

  if (typeof n !== 'number' || n < 0 || n > 999999999999 || !Number.isInteger(n)) {
    throw new Error('Number must be between 0 and 999,999,999,999.');
  }

  if (n === 0) {
    return 'zero';
  }

  const chunks = [];
  let current = n;
  while (current > 0) {
    chunks.push(current % 1000);
    current = Math.floor(current / 1000);
  }
  const reversedChunks = chunks.reverse();

  const parts = [];
  for (let i = 0; i < reversedChunks.length; i++) {
    const chunk = reversedChunks[i];
    const chunkStr = convertChunk(chunk);
    if (chunkStr) {
      const scaleIndex = reversedChunks.length - 1 - i;
      const scale = scaleNames[scaleIndex];
      parts.push(chunkStr + (scale ? ` ${scale}` : ''));
    }
  }

  return parts.join(' ');
};
