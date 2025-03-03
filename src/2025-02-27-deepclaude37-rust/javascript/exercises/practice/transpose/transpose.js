//
// This is only a SKELETON file for the 'Transpose' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const transpose = (rows) => {
  if (rows.length === 0) return [];
  const maxLen = Math.max(...rows.map(row => row.length));
  const transposed = [];
  
  for (let i = 0; i < maxLen; i++) {
    let line = '';
    for (const row of rows) {
      line += i < row.length ? row[i] : ' ';
    }
    // Don't trim trailing spaces as they may be part of the input
    transposed.push(line);
  }
  
  return transposed;
};
