class Scale:
    def __init__(self, tonic):
        self.tonic = self._normalize_tonic(tonic)
        self.scale_type = self._determine_scale_type()
        self.base_scale = self._create_base_scale()
        self.index = self.base_scale.index(self.tonic)
        self.chromatic_scale = self.base_scale[self.index:] + self.base_scale[:self.index]

    def _normalize_tonic(self, tonic):
        if len(tonic) == 1:
            return tonic[0].upper() + tonic[1:]
        return tonic[0].upper() + tonic[1].lower()

    def _determine_scale_type(self):
        sharp_major = {'C', 'G', 'D', 'A', 'E', 'B', 'F#'}
        flat_major = {'F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb'}
        sharp_minor = {'a', 'e', 'b', 'f#', 'c#', 'g#', 'd#'}
        flat_minor = {'d', 'g', 'c', 'f', 'bb', 'eb'}
        
        if self.tonic in sharp_major or self.tonic in sharp_minor:
            return 'sharps'
        elif self.tonic in flat_major or self.tonic in flat_minor:
            return 'flats'
        # Default to sharps for unknown tonics
        return 'sharps'

    def _create_base_scale(self):
        if self.scale_type == 'sharps':
            return ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#']
        else:
            return ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab']

    def chromatic(self):
        return self.chromatic_scale

    def interval(self, intervals):
        interval_steps = {'m': 1, 'M': 2, 'A': 3}
        current_pos = 0  # Starting position in chromatic_scale
        notes = [self.chromatic_scale[current_pos]]
        
        for interval in intervals:
            current_pos = (current_pos + interval_steps[interval]) % 12
            notes.append(self.chromatic_scale[current_pos])
            
        return notes
