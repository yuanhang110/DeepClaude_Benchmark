const numberToWords = (num) => {
  const words = [
    'Zero', 'One', 'Two', 'Three', 'Four', 
    'Five', 'Six', 'Seven', 'Eight', 'Nine', 'Ten'
  ];
  return words[num];
};

const getVerse = (bottles) => {
  if (bottles === 0) {
    return ['No green bottles hanging on the wall.'];
  }
  
  const bottleText = bottles === 1 ? 'bottle' : 'bottles';
  const nextBottles = bottles - 1;
  const nextBottleText = nextBottles === 1 ? 'bottle' : 'bottles';
  
  const currentWord = numberToWords(bottles);
  const nextWord = numberToWords(nextBottles);
  
  return [
    `${currentWord} green ${bottleText} hanging on the wall,`,
    `${currentWord} green ${bottleText} hanging on the wall,`,
    `And if one green bottle should accidentally fall,`,
    nextBottles > 0 
      ? `There'll be ${nextWord.toLowerCase()} green ${nextBottleText} hanging on the wall.`
      : `There'll be no green bottles hanging on the wall.`
  ];
};

export const recite = (initialBottlesCount, takeDownCount) => {
  let result = [];
  let currentBottles = initialBottlesCount;
  
  for (let i = 0; i < takeDownCount; i++) {
    result.push(...getVerse(currentBottles));
    currentBottles--;
    
    // Add an empty line between verses except the last one
    if (i < takeDownCount - 1) {
      result.push('');
    }
  }
  
  return result;
};
