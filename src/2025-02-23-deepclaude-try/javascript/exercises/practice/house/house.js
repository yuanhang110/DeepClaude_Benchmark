//
// This is only a SKELETON file for the 'House' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class House {
  static parts = [
    { subject: 'the house that Jack built.', action: '' },
    { subject: 'the malt', action: 'lay in ' },
    { subject: 'the rat', action: 'ate ' },
    { subject: 'the cat', action: 'killed ' },
    { subject: 'the dog', action: 'worried ' },
    { subject: 'the cow with the crumpled horn', action: 'tossed ' },
    { subject: 'the maiden all forlorn', action: 'milked ' },
    { subject: 'the man all tattered and torn', action: 'kissed ' },
    { subject: 'the priest all shaven and shorn', action: 'married ' },
    { subject: 'the rooster that crowed in the morn', action: 'woke ' },
    { subject: 'the farmer sowing his corn', action: 'kept ' },
    { subject: 'the horse and the hound and the horn', action: 'belonged to ' }
  ];

  static verse(n) {
    const reversedParts = House.parts.slice(0, n).reverse();
    let lines = [`This is ${reversedParts[0].subject}`];
    for (let i = 0; i < reversedParts.length - 1; i++) {
      lines.push(`that ${reversedParts[i].action}${reversedParts[i + 1].subject}`);
    }
    return lines;
  }

  static verses(start, end) {
    let result = [];
    for (let i = start; i <= end; i++) {
      const verseLines = House.verse(i);
      result.push(...verseLines);
      if (i !== end) {
        result.push('');
      }
    }
    return result;
  }
}
