//
// This is only a SKELETON file for the 'Killer Sudoku Helper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

const backtrack = (start, path, remainingSum, length, results) => {
  if (path.length === length) {
    if (remainingSum === 0) {
      results.push([...path]);
    }
    return;
  }

  for (let i = start; i <= 9; i++) {
    // Handle single-digit cages explicitly first
    if (length === 1 && i !== remainingSum) continue;
    
    // Prune branches that can't possibly sum up to remainingSum
    if (i > remainingSum) break;
    
    // Skip numbers that would leave insufficient remaining numbers to complete the combination
    const remainingNumbersNeeded = length - path.length - 1;
    if (9 - i < remainingNumbersNeeded) break;
    
    backtrack(i + 1, [...path, i], remainingSum - i, length, results);
  }
};

export const combinations = (cage) => {
  const results = [];
  // Handle single-digit cage case explicitly
  if (cage.length === 1) {
    if (cage.sum >= 1 && cage.sum <= 9) {
      results.push([cage.sum]);
    }
    return results;
  }
  backtrack(1, [], cage.sum, cage.length, results);
  return results;
};
