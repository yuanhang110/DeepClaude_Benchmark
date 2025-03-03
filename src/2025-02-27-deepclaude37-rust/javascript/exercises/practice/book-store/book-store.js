//
// This is only a SKELETON file for the 'BookStore' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const cost = (books) => {
  // Count frequency of each book
  const counts = new Array(5).fill(0);
  for (const book of books) {
    counts[book - 1]++;
  }
  
  // Sort counts in descending order and remove zeros
  let sorted = counts.filter(c => c > 0).sort((a, b) => b - a);
  const memo = new Map();

  const calculate = (sortedCounts) => {
    // Base case: no books left
    if (sortedCounts.length === 0) return 0;
    
    // Check if we've already calculated this state
    const key = sortedCounts.join(',');
    if (memo.has(key)) return memo.get(key);

    let minCost = Infinity;
    // Try all possible group sizes from 1 to number of unique books
    for (let k = 1; k <= sortedCounts.length; k++) {
      const newCounts = [];
      for (let i = 0; i < sortedCounts.length; i++) {
        if (i < k) {
          // Take one book from each of the first k different books
          const newCount = sortedCounts[i] - 1;
          if (newCount > 0) {
            newCounts.push(newCount);
          }
        } else {
          newCounts.push(sortedCounts[i]);
        }
      }
      
      // Sort remaining counts for next recursive call
      const nextSorted = newCounts.sort((a, b) => b - a);
      
      // Calculate discount based on group size
      const discount = [0, 0, 0.05, 0.1, 0.2, 0.25][k];
      
      // Calculate cost for this grouping
      const groupCost = k * 800 * (1 - discount);
      const remainingCost = calculate(nextSorted);
      const totalCost = groupCost + remainingCost;
      
      if (totalCost < minCost) {
        minCost = totalCost;
      }
    }
    
    // Memoize result for this state
    memo.set(key, minCost);
    return minCost;
  };

  return calculate(sorted);
};
