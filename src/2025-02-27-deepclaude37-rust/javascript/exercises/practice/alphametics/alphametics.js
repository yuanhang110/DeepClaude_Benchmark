//
// This is only a SKELETON file for the 'Alphametics' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const solve = (equation) => {
  // Parse the equation - handle case where equation doesn't contain ' = '
  const parts = equation.split(' = ');
  if (parts.length !== 2) return null;
  
  const [leftStr, right] = parts;
  if (!leftStr || !right) return null;
  
  const left = leftStr.split(' + ');
  
  // Filter out any empty strings and check for invalid input
  if (left.some(word => !word) || !right) return null;
  
  // Collect all unique letters and leading letters
  const letters = new Set();
  const leadingLetters = new Set();
  for (const word of [...left, right]) {
    if (word && word[0]) {
      leadingLetters.add(word[0]);
    }
    for (const c of word) {
      letters.add(c);
    }
  }
  const lettersArray = Array.from(letters);

  // If more than 10 unique letters, no solution is possible
  if (lettersArray.length > 10) {
    return null;
  }

  // Generate permutations with backtracking
  const used = new Array(10).fill(false);
  const current = [];
  
  // Try to find a valid solution
  const solution = findSolution(0);
  return solution;
  
  // Helper function for backtracking
  function findSolution(index) {
    // If we've assigned digits to all letters, check if solution is valid
    if (index === lettersArray.length) {
      const mapping = {};
      for (let i = 0; i < lettersArray.length; i++) {
        mapping[lettersArray[i]] = current[i];
      }
      
      // Check if the equation balances
      if (isValidSolution(mapping)) {
        return mapping;
      }
      return null;
    }
    
    const letter = lettersArray[index];
    // Leading letters can't be assigned 0
    const startDigit = leadingLetters.has(letter) ? 1 : 0;
    
    for (let digit = startDigit; digit <= 9; digit++) {
      if (!used[digit]) {
        used[digit] = true;
        current.push(digit);
        
        const result = findSolution(index + 1);
        if (result) {
          return result;
        }
        
        current.pop();
        used[digit] = false;
      }
    }
    
    return null;
  }
  
  // Helper function to check if a mapping produces a valid equation
  function isValidSolution(mapping) {
    // Calculate sum of left side
    let leftSum = 0;
    for (const word of left) {
      let num = 0;
      for (const c of word) {
        num = num * 10 + mapping[c];
      }
      leftSum += num;
    }
    
    // Calculate right side
    let rightNum = 0;
    for (const c of right) {
      rightNum = rightNum * 10 + mapping[c];
    }
    
    return leftSum === rightNum;
  }
};
