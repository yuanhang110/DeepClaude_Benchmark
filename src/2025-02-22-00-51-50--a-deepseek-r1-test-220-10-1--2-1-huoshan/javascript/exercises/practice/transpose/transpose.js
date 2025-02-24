//
// This is only a SKELETON file for the 'Transpose' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const transpose = (rows) => {
  if (rows.length === 0) return [];
  const maxLength = Math.max(...rows.map(row => row.length));
  const transposed = [];
  
  for (let col = 0; col < maxLength; col++) {
    let transposedRow = '';
    // Build the transposed row by checking each original row's character at current column
    let lastNonPadding = -1;
    for (const row of rows) {
      if (col < row.length) {
        transposedRow += row[col];
        lastNonPadding = transposedRow.length - 1;  // Track last non-padding character position
      } else {
        transposedRow += ' ';
      }
    }
    // Only trim padding spaces from end (those added due to short rows)
    if (lastNonPadding !== -1) {
      transposedRow = transposedRow.substring(0, lastNonPadding + 1);
    } else {
      transposedRow = '';
    }
    transposed.push(transposedRow);
  }
  
  return transposed;
};
