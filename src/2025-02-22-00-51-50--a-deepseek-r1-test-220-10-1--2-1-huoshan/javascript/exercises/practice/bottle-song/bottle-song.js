//
// This is only a SKELETON file for the 'Bottle Song' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const recite = (initialBottlesCount, takeDownCount) => {
  const numberWords = ['', 'One', 'Two', 'Three', 'Four', 'Five', 'Six', 'Seven', 'Eight', 'Nine', 'Ten'];
  const verses = [];
  
  for (let i = 0; i < takeDownCount; i++) {
    const current = initialBottlesCount - i;
    const currentWord = numberWords[current];
    const currentBottle = current === 1 ? 'bottle' : 'bottles';
    
    const nextNumber = current - 1;
    const nextWord = nextNumber === 0 ? 'no' : numberWords[nextNumber].toLowerCase();
    const nextBottle = nextNumber === 1 ? 'bottle' : 'bottles';
    
    verses.push(
      `${currentWord} green ${currentBottle} hanging on the wall,`,
      `${currentWord} green ${currentBottle} hanging on the wall,`,
      `And if one green bottle should accidentally fall,`,
      `There'll be ${nextWord} green ${nextBottle} hanging on the wall.`
    );
    
    // Add empty string between verses except after last one
    if (i < takeDownCount - 1) {
      verses.push('');
    }
  }
  
  return verses;
};
