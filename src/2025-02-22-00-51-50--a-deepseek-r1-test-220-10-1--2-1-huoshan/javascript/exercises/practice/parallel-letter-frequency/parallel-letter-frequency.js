//
// This is only a SKELETON file for the 'Parallel Letter Frequency' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

const countLetters = (text) => {
  const counts = {};
  for (const char of text.toLowerCase()) {
    if (/^\p{L}$/iu.test(char)) {
      counts[char] = (counts[char] || 0) + 1;
    }
  }
  return counts;
};

const mergeCounts = (results) => {
  const total = {};
  for (const counts of results) {
    for (const [char, count] of Object.entries(counts)) {
      total[char] = (total[char] || 0) + count;
    }
  }
  return total;
};

export const parallelLetterFrequency = async (texts) => {
  // Process each text as a separate parallel task
  const countPromises = texts.map(text => 
    new Promise(resolve => resolve(countLetters(text)))
  );
  
  // Wait for all parallel counts to complete
  const results = await Promise.all(countPromises);
  
  // Merge all individual results into final count
  return mergeCounts(results);
};
