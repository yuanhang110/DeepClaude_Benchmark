export const sum = (level, ...numbers) => {
  if (level <= 0) {
    return 0;
  }

  const multiples = new Set();
  
  for (const num of numbers) {
    if (num === 0) {
      if (level > 0) {
        multiples.add(0);
      }
      continue;
    }

    let current = num;
    while (current < level) {
      multiples.add(current);
      current += num;
    }
  }
  
  return Array.from(multiples).reduce((acc, val) => acc + val, 0);
};
