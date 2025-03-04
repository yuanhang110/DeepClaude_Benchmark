//
// This is only a SKELETON file for the 'Beer Song' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const recite = (initialBottlesCount, takeDownCount) => {
  const verses = [];
  let current = initialBottlesCount;
  for (let i = 0; i < takeDownCount; i++) {
    if (i > 0) {
      verses.push('');
    }
    const [line1, line2] = generateVerse(current);
    verses.push(line1, line2);
    current = current === 0 ? 99 : current - 1;
  }
  return verses;
};

function generateVerse(current) {
  let line1, line2;
  if (current === 0) {
    line1 = 'No more bottles of beer on the wall, no more bottles of beer.';
    line2 = 'Go to the store and buy some more, 99 bottles of beer on the wall.';
  } else {
    const bottles = current === 1 ? '1 bottle' : `${current} bottles`;
    line1 = `${bottles} of beer on the wall, ${bottles} of beer.`;
    const next = current - 1;
    if (next === 0) {
      line2 = 'Take it down and pass it around, no more bottles of beer on the wall.';
    } else {
      const nextBottles = next === 1 ? '1 bottle' : `${next === 0 ? 'no more' : next} bottles`;
      const action = current === 1 ? 'it' : 'one';
      line2 = `Take ${action} down and pass it around, ${nextBottles} of beer on the wall.`;
    }
  }
  return [line1, line2];
}
