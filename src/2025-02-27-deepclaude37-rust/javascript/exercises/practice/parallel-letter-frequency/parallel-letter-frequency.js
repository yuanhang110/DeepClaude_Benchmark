//
// This is only a SKELETON file for the 'Parallel Letter Frequency' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const parallelLetterFrequency = async (texts) => {
  // Helper function to count letters in a single text
  const countLetters = (text) => {
    const frequency = {};
    for (const char of text.toLowerCase()) {
      if (/\p{L}/u.test(char)) {
        frequency[char] = (frequency[char] || 0) + 1;
      }
    }
    return frequency;
  };

  // Process each text concurrently
  const promises = texts.map(text => 
    Promise.resolve().then(() => countLetters(text))
  );
  
  // Wait for all texts to be processed
  const results = await Promise.all(promises);
  
  // Merge the frequency results
  const totalFrequency = {};
  for (const freq of results) {
    for (const [char, count] of Object.entries(freq)) {
      totalFrequency[char] = (totalFrequency[char] || 0) + count;
    }
  }

  return totalFrequency;
};
