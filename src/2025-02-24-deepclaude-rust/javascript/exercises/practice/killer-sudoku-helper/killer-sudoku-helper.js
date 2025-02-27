//
// This is only a SKELETON file for the 'Killer Sudoku Helper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const combinations = (cage) => {
  const sum = Number(cage.sum);
  const k = cage.length;
  const result = [];
  
  const generate = (start, current, currentSum) => {
    if (current.length === k) {
      if (currentSum === sum) {
        result.push([...current]);
      }
      return;
    }
    for (let i = start; i <= 9; i++) {
      const nextSum = currentSum + i;
      if (nextSum > sum) break;
      current.push(i);
      generate(i + 1, current, nextSum);
      current.pop();
    }
  };
  
  generate(1, [], 0);
  
  return result;
};
