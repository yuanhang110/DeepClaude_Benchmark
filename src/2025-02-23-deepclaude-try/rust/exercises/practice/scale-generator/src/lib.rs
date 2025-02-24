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
#[derive(Debug)]
pub enum Error {
    InvalidTonic,
    InvalidIntervals,
}

pub struct Scale {
    notes: Vec<String>,
}

impl Scale {
    pub fn new(tonic: &str, intervals: &str) -> Result<Scale, Error> {
        let tonic = tonic.to_uppercase();
        let notes = Self::get_notes(&tonic)?;
        let steps = Self::parse_intervals(intervals)?;
        let scale_notes = Self::generate_scale(&notes, &steps, &tonic)?;
        Ok(Scale { notes: scale_notes })
    }

    pub fn chromatic(tonic: &str) -> Result<Scale, Error> {
        Self::new(tonic, "mmmmmmmmmmm") // 11 half steps
    }

    pub fn enumerate(&self) -> Vec<String> {
        self.notes.clone()
    }

    fn get_notes(tonic: &str) -> Result<Vec<String>, Error> {
        let sharps = vec!["A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"];
        let flats = vec!["A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"];
        
        match tonic.as_str() {
            "C" | "A" | "E" => Ok(sharps.clone()),
            "F" | "Bb" | "Eb" => Ok(flats.clone()),
            "G" | "D" | "A" | "E" | "B" | "F#" => Ok(sharps.clone()),
            _ => Err(Error::InvalidTonic),
        }
    }

    fn parse_intervals(intervals: &str) -> Result<Vec<usize>, Error> {
        intervals.chars().map(|c| match c {
            'm' => Ok(1),
            'M' => Ok(2),
            'A' => Ok(3),
            _ => Err(Error::InvalidIntervals),
        }).collect()
    }

    fn generate_scale(notes: &[String], steps: &[usize], tonic: &str) -> Result<Vec<String>, Error> {
        let tonic_index = notes.iter().position(|n| n.eq_ignore_ascii_case(tonic)).ok_or(Error::InvalidTonic)?;
        let mut scale = vec![notes[tonic_index].clone()];

        let mut current_index = tonic_index;
        for &step in steps {
            current_index = (current_index + step) % notes.len();
            scale.push(notes[current_index].clone());
        }
        Ok(scale)
    }
