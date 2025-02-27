// You should change this.
//
// Depending on your implementation, there are a variety of potential errors
// which might occur. They aren't checked by the test suite in order to
// allow the greatest freedom of implementation, but real libraries should
// provide useful, descriptive errors so that downstream code can react
// appropriately.
//
// One common idiom is to define an Error enum which wraps all potential
// errors. Another common idiom is to use a helper type such as failure::Error
// which does more or less the same thing but automatically.
#[derive(Debug, PartialEq)]
pub enum Error {
    InvalidTonic,
    InvalidInterval,
}

pub struct Scale {
    notes: Vec<String>,
}

impl Scale {
    pub fn new(tonic: &str, intervals: &str) -> Result<Scale, Error> {
        const SHARPS: [&str; 12] = ["A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"];
        const FLATS: [&str; 12] = ["A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"];
        
        let original_sequence = if is_sharp_tonic(tonic)? {
            &SHARPS
        } else {
            &FLATS
        };
        
        let index = original_sequence.iter()
            .position(|note| note.eq_ignore_ascii_case(tonic))
            .ok_or(Error::InvalidTonic)?;
        
        let rotated_sequence: Vec<String> = original_sequence.iter()
            .cycle()
            .skip(index)
            .take(12)
            .map(|s| s.to_string())
            .collect();
        
        let mut current = 0;
        let mut notes = vec![rotated_sequence[current].clone()];
        
        for c in intervals.chars() {
            let step = match c {
                'm' => 1,
                'M' => 2,
                'A' => 3,
                _ => return Err(Error::InvalidInterval),
            };
            current = (current + step) % 12;
            notes.push(rotated_sequence[current].clone());
        }
        
        Ok(Scale { notes })
    }

    pub fn chromatic(tonic: &str) -> Result<Scale, Error> {
        const SHARPS: [&str; 12] = ["A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"];
        const FLATS: [&str; 12] = ["A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"];
        
        let original_sequence = if is_sharp_tonic(tonic)? {
            &SHARPS
        } else {
            &FLATS
        };
        
        let index = original_sequence.iter()
            .position(|note| note.eq_ignore_ascii_case(tonic))
            .ok_or(Error::InvalidTonic)?;
        
        let notes: Vec<String> = original_sequence.iter()
            .cycle()
            .skip(index)
            .take(12)
            .map(|s| s.to_string())
            .collect();
        
        Ok(Scale { notes })
    }

    pub fn enumerate(&self) -> Vec<String> {
        self.notes.clone()
    }
}

fn is_sharp_tonic(tonic: &str) -> Result<bool, Error> {
    let sharps_tonics = [
        "g", "d", "a", "e", "b", "f#",
        "e", "b", "f#", "c#", "g#", "d#"
    ];
    let flats_tonics = [
        "f", "bb", "eb", "ab", "db", "gb",
        "d", "g", "c", "f", "bb", "eb"
    ];
    
    let normalized = tonic.to_ascii_lowercase();
    
    if sharps_tonics.iter().any(|&s| s == normalized.as_str()) {
        Ok(true)
    } else if flats_tonics.iter().any(|&s| s == normalized.as_str()) {
        Ok(false)
    } else if normalized == "c" || normalized == "a" {
        Ok(true)
    } else {
        Err(Error::InvalidTonic)
    }
}
