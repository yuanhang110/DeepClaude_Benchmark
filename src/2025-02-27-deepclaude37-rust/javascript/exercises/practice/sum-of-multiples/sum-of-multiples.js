//
// This is only a SKELETON file for the 'Sum Of Multiples' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const sum = (level, numbers = []) => {
  const multiples = new Set();
  
  for (const num of numbers) {
    // Skip 0 to avoid infinite loop
    if (num === 0) continue;
    
    // Find all multiples of this base value less than level
    for (let multiple = num; multiple < level; multiple += num) {
      multiples.add(multiple);
    }
  }
  
  // Sum all unique multiples
  return Array.from(multiples).reduce((sum, value) => sum + value, 0);
};
