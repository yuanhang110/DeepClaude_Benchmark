//
// This is only a SKELETON file for the 'OCR Numbers' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

// Define the patterns for each digit
const digitMap = {
  ' _ | ||_|': '0',
  '     |  |': '1',
  ' _  _||_ ': '2',
  ' _  _| _|': '3',
  '   |_|  |': '4',
  ' _ |_  _|': '5',
  ' _ |_ |_|': '6',
  ' _   |  |': '7',
  ' _ |_||_|': '8',
  ' _ |_| _|': '9'
};

export const convert = (input) => {
  // Split input into lines
  const lines = input.split('\n');
  
  // Check if number of lines is valid (multiple of 4)
  if (lines.length % 4 !== 0) {
    throw new Error('Number of lines must be a multiple of four');
  }
  
  const result = [];
  
  // Process each group of 4 lines
  for (let i = 0; i < lines.length; i += 4) {
    const group = lines.slice(i, i + 4);
    
    // Validate line lengths
    const lineLength = group[0].length;
    if (group.slice(0, 3).some(line => line.length !== lineLength)) {
      throw new Error('Lines must have consistent length');
    }
    
    if (lineLength % 3 !== 0) {
      throw new Error('Line length must be a multiple of 3');
    }
    
    // Check that fourth line is blank
    if (group[3].trim() !== '') {
      throw new Error('Fourth line must be blank');
    }
    
    // Process each digit in the line
    const numDigits = lineLength / 3;
    let digits = '';
    
    for (let d = 0; d < numDigits; d++) {
      // Extract the 3-character chunks for each row of this digit
      const start = d * 3;
      const end = start + 3;
      
      const line1 = group[0].substring(start, end);
      const line2 = group[1].substring(start, end);
      const line3 = group[2].substring(start, end);
      
      // Create the key to look up in the digit map
      const key = line1 + line2 + line3;
      
      // Look up the digit or use '?' if not recognized
      digits += digitMap[key] || '?';
    }
    
    result.push(digits);
  }
  
  // Join multiple lines with commas
  return result.join(',');
};
