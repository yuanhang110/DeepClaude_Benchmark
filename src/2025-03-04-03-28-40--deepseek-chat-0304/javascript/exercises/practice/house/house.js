export class House {
  static components = [
    'the house that Jack built.',
    'the malt',
    'the rat',
    'the cat',
    'the dog',
    'the cow with the crumpled horn',
    'the maiden all forlorn',
    'the man all tattered and torn',
    'the priest all shaven and shorn',
    'the rooster that crowed in the morn',
    'the farmer sowing his corn',
    'the horse and the hound and the horn'
  ];

  static actions = [
    'lay in',
    'ate',
    'killed',
    'worried',
    'tossed',
    'milked',
    'kissed',
    'married',
    'woke',
    'kept',
    'belonged to'
  ];

  static verse(n) {
    let lines = [`This is ${this.components[n-1]}`];
    
    for (let i = n-1; i > 0; i--) {
      lines.push(`that ${this.actions[i-1]} ${this.components[i-1]}`);
    }
    
    return lines;
  }

  static verses(start, end) {
    let verses = [];
    for (let i = start; i <= end; i++) {
      verses.push(...this.verse(i));
      if (i < end) verses.push(''); // Add empty line between verses
    }
    return verses;
  }
}
