export const recite = (initialBottlesCount, takeDownCount) => {
  const verses = [];
  
  const generateVerse = (current, next) => {
    const line1 = current === 0 
      ? `No more bottles of beer on the wall, no more bottles of beer.`
      : `${current} bottle${current !== 1 ? 's' : ''} of beer on the wall, ${current} bottle${current !== 1 ? 's' : ''} of beer.`;
    
    const line2 = current === 0
      ? `Go to the store and buy some more, 99 bottles of beer on the wall.`
      : `Take ${current === 1 ? 'it' : 'one'} down and pass it around, ${next === 0 ? 'no more' : next} bottle${next !== 1 ? 's' : ''} of beer on the wall.`;

    return [line1, line2];
  };

  for (let i = 0; i < takeDownCount; i++) {
    const current = initialBottlesCount - i;
    const next = current - 1;
    
    let verseLines = generateVerse(current, next);
    
    if (current === 0) {
      verseLines = generateVerse(0, 99);
    }

    verses.push(...verseLines);
    
    // Add empty line between verses unless it's the last one
    if (i !== takeDownCount - 1) {
      verses.push('');
    }
  }

  return verses;
};
