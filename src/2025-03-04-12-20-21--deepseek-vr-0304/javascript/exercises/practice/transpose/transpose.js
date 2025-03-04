//
// This is only a SKELETON file for the 'Transpose' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const transpose = (input) => {
  if (input.length === 0) return [];
  const maxLen = Math.max(...input.map(row => row.length));
  const result = [];
  for (let c = 0; c < maxLen; c++) {
    let transposedRow = '';
    for (const row of input) {
      transposedRow += c < row.length ? row[c] : ' ';
    }
    transposedRow = transposedRow.replace(/\s+$/, '');
    result.push(transposedRow);
  }
  return result;
};
