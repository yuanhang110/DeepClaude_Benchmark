//
// This is only a SKELETON file for the 'Killer Sudoku Helper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const combinations = (cage) => {
  const results = [];
  
  // Get sum and size from the cage object
  const sum = cage.sum;
  const size = cage.size;
  const excludedDigits = cage.excludedDigits || [];
  
  // Calculate minimum and maximum possible sums for the given cage size
  // Min sum: sum of smallest "size" digits (1+2+...+size)
  const minSum = (size * (size + 1)) / 2;
  // Max sum: sum of largest "size" digits ((10-size)+...+9)
  const maxSum = size * (19 - size) / 2;
  
  // If sum is outside possible range, return empty array
  if (sum < minSum || sum > maxSum) {
    return [];
  }
  
  // Recursive backtracking function to find all valid combinations
  function backtrack(start, currentCombo, currentSum) {
    // If we've selected the right number of digits
    if (currentCombo.length === size) {
      // If the sum matches and no digits are excluded, add to results
      if (currentSum === sum && !currentCombo.some(d => excludedDigits.includes(d))) {
        results.push([...currentCombo]);
      }
      return;
    }
    
    // Try each possible next digit
    for (let i = start; i <= 9; i++) {
      // Skip excluded digits
      if (excludedDigits.includes(i)) continue;
      
      // If adding this digit would exceed target sum, no need to try larger digits
      if (currentSum + i > sum) break;
      
      // Add this digit to the combination
      currentCombo.push(i);
      // Recursive call with updated parameters
      backtrack(i + 1, currentCombo, currentSum + i);
      // Remove this digit (backtrack)
      currentCombo.pop();
    }
  }
  
  // Start the backtracking process
  backtrack(1, [], 0);
  
  return results;
};
