export const cost = (books) => {
  const BOOK_PRICE = 800;
  const DISCOUNTS = [0, 0, 0.05, 0.10, 0.20, 0.25];
  
  // Count how many of each book we have
  const counts = [0, 0, 0, 0, 0];
  books.forEach(book => counts[book - 1]++);
  
  // Recursive function to find the best grouping
  const calculateCost = (counts) => {
    // Base case: all books are counted
    if (counts.every(c => c === 0)) return 0;
    
    let minCost = Infinity;
    
    // Try different group sizes from 2 to 5
    for (let groupSize = 2; groupSize <= 5; groupSize++) {
      // Make a copy of counts
      const tempCounts = [...counts];
      let unique = 0;
      
      // Try to form a group of current size
      for (let i = 0; i < 5; i++) {
        if (tempCounts[i] > 0) {
          unique++;
          tempCounts[i]--;
        }
        if (unique === groupSize) break;
      }
      
      // If we couldn't form a group of this size, skip
      if (unique < groupSize) continue;
      
      // Calculate cost for this group and recursively for remaining books
      const groupCost = groupSize * BOOK_PRICE * (1 - DISCOUNTS[groupSize]);
      const remainingCost = calculateCost(tempCounts);
      const totalCost = groupCost + remainingCost;
      
      // Keep the minimum cost
      if (totalCost < minCost) {
        minCost = totalCost;
      }
    }
    
    // Also consider the case where we don't form any group
    const noGroupCost = counts.reduce((sum, count) => sum + count * BOOK_PRICE, 0);
    if (noGroupCost < minCost) {
      minCost = noGroupCost;
    }
    
    return minCost;
  };
  
  return Math.round(calculateCost(counts));
};
