export class Scale {
  constructor(tonic) {
    this.tonic = this.normalizeTonic(tonic);
    this.baseScaleType = this.determineBaseScale();
    this.baseScale = this.baseScaleType === 'sharps' 
      ? ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#']
      : ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab'];
    this.chromaticScale = this.buildChromaticScale();
  }

  normalizeTonic(tonic) {
    const normalized = tonic[0].toUpperCase() + tonic.slice(1).toLowerCase();
    return normalized.replace(/b/g, 'b');
  }

  determineBaseScale() {
    if (['C', 'A'].includes(this.tonic)) {
      return 'sharps';
    }

    const sharpMajors = ['G', 'D', 'A', 'E', 'B', 'F#'];
    const sharpMinors = ['E', 'B', 'F#', 'C#', 'G#', 'D#'];
    const flatMajors = ['F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb'];
    const flatMinors = ['D', 'G', 'C', 'F', 'Bb', 'Eb'];

    if (flatMinors.includes(this.tonic) || flatMajors.includes(this.tonic)) {
      return 'flats';
    } else if (sharpMajors.includes(this.tonic) || sharpMinors.includes(this.tonic)) {
      return 'sharps';
    } else {
      throw new Error(`Invalid tonic: ${this.tonic}`);
    }
  }

  buildChromaticScale() {
    const index = this.baseScale.indexOf(this.tonic);
    if (index === -1) {
      throw new Error(`Tonic ${this.tonic} not found in ${this.baseScaleType} scale`);
    }
    return [...this.baseScale.slice(index), ...this.baseScale.slice(0, index)];
  }

  chromatic() {
    return this.chromaticScale;
  }

  interval(intervals) {
    let currentIndex = 0;
    const scale = [this.chromaticScale[0]];
    
    for (const interval of intervals) {
      const step = this.getStep(interval);
      currentIndex = (currentIndex + step) % 12;
      scale.push(this.chromaticScale[currentIndex]);
    }
    
    return scale;
  }

  getStep(interval) {
    switch(interval) {
      case 'm': return 1;
      case 'M': return 2;
      case 'A': return 3;
      default: throw new Error(`Invalid interval: ${interval}`);
    }
  }
}
