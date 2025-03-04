export class Song {
  constructor() {
    this.animals = [
      { name: 'fly', comment: "I don't know why she swallowed the fly. Perhaps she'll die." },
      { name: 'spider', action: 'wriggled and jiggled and tickled inside her', comment: 'She swallowed the spider to catch the fly.' },
      { name: 'bird', exclamation: 'How absurd to swallow a bird!', comment: 'She swallowed the bird to catch the spider that wriggled and jiggled and tickled inside her.' },
      { name: 'cat', exclamation: 'Imagine that, to swallow a cat!', comment: 'She swallowed the cat to catch the bird.' },
      { name: 'dog', exclamation: 'What a hog, to swallow a dog!', comment: 'She swallowed the dog to catch the cat.' },
      { name: 'goat', exclamation: 'Just opened her throat and swallowed a goat!', comment: 'She swallowed the goat to catch the dog.' },
      { name: 'cow', exclamation: "I don't know how she swallowed a cow!", comment: 'She swallowed the cow to catch the goat.' },
      { name: 'horse', exclamation: "She's dead, of course!" }
    ];
  }

  verse(n) {
    const animal = this.animals[n - 1];
    let lines = [`I know an old lady who swallowed a ${animal.name}.`];
    
    if (animal.exclamation) {
      lines.push(animal.exclamation);
    }
    
    if (animal.action) {
      lines.push(`It ${animal.action}.`);
    }
    
    if (n === 8) {
      return lines.join('\n') + '\n';
    }
    
    for (let i = n - 1; i >= 0; i--) {
      if (this.animals[i].comment) {
        lines.push(this.animals[i].comment);
      }
    }
    
    return lines.join('\n') + '\n';
  }

  verses(start, end) {
    let result = [];
    for (let i = start; i <= end; i++) {
      result.push(this.verse(i));
    }
    return result.join('\n') + '\n';
  }
}
