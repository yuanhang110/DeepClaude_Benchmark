//
// This is only a SKELETON file for the 'Sum Of Multiples' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const sum = (level, multiples = []) => {
  if (level === 0) return 0;
  const multiplesSet = new Set();
  
  for (const base of multiples) {
    if (base === 0) {
      if (level > 0) {
        multiplesSet.add(0);
      }
      continue;
    }
    
    const absBase = Math.abs(base);
    for (let i = absBase; i < level; i += absBase) {
      multiplesSet.add(i);
    }
  }
  
  return [...multiplesSet].reduce((acc, val) => acc + val, 0);
};
