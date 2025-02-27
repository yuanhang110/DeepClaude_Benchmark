//
// This is only a SKELETON file for the 'Phone Number' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const clean = (number) => {
  if (/[a-zA-Z]/.test(number)) {
    throw new Error('Letters not permitted');
  }

  const hasNonDigits = /\D/.test(number);

  let cleaned = number.replace(/\D/g, '');

  if (cleaned.length < 10) {
    if (hasNonDigits) {
      throw new Error('Punctuations not permitted');
    } else {
      throw new Error('Incorrect number of digits');
    }
  } else if (cleaned.length > 11) {
    throw new Error('More than 11 digits');
  }

  if (cleaned.length === 11) {
    if (cleaned[0] !== '1') {
      throw new Error('11 digits must start with 1');
    }
    cleaned = cleaned.substring(1);
  }

  if (cleaned[0] === '0') {
    throw new Error('Area code cannot start with zero');
  }
  if (cleaned[0] === '1') {
    throw new Error('Area code cannot start with one');
  }

  if (cleaned[3] === '0') {
    throw new Error('Exchange code cannot start with zero');
  }
  if (cleaned[3] === '1') {
    throw new Error('Exchange code cannot start with one');
  }

  return cleaned;
};
