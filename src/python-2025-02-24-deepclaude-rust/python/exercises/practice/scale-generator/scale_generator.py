class Scale:
    def __init__(self, tonic):
        self.tonic = self.normalize_tonic(tonic)
        self.use_sharps = self.determine_key_signature()
        self.notes = self.sharp_notes if self.use_sharps else self.flat_notes
        self.tonic_index = self.notes.index(self.tonic)
    
    def normalize_tonic(self, tonic):
        if not tonic:
            return tonic
        return tonic[0].upper() + tonic[1:].lower()
    
    def determine_key_signature(self):
        major_sharp = {'G', 'D', 'A', 'E', 'B', 'F#'}
        minor_sharp = {'E', 'B', 'F#', 'C#', 'G#', 'D#'}
        natural_sharp = {'C', 'A'}
        sharp_tonics = major_sharp.union(minor_sharp).union(natural_sharp)
        
        major_flat = {'F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb'}
        minor_flat = {'D', 'G', 'C', 'F', 'Bb', 'Eb'}
        flat_tonics = major_flat.union(minor_flat)
        
        if self.tonic in sharp_tonics:
            return True
        elif self.tonic in flat_tonics:
            return False
        else:
            return True
    
    @property
    def sharp_notes(self):
        return ['C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#', 'A', 'A#', 'B']
    
    @property
    def flat_notes(self):
        return ['C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab', 'A', 'Bb', 'B']
    
    def chromatic(self):
        return self.notes[self.tonic_index:] + self.notes[:self.tonic_index]
    
    def interval(self, intervals):
        current_index = self.tonic_index
        scale = [self.notes[current_index]]
        step_map = {'m': 1, 'M': 2, 'A': 3}
        for interval in intervals:
            step = step_map.get(interval, 0)
            current_index = (current_index + step) % 12
            scale.append(self.notes[current_index])
        return scale
