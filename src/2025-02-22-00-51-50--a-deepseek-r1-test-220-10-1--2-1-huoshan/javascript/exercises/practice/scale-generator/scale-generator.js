const SHARP_SCALE = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#'];
const FLAT_SCALE = ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab'];
const SHARP_KEYS = ['C', 'G', 'D', 'A', 'E', 'B', 'F#', 'a', 'e', 'b', 'f#', 'c#', 'g#', 'd#'];
const FLAT_KEYS = ['F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb', 'd', 'g', 'c', 'f', 'bb', 'eb'];

export class Scale {
  constructor(tonic) {
    this.tonic = tonic.length > 1 
      ? tonic[0].toUpperCase() + tonic[1]
      : tonic.toUpperCase();
    
    this.useSharps = SHARP_KEYS.includes(this.tonic);
    this.useFlats = FLAT_KEYS.includes(this.tonic);
    
    if (!this.useSharps && !this.useFlats) {
      this.useSharps = !this.tonic.endsWith('b');
    }
    
    const baseScale = this.useSharps ? SHARP_SCALE : FLAT_SCALE;
    const startIndex = baseScale.findIndex(n => n.toUpperCase() === this.tonic.toUpperCase());
    this.chromaticScale = [];
    
    for (let i = 0; i < 12; i++) {
      this.chromaticScale.push(baseScale[(startIndex + i) % 12]);
    }
  }

  chromatic() {
    return this.chromaticScale;
  }

  interval(intervals) {
    const steps = {
      'm': 1,
      'M': 2,
      'A': 3
    };
    
    const scale = [this.chromaticScale[0]];
    let currentIndex = 0;
    
    for (const interval of intervals) {
      currentIndex = (currentIndex + steps[interval]) % 12;
      scale.push(this.chromaticScale[currentIndex]);
    }
    
    return scale;
  }
}
