//
// This is only a SKELETON file for the 'Food Chain' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Song {
  constructor() {
    this.animals = [
      { name: 'fly', comment: null },
      { name: 'spider', comment: 'It wriggled and jiggled and tickled inside her.' },
      { name: 'bird', comment: 'How absurd to swallow a bird!' },
      { name: 'cat', comment: 'Imagine that, to swallow a cat!' },
      { name: 'dog', comment: 'What a hog, to swallow a dog!' },
      { name: 'goat', comment: 'Just opened her throat and swallowed a goat!' },
      { name: 'cow', comment: "I don't know how she swallowed a cow!" },
      { name: 'horse', comment: "She's dead, of course!" }
    ];
  }

  verse(number) {
    const animal = this.animals[number - 1];
    let lines = [`I know an old lady who swallowed a ${animal.name}.`];

    if (animal.comment) {
      lines.push(animal.comment);
    }

    // Special case for horse
    if (animal.name === 'horse') {
      return lines.join('\n') + '\n';
    }

    // Build the chain of animals
    for (let i = number - 1; i > 0; i--) {
      const currentAnimal = this.animals[i];
      const previousAnimal = this.animals[i - 1];
      
      let line = `She swallowed the ${currentAnimal.name} to catch the ${previousAnimal.name}`;
      
      // Add the spider's special line when referring to it
      if (previousAnimal.name === 'spider') {
        line += ' that wriggled and jiggled and tickled inside her';
      }
      
      lines.push(line + '.');
    }

    lines.push("I don't know why she swallowed the fly. Perhaps she'll die.");
    
    return lines.join('\n') + '\n';
  }

  verses(start, end) {
    const verses = [];
    for (let i = start; i <= end; i++) {
      verses.push(this.verse(i));
    }
    return verses.join('\n\n');
  }
}
