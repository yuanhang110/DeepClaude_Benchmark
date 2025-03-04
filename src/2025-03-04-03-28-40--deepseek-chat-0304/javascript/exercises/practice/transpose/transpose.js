export const transpose = (input) => {
  if (input.length === 0) return [];
  
  // Find the maximum line length
  const maxLength = Math.max(...input.map(line => line.length));
  
  // Pad lines with spaces on the right to make them equal length
  const padded = input.map(line => line.padEnd(maxLength, ' '));
  
  // Transpose the matrix
  const transposed = [];
  for (let i = 0; i < maxLength; i++) {
    let column = '';
    for (let j = 0; j < padded.length; j++) {
      column += padded[j][i];
    }
    transposed.push(column);
  }
  
  // Remove only trailing spaces that were added by padding
  return transposed.map(line => {
    // Find the last non-space character
    const lastCharIndex = line.split('').findLastIndex(c => c !== ' ');
    // If the line is all spaces, return empty string
    if (lastCharIndex === -1) return '';
    // Return the line up to and including the last non-space character
    return line.slice(0, lastCharIndex + 1);
  });
};
