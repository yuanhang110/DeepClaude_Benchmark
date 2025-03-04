//
// This is only a SKELETON file for the 'Sum Of Multiples' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const sum = (level, ...numbers) => {
  const multiples = new Set();
  for (const num of numbers) {
    if (num === 0) continue;
    const maxK = Math.floor((level - 1) / num);
    for (let k = 1; k <= maxK; k++) {
      multiples.add(k * num);
    }
  }
  return [...multiples].reduce((acc, val) => acc + val, 0);
};
