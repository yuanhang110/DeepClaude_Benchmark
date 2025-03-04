export class Scale {
  static SHARPS_SCALE = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#'];
  static FLATS_SCALE = ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab'];
  
  static SHARP_KEYS = ['G', 'D', 'A', 'E', 'B', 'F#', 'e', 'b', 'f#', 'c#', 'g#', 'd#'];
  static FLAT_KEYS = ['F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb', 'd', 'g', 'c', 'f', 'bb', 'eb'];
  
  constructor(tonic) {
    this.tonic = tonic[0].toUpperCase() + tonic.slice(1);
  }

  chromatic() {
    const scale = this.getScale();
    let index = scale.indexOf(this.tonic);
    if (index === -1) {
      // Handle case where tonic is in the other scale
      const altScale = this.getScale(true);
      index = altScale.indexOf(this.tonic);
      if (index === -1) throw new Error('Invalid tonic');
    }
    
    const notes = [];
    for (let i = 0; i < 12; i++) {
      notes.push(scale[(index + i) % 12]);
    }
    return notes;
  }

  interval(intervals) {
    const scale = this.getScale();
    let index = scale.indexOf(this.tonic);
    if (index === -1) {
      // Handle case where tonic is in the other scale
      const altScale = this.getScale(true);
      index = altScale.indexOf(this.tonic);
      if (index === -1) throw new Error('Invalid tonic');
    }
    
    const notes = [scale[index]];
    for (const interval of intervals) {
      switch(interval) {
        case 'm':
          index = (index + 1) % 12;
          break;
        case 'M':
          index = (index + 2) % 12;
          break;
        case 'A':
          index = (index + 3) % 12;
          break;
        default:
          throw new Error('Invalid interval');
      }
      notes.push(scale[index]);
    }
    
    return notes;
  }

  getScale(useAlternate = false) {
    if (useAlternate) {
      return Scale.SHARP_KEYS.includes(this.tonic) ? Scale.FLATS_SCALE : Scale.SHARPS_SCALE;
    }
    if (Scale.SHARP_KEYS.includes(this.tonic)) return Scale.SHARPS_SCALE;
    if (Scale.FLAT_KEYS.includes(this.tonic)) return Scale.FLATS_SCALE;
    // For C and a, use sharps by default (ascending)
    if (this.tonic === 'C' || this.tonic === 'a') return Scale.SHARPS_SCALE;
    // For D and G, use flats when descending
    if (this.tonic === 'D' || this.tonic === 'G') return Scale.FLATS_SCALE;
    return Scale.SHARPS_SCALE;
  }
}
