export const sum = (level, ...baseValues) => {
  if (!level || baseValues.length === 0) return 0;
  
  // Create a Set to store unique multiples
  const multiples = new Set();
  
  // For each base value, find all multiples less than level
  baseValues.forEach(base => {
    if (base === 0) return;
    for (let i = base; i < level; i += base) {
      multiples.add(i);
    }
  });
  
  // Sum all unique multiples
  return Array.from(multiples).reduce((sum, num) => sum + num, 0);
};
