export const combinations = (cage) => {
  const { sum, size } = cage;
  
  // Helper function to generate combinations
  function findCombinations(target, digitsNeeded, start, current, results) {
    if (digitsNeeded === 0) {
      if (target === 0) {
        results.push([...current].sort((a, b) => a - b));
      }
      return;
    }

    for (let i = start; i <= 9; i++) {
      if (i > target) break;
      current.push(i);
      findCombinations(target - i, digitsNeeded - 1, i + 1, current, results);
      current.pop();
    }
  }

  const result = [];
  findCombinations(sum, size, 1, [], result);
  
  // Sort the results
  const sortedResults = result.sort((a, b) => {
    for (let i = 0; i < a.length; i++) {
      if (a[i] !== b[i]) return a[i] - b[i];
    }
    return 0;
  });
  
  return sortedResults;
};
