//
// This is only a SKELETON file for the 'Beer Song' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const recite = (initialBottlesCount, takeDownCount) => {
  const verses = [];
  for (let i = 0; i < takeDownCount; i++) {
    const currentBottles = (initialBottlesCount - i + 100) % 100;
    const line1 = currentBottles === 0 
      ? "No more bottles of beer on the wall, no more bottles of beer."
      : `${currentBottles} ${currentBottles === 1 ? 'bottle' : 'bottles'} of beer on the wall, ${currentBottles} ${currentBottles === 1 ? 'bottle' : 'bottles'} of beer.`;

    let line2;
    if (currentBottles === 0) {
      line2 = "Go to the store and buy some more, 99 bottles of beer on the wall.";
    } else {
      const nextBottles = currentBottles - 1;
      const nextBottlesText = nextBottles === 0 
        ? 'no more bottles'
        : `${nextBottles} ${nextBottles === 1 ? 'bottle' : 'bottles'}`;
      line2 = `Take ${currentBottles === 1 ? 'it' : 'one'} down and pass it around, ${nextBottlesText} of beer on the wall.`;
    }

    verses.push(line1);
    verses.push(line2);
    if (i < takeDownCount - 1) {
      verses.push('');
    }
  }
  return verses;
};
