//
// This is only a SKELETON file for the 'House' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class House {
  static versesData = [
    { subject: 'house that Jack built.', action: null },
    { subject: 'malt', action: 'lay in' },
    { subject: 'rat', action: 'ate' },
    { subject: 'cat', action: 'killed' },
    { subject: 'dog', action: 'worried' },
    { subject: 'cow with the crumpled horn', action: 'tossed' },
    { subject: 'maiden all forlorn', action: 'milked' },
    { subject: 'man all tattered and torn', action: 'kissed' },
    { subject: 'priest all shaven and shorn', action: 'married' },
    { subject: 'rooster that crowed in the morn', action: 'woke' },
    { subject: 'farmer sowing his corn', action: 'kept' },
    { subject: 'horse and the hound and the horn', action: 'belonged to' },
  ];

  static verse(n) {
    const index = n - 1;
    if (index < 0 || index >= this.versesData.length) {
      throw new Error('Invalid verse');
    }
    const lines = [];
    lines.push(`This is the ${this.versesData[index].subject}`);
    for (let i = index; i > 0; i--) {
      const action = this.versesData[i].action;
      const prevSubject = this.versesData[i - 1].subject;
      lines.push(`that ${action} the ${prevSubject}`);
    }
    return lines;
  }

  static verses(start, end) {
    const result = [];
    for (let i = start; i <= end; i++) {
      const verseLines = this.verse(i);
      result.push(...verseLines);
      if (i !== end) {
        result.push('');
      }
    }
    return result;
  }
}
