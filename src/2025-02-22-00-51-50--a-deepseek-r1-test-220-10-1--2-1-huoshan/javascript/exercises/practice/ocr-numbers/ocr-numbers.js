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
  const lines = input.split('\n');
  
  // Validate line count is multiple of 4 and all lengths are valid
  if (lines.length % 4 !== 0 || lines.some(l => l.length % 3 !== 0)) {
    throw new Error('Invalid input size');
  }
  
  const results = [];
  
  // Process each group of 4 lines (3 data lines + 1 blank)
  for (let i = 0; i < lines.length; i += 4) {
    const group = lines.slice(i, i + 3); // Skip the 4th blank line
    const blankLine = lines[i + 3];
    
    // Verify the blank line is truly blank
    if (blankLine.trim() !== '') {
      throw new Error('Invalid input - missing blank line');
    }
    const numDigits = group[0].length / 3;
    let digits = '';
    
    // Validate all lines in group have same length
    if (group.some(l => l.length !== group[0].length)) {
      throw new Error('Inconsistent line lengths');
    }
    
    // Process each digit
    for (let d = 0; d < numDigits; d++) {
      const start = d * 3;
      const key = group.map(l => l.substr(start, 3)).join('');
      digits += digitMap[key] || '?';
    }
    
    results.push(digits);
  }
  
  return results.join(',');
};
