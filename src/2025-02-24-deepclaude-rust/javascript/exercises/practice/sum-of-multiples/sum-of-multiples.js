//
// This is only a SKELETON file for the 'Sum Of Multiples' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const sum = (level, multiples) => {
  const validMultiples = new Set();
  
  for (const factor of multiples) {
    if (factor === 0) {
      if (level > 0) {
        validMultiples.add(0);
      }
      continue;
    }
    
    for (let current = factor; current < level; current += factor) {
      validMultiples.add(current);
    }
  }
  
  return Array.from(validMultiples).reduce((sum, num) => sum + num, 0);
};
