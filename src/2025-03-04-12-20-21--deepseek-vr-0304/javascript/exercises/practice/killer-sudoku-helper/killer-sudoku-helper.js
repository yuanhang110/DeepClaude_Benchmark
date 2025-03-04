//
// This is only a SKELETON file for the 'Killer Sudoku Helper' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const combinations = (cage) => {
  const { sum, count } = cage;
  const result = [];

  const backtrack = (start, currentCombo, currentSum) => {
    if (currentCombo.length === count) {
      if (currentSum === sum) {
        result.push([...currentCombo]);
      }
      return;
    }

    for (let i = start; i <= 9; i++) {
      const newSum = currentSum + i;
      if (newSum > sum) {
        break;
      }
      currentCombo.push(i);
      backtrack(i + 1, currentCombo, newSum);
      currentCombo.pop();
    }
  };

  backtrack(1, [], 0);

  return result;
};
