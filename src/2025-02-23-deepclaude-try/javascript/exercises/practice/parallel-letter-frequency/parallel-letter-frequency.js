export const parallelLetterFrequency = async (texts) => {
  // Create a promise for processing each text concurrently
  const promises = texts.map(text => 
    new Promise(resolve => {
      const freq = {};
      // Count letter frequencies in this text
      for (const char of text.toLowerCase()) {
        if (/\p{L}/u.test(char)) {
          freq[char] = (freq[char] || 0) + 1;
        }
      }
      resolve(freq);
    })
  );
  
  // Wait for all texts to be processed
  const results = await Promise.all(promises);
  
  // Combine all frequency counts into a single result
  return results.reduce((total, freq) => {
    for (const [char, count] of Object.entries(freq)) {
      total[char] = (total[char] || 0) + count;
    }
    return total;
  }, {});
};
