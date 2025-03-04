//
// This is only a SKELETON file for the 'House' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class House {
  static versesData = [
    ['house that Jack built.', ''],
    ['malt', 'lay in the'],
    ['rat', 'ate the'],
    ['cat', 'killed the'],
    ['dog', 'worried the'],
    ['cow with the crumpled horn', 'tossed the'],
    ['maiden all forlorn', 'milked the'],
    ['man all tattered and torn', 'kissed the'],
    ['priest all shaven and shorn', 'married the'],
    ['rooster that crowed in the morn', 'woke the'],
    ['farmer sowing his corn', 'kept the'],
    ['horse and the hound and the horn', 'belonged to the']
  ];

  static verse(n) {
    const lines = [];
    lines.push(`This is the ${this.versesData[n-1][0]}`);
    for (let i = n - 2; i >= 0; i--) {
      lines.push(`that ${this.versesData[i+1][1]} ${this.versesData[i][0]}`);
    }
    return lines;
  }

  static verses(start, end) {
    const result = [];
    for (let i = start; i <= end; i++) {
      result.push(this.verse(i).join('\n'));
    }
    return result.join('\n\n') + '\n';
  }
}
