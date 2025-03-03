//
// This is only a SKELETON file for the 'Phone Number' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const clean = (number) => {
  // Check for letters in the input
  if (/[a-zA-Z]/.test(number)) {
    throw new Error('Letters not permitted');
  }

  // Check for punctuation that's not allowed
  if (/[^\d\s\(\)\-\.\+]/.test(number)) {
    throw new Error('Punctuations not permitted');
  }

  // Remove all non-digit characters
  let cleaned = number.replace(/\D/g, '');

  // Validate length and country code
  const length = cleaned.length;
  if (length < 10) {
    throw new Error('Incorrect number of digits');
  }
  if (length > 11) {
    throw new Error('More than 11 digits');
  }
  if (length === 11) {
    if (cleaned[0] !== '1') {
      throw new Error('11 digits must start with 1');
    }
    cleaned = cleaned.substring(1);
  }

  // Validate area code
  if (cleaned[0] === '0') {
    throw new Error('Area code cannot start with zero');
  }
  if (cleaned[0] === '1') {
    throw new Error('Area code cannot start with one');
  }

  // Validate exchange code
  if (cleaned[3] === '0') {
    throw new Error('Exchange code cannot start with zero');
  }
  if (cleaned[3] === '1') {
    throw new Error('Exchange code cannot start with one');
  }

  return cleaned;
};
