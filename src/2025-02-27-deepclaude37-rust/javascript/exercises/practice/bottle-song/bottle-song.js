//
// This is only a SKELETON file for the 'Bottle Song' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const recite = (initialBottlesCount, takeDownCount) => {
  const verses = [];
  const numberWords = {
    10: 'Ten',
    9: 'Nine',
    8: 'Eight',
    7: 'Seven',
    6: 'Six',
    5: 'Five',
    4: 'Four',
    3: 'Three',
    2: 'Two',
    1: 'One'
  };
  for (let i = 0; i < takeDownCount; i++) {
    const current = initialBottlesCount - i;
    let currentBottleText, nextBottleText;
    
    // Format current bottle count
    if (current === 1) {
      currentBottleText = 'One green bottle';
    } else {
      currentBottleText = `${numberWords[current]} green bottles`;
    }
    
    // Format next bottle count
    const nextCount = current - 1;
    if (nextCount === 0) {
      nextBottleText = 'no green bottles';
    } else if (nextCount === 1) {
      nextBottleText = 'one green bottle';
    } else {
      nextBottleText = `${numberWords[nextCount].toLowerCase()} green bottles`;
    }
    
    // Construct verse
    const line1 = `${currentBottleText} hanging on the wall,`;
    const line2 = line1;
    const line3 = 'And if one green bottle should accidentally fall,';
    const line4 = `There'll be ${nextBottleText} hanging on the wall.`;
    
    verses.push([line1, line2, line3, line4].join('\n'));
  }
  
  return verses.join('\n\n');
};
