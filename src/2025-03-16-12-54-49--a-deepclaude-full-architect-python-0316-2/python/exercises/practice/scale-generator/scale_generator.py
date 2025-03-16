class Scale:
    SHARP_SCALE = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#']
    FLAT_SCALE = ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab']
    
    # Define which tonics use sharps vs flats
    SHARP_TONICS = {
        'major': ['C', 'G', 'D', 'A', 'E', 'B', 'F#'],
        'minor': ['A', 'E', 'B', 'F#', 'C#', 'G#', 'D#']
    }
    FLAT_TONICS = {
        'major': ['F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb'],
        'minor': ['D', 'G', 'C', 'F', 'Bb', 'Eb']
    }

    def __init__(self, tonic):
        # Normalize tonic to have first letter uppercase, rest lowercase
        self.tonic = tonic[0].upper() + tonic[1:].lower() if len(tonic) > 1 else tonic.upper()
        self.scale = self._determine_scale_type()
    
    def _determine_scale_type(self):
        """Determine whether to use the sharp or flat scale based on the tonic."""
        # Normalize tonic for comparison
        normalized_tonic = self.tonic[0].upper() + self.tonic[1:] if len(self.tonic) > 1 else self.tonic.upper()
        
        # Special case for C and a which follow sharps when ascending
        if normalized_tonic in ['C', 'A']:
            return self.SHARP_SCALE
            
        # Check if the tonic is in the sharp or flat lists
        for tonics in self.SHARP_TONICS.values():
            if normalized_tonic in tonics:
                return self.SHARP_SCALE
                
        return self.FLAT_SCALE
    
    def _get_rotated_scale(self):
        """Get the scale rotated to start with the tonic."""
        try:
            index = self.scale.index(self.tonic)
        except ValueError:
            # Handle case where tonic might be written differently (e.g., "Bb" vs "A#")
            if '#' in self.tonic and self.scale == self.FLAT_SCALE:
                # Convert sharp to flat equivalent
                sharp_index = self.SHARP_SCALE.index(self.tonic)
                self.tonic = self.FLAT_SCALE[sharp_index]
                index = sharp_index
            elif 'b' in self.tonic and self.scale == self.SHARP_SCALE:
                # Convert flat to sharp equivalent
                flat_index = self.FLAT_SCALE.index(self.tonic)
                self.tonic = self.SHARP_SCALE[flat_index]
                index = flat_index
            else:
                raise ValueError(f"Tonic {self.tonic} not found in scale")
                
        return self.scale[index:] + self.scale[:index]

    def chromatic(self):
        """Generate a chromatic scale starting with the tonic."""
        return self._get_rotated_scale()

    def interval(self, intervals):
        """Generate a scale based on the given intervals."""
        # Define interval steps
        interval_map = {'m': 1, 'M': 2, 'A': 3}
        
        rotated_scale = self._get_rotated_scale()
        result = [self.tonic]  # Start with the tonic
        current_position = 0
        
        for interval in intervals:
            steps = interval_map[interval]
            current_position = (current_position + steps) % 12
            result.append(rotated_scale[current_position])
            
        return result
