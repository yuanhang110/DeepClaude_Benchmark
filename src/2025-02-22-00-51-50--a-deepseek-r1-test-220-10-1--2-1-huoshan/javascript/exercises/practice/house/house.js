export class House {
  static verses = [
    ['house', 'Jack built'],
    ['malt', 'lay in'],
    ['rat', 'ate'],
    ['cat', 'killed'],
    ['dog', 'worried'],
    ['cow with the crumpled horn', 'tossed'],
    ['maiden all forlorn', 'milked'],
    ['man all tattered and torn', 'kissed'],
    ['priest all shaven and shorn', 'married'],
    ['rooster that crowed in the morn', 'woke'],
    ['farmer sowing his corn', 'kept'],
    ['horse and the hound and the horn', 'belonged to']
  ];

  static prefixes = [
    '',
    'that ',
    'that ',
    'that ',
    'that ',
    'that ',
    'that ',
    'that ',
    'that ',
    'that ',
    'that ',
    'that '
  ];

  static verse(n) {
    const lines = [];
    lines.push(`This is the ${this.verses[n - 1][0]}`);
    
    for (let i = n - 1; i > 0; i--) {
      lines.push(`${this.prefixes[i]}${this.verses[i][1]} the ${this.verses[i - 1][0]}`);
    }
    
    lines[lines.length - 1] += ' that Jack built.';
    return lines;
  }

  static verses(start, end) {
    const result = [];
    for (let i = start; i <= end; i++) {
      result.push(...this.verse(i));
      if (i !== end) result.push('');
    }
    return result;
  }
}
