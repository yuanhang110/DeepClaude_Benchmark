#[derive(Debug)]
pub enum Error {
    InvalidTonic,
    InvalidInterval,
    InvalidIntervalSum,
}

pub struct Scale {
    notes: Vec<String>,
}

impl Scale {
    pub fn new(tonic: &str, intervals: &str) -> Result<Scale, Error> {
        const SHARP_SCALE: [&str; 12] = [
            "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#", "B"
        ];
        const FLAT_SCALE: [&str; 12] = [
            "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B"
        ];

        // Determine whether to use sharps or flats based on the tonic and intervals
        let normalized_tonic = tonic.trim().to_lowercase();
        
        // Check if this is the harmonic minor scale
        let is_harmonic_minor = intervals == "MmMMmAm";
        
        // Check if this is a scale that should use minor accidentals (starts with m or is harmonic minor)
        let treat_as_minor = intervals.starts_with('m') || is_harmonic_minor;
        
        let is_sharp = if treat_as_minor {
            // For minor-like scales, use the minor scale accidental rules
            match normalized_tonic.as_str() {
                // Minor scales that use sharps
                "e" | "b" | "f#" | "c#" | "g#" | "d#" => true,
                // Minor scales that use flats
                "d" | "g" | "c" | "f" | "bb" | "eb" => false,
                // Default for other tonics
                _ => normalized_tonic.contains('#') || !normalized_tonic.contains('b'),
            }
        } else if intervals == "MmMMmMM" {
            // Natural minor scale - same rules as above
            match normalized_tonic.as_str() {
                "e" | "b" | "f#" | "c#" | "g#" | "d#" => true,
                "d" | "g" | "c" | "f" | "bb" | "eb" => false,
                _ => normalized_tonic.contains('#') || !normalized_tonic.contains('b'),
            }
        } else {
            // Major and other scales
            match normalized_tonic.as_str() {
                // Major scales that use sharps
                "g" | "d" | "a" | "e" | "b" | "f#" => true,
                // Major scales that use flats
                "f" | "bb" | "eb" | "ab" | "db" | "gb" => false,
                // Default to sharps for C and for tonics with sharps
                _ => normalized_tonic.contains('#') || !normalized_tonic.contains('b'),
            }
        };

        let scale = if is_sharp { &SHARP_SCALE } else { &FLAT_SCALE };

        // Find the tonic's position in the scale (case-insensitive)
        let index = scale.iter()
            .position(|&note| note.eq_ignore_ascii_case(tonic))
            .ok_or(Error::InvalidTonic)?;

        // Create a rotated scale that starts with the tonic
        let rotated_scale: Vec<String> = scale.iter()
            .cycle()
            .skip(index)
            .take(12)
            .map(|&s| s.to_string())
            .collect();

        // Parse interval steps (m=1, M=2, A=3)
        let steps = intervals.chars()
            .map(|c| match c {
                'm' => Ok(1),
                'M' => Ok(2),
                'A' => Ok(3),
                _ => Err(Error::InvalidInterval),
            })
            .collect::<Result<Vec<_>, _>>()?;

        // Apply intervals to get the scale notes
        let mut current = 0;
        let mut notes = vec![rotated_scale[0].clone()];

        for step in steps {
            current = (current + step) % 12;
            notes.push(rotated_scale[current].clone());
        }

        Ok(Scale { notes })
    }

    pub fn chromatic(tonic: &str) -> Result<Scale, Error> {
        // For chromatic scale, we still need to determine sharps or flats based on the tonic
        let normalized_tonic = tonic.trim().to_lowercase();
        let intervals = "mmmmmmmmmmmm";
        
        // For chromatic scale, follow the major scale rules for the tonic
        let is_sharp = match normalized_tonic.as_str() {
            // Major scales that use sharps
            "g" | "d" | "a" | "e" | "b" | "f#" => true,
            // Major scales that use flats
            "f" | "bb" | "eb" | "ab" | "db" | "gb" => false,
            // Default to sharps for C and for tonics with sharps
            _ => normalized_tonic.contains('#') || !normalized_tonic.contains('b'),
        };
        
        let scale = if is_sharp {
            &SHARP_SCALE
        } else {
            &FLAT_SCALE
        };
        
        let index = scale.iter()
            .position(|&note| note.eq_ignore_ascii_case(tonic))
            .ok_or(Error::InvalidTonic)?;
            
        let rotated_scale: Vec<String> = scale.iter()
            .cycle()
            .skip(index)
            .take(13)
            .map(|&s| s.to_string())
            .collect();
            
        Ok(Scale { notes: rotated_scale })
    }

    pub fn enumerate(&self) -> Vec<String> {
        self.notes.clone()
    }
}
