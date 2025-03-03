//
// This is only a SKELETON file for the 'Beer Song' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const recite = (initialBottlesCount, takeDownCount) => {
  const generateVerse = (current) => {
    // First line of verse - about current bottles
    let currentLine;
    if (current === 0) {
      currentLine = 'No more bottles of beer on the wall, no more bottles of beer.';
    } else {
      const plural = current !== 1 ? 's' : '';
      currentLine = `${current} bottle${plural} of beer on the wall, ${current} bottle${plural} of beer.`;
    }

    // Second line of verse - about taking one down and next state
    let nextLine;
    if (current === 0) {
      nextLine = 'Go to the store and buy some more, 99 bottles of beer on the wall.';
    } else {
      const action = current === 1 ? 'it' : 'one';
      const next = (current - 1 + 100) % 100;
      let nextBottles;
      let nextPlural;
      if (next === 0) {
        nextBottles = 'no more';
        nextPlural = 'bottles';
      } else {
        nextBottles = next;
        nextPlural = next !== 1 ? 'bottles' : 'bottle';
      }
      nextLine = `Take ${action} down and pass it around, ${nextBottles} ${nextPlural} of beer on the wall.`;
    }

    // Return as an array of two lines instead of a joined string
    return [currentLine, nextLine];
  };

  // Generate all requested verses
  const result = [];
  for (let i = 0; i < takeDownCount; i++) {
    const current = (initialBottlesCount - i + 100) % 100;
    const verseLines = generateVerse(current);
    
    // Add each line of the verse to the result array
    result.push(...verseLines);
    
    // Add an empty string between verses (but not after the last verse)
    if (i < takeDownCount - 1) {
      result.push('');
    }
  }
  
  return result;
};
