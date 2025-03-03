//
// This is only a SKELETON file for the 'Scale Generator' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Scale {
  constructor(tonic) {
    this.tonic = this.normalizeTonic(tonic);
    this.keySignature = this.determineKeySignature(this.tonic);
    this.sharpSequence = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#'];
    this.flatSequence = ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab'];
    this.sequence = this.keySignature === 'sharps' ? this.sharpSequence : this.flatSequence;
    this.startIndex = this.sequence.indexOf(this.tonic);
    if (this.startIndex === -1) {
      throw new Error('Invalid tonic');
    }
  }

  normalizeTonic(tonic) {
    const notePart = tonic[0]; // Preserve case to determine major/minor
    const accidental = tonic.slice(1).toLowerCase();
    return notePart + accidental;
  }

  determineKeySignature(normalizedTonic) {
    // Natural keys
    if (normalizedTonic === 'C' || normalizedTonic === 'a') {
      return 'sharps';
    }
    
    // Check if major (uppercase) or minor (lowercase)
    const isMajor = normalizedTonic[0] === normalizedTonic[0].toUpperCase();
    
    if (isMajor) {
      // Major keys
      const majorSharps = new Set(['G', 'D', 'A', 'E', 'B', 'F#']);
      if (majorSharps.has(normalizedTonic)) {
        return 'sharps';
      }
      
      const majorFlats = new Set(['F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb']);
      if (majorFlats.has(normalizedTonic)) {
        return 'flats';
      }
    } else {
      // Minor keys
      const minorSharps = new Set(['e', 'b', 'f#', 'c#', 'g#', 'd#']);
      if (minorSharps.has(normalizedTonic)) {
        return 'sharps';
      }
      
      const minorFlats = new Set(['d', 'g', 'c', 'f', 'bb', 'eb']);
      if (minorFlats.has(normalizedTonic)) {
        return 'flats';
      }
    }
    
    throw new Error('Invalid tonic');
  }

  chromatic() {
    const scale = [];
    for (let i = 0; i < 12; i++) {
      const index = (this.startIndex + i) % 12;
      // Ensure all notes are uppercase (except 'b' in flats)
      const note = this.sequence[index];
      scale.push(note[0].toUpperCase() + note.slice(1));
    }
    return scale;
  }

  interval(intervals) {
    let currentIndex = this.startIndex;
    const scale = [this.sequence[currentIndex]];
    for (const interval of intervals) {
      let steps;
      switch (interval) {
        case 'm':
          steps = 1;
          break;
        case 'M':
          steps = 2;
          break;
        case 'A':
          steps = 3;
          break;
        default:
          throw new Error(`Invalid interval: ${interval}`);
      }
      currentIndex = (currentIndex + steps) % 12;
      scale.push(this.sequence[currentIndex]);
    }
    return scale;
  }
}
