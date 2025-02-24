export class Song {
  constructor() {
    this.animals = [
      {
        name: 'fly',
        intro: 'I know an old lady who swallowed a fly.\n',
        followUp: 'I don\'t know why she swallowed the fly. Perhaps she\'ll die.\n'
      },
      {
        name: 'spider',
        intro: 'I know an old lady who swallowed a spider.\nIt wriggled and jiggled and tickled inside her.\n',
        followUp: 'She swallowed the spider to catch the fly',
        special: ' that wriggled and jiggled and tickled inside her'
      },
      {
        name: 'bird',
        intro: 'I know an old lady who swallowed a bird.\nHow absurd to swallow a bird!\n',
        followUp: 'She swallowed the bird to catch the spider',
      },
      {
        name: 'cat',
        intro: 'I know an old lady who swallowed a cat.\nImagine that, to swallow a cat!\n',
        followUp: 'She swallowed the cat to catch the bird',
      },
      {
        name: 'dog',
        intro: 'I know an old lady who swallowed a dog.\nWhat a hog, to swallow a dog!\n',
        followUp: 'She swallowed the dog to catch the cat',
      },
      {
        name: 'goat',
        intro: 'I know an old lady who swallowed a goat.\nJust opened her throat and swallowed a goat!\n',
        followUp: 'She swallowed the goat to catch the dog',
      },
      {
        name: 'cow',
        intro: 'I know an old lady who swallowed a cow.\nI don\'t know how she swallowed a cow!\n',
        followUp: 'She swallowed the cow to catch the goat',
      },
      {
        name: 'horse',
        intro: 'I know an old lady who swallowed a horse.\nShe\'s dead, of course!\n',
        isEnd: true
      }
    ];
  }

  verse(number) {
    const index = number - 1;
    const animal = this.animals[index];
    let lines = animal.intro;
    
    if (animal.isEnd) {
      return lines;
    }
    
    lines += animal.followUp;
    
    // Build the chain of swallowing reasons
    // Build the chain of swallowing reasons
    for (let i = index - 1; i >= 0; i--) {
      const prev = this.animals[i];
      if (i === 0) { // Fly
        lines += prev.followUp;
        break;
      }
      lines += `${prev.followUp}${prev.special || ''}.\n`;
    }
    
    return lines;
  }

  verses(start, end) {
    let result = '';
    for (let i = start; i <= end; i++) {
      result += this.verse(i) + '\n';
    }
    return result;
  }
}
