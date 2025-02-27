//
// This is only a SKELETON file for the 'OCR Numbers' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const convert = (input) => {
  const lines = input.split('\n');
  if (lines.length % 4 !== 0) {
    throw new Error('Invalid input size');
  }

  const groups = [];
  for (let i = 0; i < lines.length; i += 4) {
    const groupLines = lines.slice(i, i + 4);
    if (groupLines[3].trim() !== '') {
      throw new Error('Invalid input: fourth line not blank');
    }
    groups.push(groupLines.slice(0, 3));
  }

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
    ' _ |_| _|': '9',
  };

  const result = [];
  for (const [line1, line2, line3] of groups) {
    const length = line1.length;
    if (line2.length !== length || line3.length !== length || length % 3 !== 0) {
      throw new Error('Invalid input line length');
    }

    const numDigits = length / 3;
    let digits = '';
    for (let i = 0; i < numDigits; i++) {
      const seg1 = line1.substring(i * 3, i * 3 + 3);
      const seg2 = line2.substring(i * 3, i * 3 + 3);
      const seg3 = line3.substring(i * 3, i * 3 + 3);
      const key = seg1 + seg2 + seg3;
      digits += digitMap[key] || '?';
    }

    result.push(digits);
  }

  return result.join(',');
};
