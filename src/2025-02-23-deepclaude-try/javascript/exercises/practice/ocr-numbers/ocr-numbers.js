//
// This is only a SKELETON file for the 'OCR Numbers' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const convert = (input) => {
  const digitPatterns = {
    ' _ | ||_|': '0',
    '     |  |': '1',
    ' _ _| |_': '2',
    ' _ _| _| ': '3',
    '   |_|  |': '4',
    ' _ |_  _|': '5',
    ' _ |_ |_|': '6',
    ' _   |  |': '7',
    ' _ |_||_|': '8',
    ' _ |_| _|': '9'
  };

  // Split input into lines
  const lines = input.split('\n');
  
  // Validate total line count
  if (lines.length % 4 !== 0) {
    throw new Error('Number of lines must be a multiple of four');
  }

  const result = [];
  
  // Process each group of 4 lines
  for (let i = 0; i < lines.length; i += 4) {
    const group = lines.slice(i, i + 4);
    
    // Check fourth line is blank
    if (group[3].trim() !== '') {
      throw new Error('Fourth line of each group must be blank');
    }

    const firstThree = group.slice(0, 3);
    const width = firstThree[0].length;
    
    // Validate line lengths
    if (width % 3 !== 0 || firstThree.some(line => line.length !== width)) {
      throw new Error('Invalid line length');
    }

    const digitCount = width / 3;
    let digits = '';
    
    // Process each digit in the line
    for (let d = 0; d < digitCount; d++) {
      const parts = [];
      for (let j = 0; j < 3; j++) {
        const start = d * 3;
        const end = start + 3;
        parts.push(firstThree[j].substring(start, end));
      }
      const key = parts.join('');
      digits += digitPatterns[key] || '?';
    }

    result.push(digits);
  }

  return result.join(',');
};
