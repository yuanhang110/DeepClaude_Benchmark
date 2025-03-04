#[derive(Debug)]
pub enum Error {
    InvalidTonic,
    InvalidInterval,
}

pub struct Scale {
    notes: Vec<String>,
}

const SHARPS: [&str; 12] = ["A", "A#", "B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#"];
const FLATS: [&str; 12] = ["A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab"];

impl Scale {
    pub fn new(tonic: &str, intervals: &str) -> Result<Scale, Error> {
        let chromatic = Self::get_chromatic_scale(tonic)?;
        let mut notes = Vec::new();
        let mut index = 0;
        
        notes.push(chromatic[index].to_string());
        
        for c in intervals.chars() {
            match c {
                'm' => index += 1,
                'M' => index += 2,
                'A' => index += 3,
                _ => return Err(Error::InvalidInterval),
            }
            notes.push(chromatic[index % 12].to_string());
        }
        
        Ok(Scale { notes })
    }

    pub fn chromatic(tonic: &str) -> Result<Scale, Error> {
        let chromatic = Self::get_chromatic_scale(tonic)?;
        Ok(Scale { notes: chromatic.iter().map(|s| s.to_string()).collect() })
    }

    pub fn enumerate(&self) -> Vec<String> {
        self.notes.clone()
    }

    fn get_chromatic_scale(tonic: &str) -> Result<&'static [&'static str], Error> {
        let tonic = Self::normalize_note(tonic);
        
        match tonic.as_str() {
            "C" | "a" | "G" | "D" | "A" | "E" | "B" | "F#" |
            "e" | "b" | "f#" | "c#" | "g#" | "d#" => Ok(&SHARPS),
            "F" | "Bb" | "Eb" | "Ab" | "Db" | "Gb" |
            "d" | "g" | "c" | "f" | "bb" | "eb" => Ok(&FLATS),
            _ => Err(Error::InvalidTonic),
        }
    }

    fn normalize_note(note: &str) -> String {
        if note.len() > 1 {
            let mut chars = note.chars();
            let first = chars.next().unwrap().to_uppercase().next().unwrap();
            let second = chars.next().unwrap();
            format!("{}{}", first, second)
        } else {
            note.to_uppercase()
        }
    }
}
