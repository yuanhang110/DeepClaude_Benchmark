# Notes sequences for sharp and flat scales
SHARP_NOTES = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#']
FLAT_NOTES = ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab']

# Sets of tonics that use sharp or flat notation
SHARP_TONICS = {'G', 'D', 'A', 'E', 'B', 'F#', 'C#', 'G#', 'D#', 'C'}  # Including C for ascending
FLAT_TONICS = {'F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb'}

class Scale:
    def __init__(self, tonic):
        # Normalize tonic to have uppercase first letter, rest lowercase
        if len(tonic) > 1 and tonic[1] in 'bB':
            self.tonic = tonic[0].upper() + 'b'
        else:
            self.tonic = tonic[0].upper() + (tonic[1:].lower() if len(tonic) > 1 else '')
        
        # Select note sequence based on tonic
        if self.tonic in SHARP_TONICS:
            selected_notes = SHARP_NOTES
        else:
            selected_notes = FLAT_NOTES
        
        # Find tonic index and rotate sequence to start with tonic
        idx = selected_notes.index(self.tonic)
        self.notes = selected_notes[idx:] + selected_notes[:idx]

    def chromatic(self):
        return self.notes

    def interval(self, intervals):
        # Map interval symbols to number of half steps
        steps = {'m': 1, 'M': 2, 'A': 3}
        
        current = 0
        scale = [self.notes[current]]
        
        for interval in intervals:
            if interval not in steps:
                raise ValueError(f"Invalid interval '{interval}'")
            current = (current + steps[interval]) % 12
            scale.append(self.notes[current])
            
        return scale
