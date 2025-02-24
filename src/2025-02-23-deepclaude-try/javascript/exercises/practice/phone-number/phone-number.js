//
// This is only a SKELETON file for the 'Phone Number' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const clean = (number) => {
  // Check for letters
  if (/[a-zA-Z]/.test(number)) {
    throw new Error('Letters not permitted');
  }

  // Check for invalid punctuations (only allow +, -, ., space, parentheses)
  if (/[^\d+()\- .]/.test(number)) {
    throw new Error('Punctuations not permitted');
  }

  // Extract digits and check length
  let digits = number.replace(/\D/g, '');
  const numDigits = digits.length;

  if (numDigits < 10) {
    throw new Error('Incorrect number of digits');
  } else if (numDigits > 11) {
    throw new Error('More than 11 digits');
  }

  // Check country code
  if (numDigits === 11) {
    if (digits[0] !== '1') {
      throw new Error('11 digits must start with 1');
    }
    digits = digits.substring(1);
  }

  // Validate area code
  const areaCode = digits.substring(0, 3);
  if (areaCode[0] === '0') {
    throw new Error('Area code cannot start with zero');
  } else if (areaCode[0] === '1') {
    throw new Error('Area code cannot start with one');
  }

  // Validate exchange code
  const exchangeCode = digits.substring(3, 6);
  if (exchangeCode[0] === '0') {
    throw new Error('Exchange code cannot start with zero');
  } else if (exchangeCode[0] === '1') {
    throw new Error('Exchange code cannot start with one');
  }

  return digits;
};
