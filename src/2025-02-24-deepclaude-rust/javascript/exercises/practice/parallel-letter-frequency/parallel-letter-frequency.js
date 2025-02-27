//
// This is only a SKELETON file for the 'Parallel Letter Frequency' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const parallelLetterFrequency = async (texts) => {
  const promises = texts.map(text => 
    Promise.resolve().then(() => {
      const counts = {};
      for (const c of text.toLowerCase()) {
        if (/\p{L}/u.test(c)) {
          counts[c] = (counts[c] || 0) + 1;
        }
      }
      return counts;
    })
  );

  const results = await Promise.all(promises);

  const total = {};
  for (const result of results) {
    for (const [char, count] of Object.entries(result)) {
      total[char] = (total[char] || 0) + count;
    }
  }

  return total;
};
