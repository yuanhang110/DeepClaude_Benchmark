export const clean = (input) => {
  // Check for letters
  if (/[a-zA-Z]/.test(input)) {
    throw new Error('Letters not permitted');
  }
  
  // Check for invalid punctuations
  if (/[@:!]/.test(input)) {
    throw new Error('Punctuations not permitted');
  }
  
  // Remove all non-digit characters
  let digits = input.replace(/\D/g, '');
  
  // Check length
  if (digits.length < 10) {
    throw new Error('Incorrect number of digits');
  }
  if (digits.length > 11) {
    throw new Error('More than 11 digits');
  }
  
  // Handle country code
  if (digits.length === 11) {
    if (digits[0] !== '1') {
      throw new Error('11 digits must start with 1');
    }
    digits = digits.slice(1);
  }
  
  // Validate NANP format
  const areaCode = digits[0];
  const exchangeCode = digits[3];
  
  if (areaCode === '0') {
    throw new Error('Area code cannot start with zero');
  }
  if (areaCode === '1') {
    throw new Error('Area code cannot start with one');
  }
  if (exchangeCode === '0') {
    throw new Error('Exchange code cannot start with zero');
  }
  if (exchangeCode === '1') {
    throw new Error('Exchange code cannot start with one');
  }
  
  return digits;
};
