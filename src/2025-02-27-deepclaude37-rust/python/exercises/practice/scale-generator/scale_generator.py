class Scale:
    def __init__(self, tonic):
        # Determine if the tonic is for a major or minor scale based on case
        is_minor = tonic[0].islower()
        
        # Normalize the tonic for scale lookup
        note_part = tonic[0].upper() + tonic[1:]
        
        # Define key signature sets based on the table in the instructions
        major_sharps = {'C', 'G', 'D', 'A', 'E', 'B', 'F#'}
        major_flats = {'F', 'Bb', 'Eb', 'Ab', 'Db', 'Gb'}
        
        minor_sharps = {'A', 'E', 'B', 'F#', 'C#', 'G#', 'D#'}
        minor_flats = {'D', 'G', 'C', 'F', 'Bb', 'Eb'}
        
        # Determine whether to use sharps or flats based on the tonic and its mode
        if is_minor:
            if note_part in minor_sharps:
                self.use_sharps = True
            else:
                self.use_sharps = False
        else:  # major
            if note_part in major_sharps:
                self.use_sharps = True
            else:
                self.use_sharps = False
        
        # Store the normalized tonic for scale generation
        self.tonic = note_part

    def chromatic(self):
        # Define the two versions of the chromatic scale
        sharps_order = ['A', 'A#', 'B', 'C', 'C#', 'D', 'D#', 'E', 'F', 'F#', 'G', 'G#']
        flats_order = ['A', 'Bb', 'B', 'C', 'Db', 'D', 'Eb', 'E', 'F', 'Gb', 'G', 'Ab']
        
        # Select appropriate scale based on tonic
        selected_order = sharps_order if self.use_sharps else flats_order
        
        # Rotate the scale to start with the tonic
        index = selected_order.index(self.tonic)
        return selected_order[index:] + selected_order[:index]

    def interval(self, intervals):
        # Get the chromatic scale starting with the tonic
        chromatic_scale = self.chromatic()
        
        # Start with the tonic
        result = [chromatic_scale[0]]
        current_index = 0
        
        # Map intervals to steps in chromatic scale
        interval_steps = {'m': 1, 'M': 2, 'A': 3}
        
        # Build the scale according to interval pattern
        for interval in intervals:
            current_index = (current_index + interval_steps[interval]) % 12
            result.append(chromatic_scale[current_index])
            
        return result
