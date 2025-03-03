//
// This is only a SKELETON file for the 'Food Chain' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Song {
  static versesData = [
    { animal: 'fly', exclamation: null },
    { animal: 'spider', exclamation: 'It wriggled and jiggled and tickled inside her.' },
    { animal: 'bird', exclamation: 'How absurd to swallow a bird!' },
    { animal: 'cat', exclamation: 'Imagine that, to swallow a cat!' },
    { animal: 'dog', exclamation: 'What a hog, to swallow a dog!' },
    { animal: 'goat', exclamation: 'Just opened her throat and swallowed a goat!' },
    { animal: 'cow', exclamation: 'I don\'t know how she swallowed a cow!' },
  ];

  verse(number) {
    if (number === 8) {
      return "I know an old lady who swallowed a horse.\nShe's dead, of course!\n";
    }

    const data = Song.versesData[number - 1];
    let lines = [];
    lines.push(`I know an old lady who swallowed a ${data.animal}.`);
    if (data.exclamation) {
      lines.push(data.exclamation);
    }

    for (let i = number - 1; i > 0; i--) {
      const currentAnimal = Song.versesData[i].animal;
      const previousAnimal = Song.versesData[i - 1].animal;
      let line = `She swallowed the ${currentAnimal} to catch the ${previousAnimal}`;
      if (previousAnimal === 'spider') {
        line += ' that wriggled and jiggled and tickled inside her';
      }
      line += '.';
      lines.push(line);
    }

    lines.push("I don't know why she swallowed the fly. Perhaps she'll die.");
    return lines.join('\n');
  }

  verses(start, end) {
    const result = [];
    for (let i = start; i <= end; i++) {
      result.push(this.verse(i));
    }
    return result.join('\n');
  }
}
