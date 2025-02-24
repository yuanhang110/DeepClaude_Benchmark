//
// This is only a SKELETON file for the 'Phone Number' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const clean = (phone) => {
  // Remove all non-digit characters
  let cleaned = phone.replace(/\D/g, '');
  
  // Validate length before country code check
  if (cleaned.length < 10) throw new Error('Incorrect number of digits');
  if (cleaned.length > 11) throw new Error('More than 11 digits');
  
  // Validate country code
  if (cleaned.length === 11) {
    if (!cleaned.startsWith('1')) {
      throw new Error('11 digits must start with 1');
    }
    cleaned = cleaned.slice(1);
  }
  
  // Validate NXX NXX XXXX format
  const areaCode = cleaned.slice(0, 3);
  const exchangeCode = cleaned.slice(3, 6);
  const subscriberNumber = cleaned.slice(6);
  
  // Validate area code
  if (areaCode[0] === '0') throw new Error('Area code cannot start with zero');
  if (areaCode[0] === '1') throw new Error('Area code cannot start with one');
  
  // Validate exchange code
  if (exchangeCode[0] === '0') throw new Error('Exchange code cannot start with zero');
  if (exchangeCode[0] === '1') throw new Error('Exchange code cannot start with one');
  
  // Check for letters in original input
  if (/[a-zA-Z]/.test(phone)) throw new Error('Letters not permitted');
  
  // Check for invalid punctuation in original input
  if (/[^\d\s()+.-]/.test(phone)) throw new Error('Punctuations not permitted');

  return cleaned;
};
