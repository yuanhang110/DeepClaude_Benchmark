//
// This is only a SKELETON file for the 'Bottle Song' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const recite = (initialBottlesCount, takeDownCount) => {
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
    1: 'One',
    0: 'No'
  };

  const verses = [];
  for (let i = 0; i < takeDownCount; i++) {
    const current = initialBottlesCount - i;
    if (current < 1) break;

    const currentWord = numberWords[current];
    const currentBottle = current === 1 ? 'bottle' : 'bottles';
    const nextBottles = current - 1;
    const nextWord = nextBottles === 0 ? 'no' : numberWords[nextBottles].toLowerCase();
    const nextBottle = nextBottles === 1 ? 'bottle' : 'bottles';

    const verse = [
      `${currentWord} green ${currentBottle} hanging on the wall,`,
      `${currentWord} green ${currentBottle} hanging on the wall,`,
      `And if one green bottle should accidentally fall,`,
      `There'll be ${nextWord} green ${nextBottle} hanging on the wall.`
    ].join('\n');

    verses.push(verse);
  }

  return verses.join('\n\n');
};
