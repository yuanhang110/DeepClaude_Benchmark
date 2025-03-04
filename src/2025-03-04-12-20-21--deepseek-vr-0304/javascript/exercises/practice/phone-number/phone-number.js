//
// This is only a SKELETON file for the 'Phone Number' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const clean = (number) => {
  // Check for letters
  if (/[a-zA-Z]/.test(number)) {
    throw new Error('Letters not permitted');
  }

  // Check for invalid punctuation
  if (/[^\d\s().+-]/.test(number)) {
    throw new Error('Punctuations not permitted');
  }

  // Clean the number by removing all non-digits
  let cleaned = number.replace(/[^\d]/g, '');

  // Check length and country code
  if (cleaned.length === 11) {
    if (cleaned.startsWith('1')) {
      cleaned = cleaned.substring(1);
    } else {
      throw new Error('11 digits must start with 1');
    }
  }

  // Check number of digits
  if (cleaned.length !== 10) {
    throw new Error('Incorrect number of digits');
  }

  // Check area code
  if (cleaned[0] === '0') {
    throw new Error('Area code cannot start with zero');
  }
  if (cleaned[0] === '1') {
    throw new Error('Area code cannot start with one');
  }

  // Check exchange code
  if (cleaned[3] === '0') {
    throw new Error('Exchange code cannot start with zero');
  }
  if (cleaned[3] === '1') {
    throw new Error('Exchange code cannot start with one');
  }

  return cleaned;
};
