export const solve = (puzzle) => {
  // Split puzzle into operands and result
  const [operandsStr, resultStr] = puzzle.split(/\s*=\s*/);
  if (!resultStr) return null; // Invalid puzzle format
  const operands = operandsStr.split(/\s*\+\s*/);
  const allWords = [...operands, resultStr];
  
  // Get unique characters and identify leading characters
  const chars = [...new Set(allWords.join(''))];
  const leadingChars = new Set([...operands.map(w => w[0]), resultStr[0]]);
  
  // Generate permutations helper
  function* permute(available, used = []) {
    if (available.length === 0) yield used;
    for (let i = 0; i < available.length; i++) {
      const next = available[i];
      if (next === 0 && leadingChars.has(chars[used.length])) continue;
      yield* permute(
        [...available.slice(0, i), ...available.slice(i + 1)],
        [...used, next]
      );
    }
  }
  
  // Check if a permutation solves the puzzle
  function testSolution(perm) {
    const charMap = chars.reduce((acc, char, i) => 
      ({...acc, [char]: perm[i]}), {});
    
    const nums = allWords.map(word => 
      parseInt([...word].map(c => charMap[c]).join('')));
    
    const sum = nums.slice(0, -1).reduce((a, b) => a + b, 0);
    return sum === nums[nums.length - 1];
  }
  
  // Try all possible permutations of digits 0-9
  const digits = [0,1,2,3,4,5,6,7,8,9];
  for (const perm of permute(digits)) {
    if (testSolution(perm)) {
      return chars.reduce((acc, char, i) => 
        ({...acc, [char]: perm[i]}), {});
    }
  }
  
  return null;
};
