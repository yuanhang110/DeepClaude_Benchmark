CHROMATIC_SHARPS = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#']
CHROMATIC_FLATS = ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab']

SHARP_KEYS = {'G', 'D', 'A', 'E', 'B', 'F#', 'e', 'b', 'f#', 'c#', 'g#', 'd#'}
FLAT_KEYS = {'F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb', 'd', 'g', 'c', 'f', 'bb', 'eb'}

class Scale:
    def __init__(self, tonic):
        self.tonic = tonic.capitalize()
        # Determine if we should use flats based on the tonic
        if self.tonic in FLAT_KEYS or (self.tonic.lower() in {'d', 'g', 'c', 'f', 'bb', 'eb'}):
            self.chromatic_scale = CHROMATIC_FLATS
        else:
            self.chromatic_scale = CHROMATIC_SHARPS

    def chromatic(self):
        # Find the starting index of the tonic
        tonic_index = next(i for i, note in enumerate(self.chromatic_scale) 
                          if note.upper() == self.tonic.upper())
        # Rotate the scale to start with the tonic
        return self.chromatic_scale[tonic_index:] + self.chromatic_scale[:tonic_index]

    def interval(self, intervals):
        chromatic = self.chromatic()
        scale = [chromatic[0]]
        index = 0
        
        for interval in intervals:
            if interval == 'm':
                index += 1
            elif interval == 'M':
                index += 2
            elif interval == 'A':
                index += 3
            # Wrap around using modulo 12
            scale.append(chromatic[index % 12])
        
        return scale
