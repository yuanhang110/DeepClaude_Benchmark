export const solve = (puzzle) => {
  // Validate input format
  const parts = puzzle.split(' = ');
  if (parts.length !== 2) return null;
  
  const [leftSide, rightSide] = parts;
  const addends = leftSide.split(' + ').filter(Boolean);
  const sum = rightSide;
  
  // Validate we have at least one addend and a sum
  if (addends.length === 0 || !sum) return null;
  
  // Get unique letters and track leading letters (can't be zero)  
  const letters = new Set([...puzzle.match(/[A-Z]/g)]);
  // Only get first letters from valid words
  const validWords = [...addends, sum].filter(word => word && word.length > 0);
  const leadingLetters = new Set(validWords.map(word => word[0]));
  
  // Can't solve if more than 10 unique letters
  if (letters.size > 10) return null;
  
  // Helper to check if assignment works
  const isValidSolution = (letterMap) => {
    // Check no leading zeros
    for (const letter of leadingLetters) {
      if (letterMap[letter] === 0) return false;
    }
    
    // Convert words to numbers
    const numbers = addends.map(word => 
      Number([...word].map(c => letterMap[c]).join(''))
    );
    const total = Number([...sum].map(c => letterMap[c]).join(''));
    
    // Check if sum is valid
    return numbers.reduce((a, b) => a + b, 0) === total;
  };

  // Try all possible digit assignments
  const letters_arr = [...letters];
  const used = new Set();
  const letterMap = {};
  
  // Recursive function to try assignments
  const tryAssignment = (index) => {
    if (index === letters_arr.length) {
      return isValidSolution(letterMap);
    }
    
    for (let digit = 0; digit <= 9; digit++) {
      if (!used.has(digit)) {
        used.add(digit);
        letterMap[letters_arr[index]] = digit;
        if (tryAssignment(index + 1)) return true;
        used.delete(digit);
      }
    }
    return false;
  };

  // Find solution
  return tryAssignment(0) ? letterMap : null;
};
