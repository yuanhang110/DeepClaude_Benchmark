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

const SHARP_NOTES: [&str; 12] = ["A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"];
const FLAT_NOTES: [&str; 12] = ["A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"];

fn determine_accidentals(tonic: &str) -> Result<&[&str; 12], Error> {
    match tonic {
        "C" | "a" => Ok(&SHARP_NOTES),
        "G" | "D" | "A" | "E" | "B" | "F#" | "e" | "b" | "f#" | "c#" | "g#" | "d#" => Ok(&SHARP_NOTES),
        "F" | "Bb" | "Eb" | "Ab" | "Db" | "Gb" | "d" | "g" | "c" | "f" | "bb" | "eb" => Ok(&FLAT_NOTES),
        _ => Err(Error::InvalidTonic),
    }
}

fn note_index(tonic: &str, notes: &[&str; 12]) -> Result<usize, Error> {
    notes.iter()
        .position(|&n| n.eq_ignore_ascii_case(tonic))
        .ok_or(Error::InvalidTonic)
}

pub struct Scale {
    notes: Vec<String>,
}

impl Scale {
    pub fn new(tonic: &str, intervals: &str) -> Result<Scale, Error> {
        let accidentals = determine_accidentals(tonic)?;
        let tonic_index = note_index(tonic, accidentals)?;
        
        if intervals.is_empty() || !intervals.chars().all(|c| matches!(c, 'M' | 'm' | 'A')) {
            return Err(Error::InvalidInterval);
        }

        let mut current = tonic_index;
        let mut notes = vec![accidentals[current].to_string()];
        
        for c in intervals.chars() {
            let step = match c {
                'M' => 2,
                'm' => 1,
                'A' => 3,
                _ => return Err(Error::InvalidInterval),
            };
            current = (current + step) % 12;
            notes.push(accidentals[current].to_string());
        }

        Ok(Scale { notes })
    }

    pub fn chromatic(tonic: &str) -> Result<Scale, Error> {
        Self::new(tonic, "mmmmmmmmmmmm")
    }

    pub fn enumerate(&self) -> Vec<String> {
        self.notes.clone()
    }
}
