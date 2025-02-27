function getNumberWord(n, capitalize) {
  if (n === 0) return 'no';
  const numberWords = ['Zero', 'One', 'Two', 'Three', 'Four', 'Five', 'Six', 'Seven', 'Eight', 'Nine', 'Ten'];
  let word = numberWords[n];
  return capitalize ? word : word.toLowerCase();
}

function generateVerse(n) {
  const currentWord = getNumberWord(n, true);
  const bottleText = n === 1 ? 'bottle' : 'bottles';
  const firstLine = `${currentWord} green ${bottleText} hanging on the wall,`;
  
  const next = n - 1;
  const nextWord = getNumberWord(next, false);
  const nextBottleText = next === 1 ? 'bottle' : 'bottles';
  const lastLine = `There'll be ${nextWord} green ${nextBottleText} hanging on the wall.`;
  
  return [
    firstLine,
    firstLine,
    'And if one green bottle should accidentally fall,',
    lastLine
  ].join('\n');
}

export const recite = (initialBottlesCount, takeDownCount) => {
  const verses = [];
  for (let i = 0; i < takeDownCount; i++) {
    const current = initialBottlesCount - i;
    if (current < 1) break;
    verses.push(generateVerse(current));
  }
  return verses.join('\n\n');
};
