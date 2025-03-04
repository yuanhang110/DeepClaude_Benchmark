// Helper function to count letters in a single text
const countLetters = (text) => {
  const frequency = {};
  for (const char of text.toLowerCase()) {
    if (/^\p{L}$/u.test(char)) {
      frequency[char] = (frequency[char] || 0) + 1;
    }
  }
  return frequency;
};

// Merge multiple frequency maps into one
const mergeFrequencies = (frequencyMaps) => {
  const result = {};
  for (const freq of frequencyMaps) {
    for (const [char, count] of Object.entries(freq)) {
      result[char] = (result[char] || 0) + count;
    }
  }
  return result;
};

export const parallelLetterFrequency = async (texts) => {
  // Process each text concurrently
  const promises = texts.map(text => 
    Promise.resolve().then(() => countLetters(text))
  );
  
  // Wait for all counts to complete
  const frequencyMaps = await Promise.all(promises);
  
  // Merge all frequency maps
  return mergeFrequencies(frequencyMaps);
};
