//
// This is only a SKELETON file for the 'House' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class House {
  static PARTS = [
    'house that Jack built.',
    'malt\nthat lay in',
    'rat\nthat ate',
    'cat\nthat killed',
    'dog\nthat worried',
    'cow with the crumpled horn\nthat tossed',
    'maiden all forlorn\nthat milked',
    'man all tattered and torn\nthat kissed',
    'priest all shaven and shorn\nthat married',
    'rooster that crowed in the morn\nthat woke',
    'farmer sowing his corn\nthat kept',
    'horse and the hound and the horn\nthat belonged to'
  ];

  static verse(verseNumber) {
    const parts = House.PARTS.slice(0, verseNumber).reverse();
    const combined = parts.join(' the ');
    const lines = combined.split('\n');
    lines[0] = `This is the ${lines[0]}`;
    for (let i = 1; i < lines.length; i++) {
      lines[i] = lines[i];
    }
    return lines;
  }

  static verses(start, end) {
    const result = [];
    for (let i = start; i <= end; i++) {
      result.push(...House.verse(i));
      if (i < end) result.push('');
    }
    return result;
  }
}
