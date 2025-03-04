export const recite = (initialBottlesCount, takeDownCount) => {
  const numberWords = ['No', 'One', 'Two', 'Three', 'Four', 'Five', 'Six', 'Seven', 'Eight', 'Nine', 'Ten'];

  const numberToWord = (n) => numberWords[n];

  const pluralize = (n) => n === 1 ? 'bottle' : 'bottles';

  const generateVerse = (current) => {
    const nextN = current - 1;
    const currentWord = numberToWord(current);
    const nextWord = numberToWord(nextN);
    const currentBottles = `${currentWord} green ${pluralize(current)}`;
    const nextBottles = nextN === 0 
      ? 'no green bottles' 
      : `${nextWord.toLowerCase()} green ${pluralize(nextN)}`;
    
    const lines = [
      `${currentBottles} hanging on the wall,`,
      `${currentBottles} hanging on the wall,`,
      `And if one green bottle should accidentally fall,`,
      `There'll be ${nextBottles} hanging on the wall.`
    ];
    
    return lines.join('\n');
  };

  const verses = [];
  for (let i = 0; i < takeDownCount; i++) {
    const current = initialBottlesCount - i;
    if (current < 1) break;
    verses.push(generateVerse(current));
  }

  return verses.join('\n\n');
};
