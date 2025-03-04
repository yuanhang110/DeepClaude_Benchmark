use std::collections::HashMap;

pub struct CodonsInfo<'a> {
    codon_map: HashMap<&'a str, &'a str>,
}

// IUPAC nucleotide codes mapping
const IUPAC_CODES: &[(char, &str)] = &[
    ('A', "A"),
    ('C', "C"),
    ('G', "G"),
    ('T', "T"),
    ('U', "T"),
    ('W', "AT"),
    ('S', "CG"),
    ('M', "AC"),
    ('K', "GT"),
    ('R', "AG"),
    ('Y', "CT"),
    ('B', "CGT"),
    ('D', "AGT"),
    ('H', "ACT"),
    ('V', "ACG"),
    ('N', "ACGT"),
];

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub struct Error;

impl<'a> CodonsInfo<'a> {
    pub fn name_for(&self, codon: &str) -> Result<&'a str, Error> {
        if codon.len() != 3 {
            return Err(Error);
        }

        // Expand each character in the codon to its possible nucleotides
        let mut possibilities = vec![String::new()];
        
        for c in codon.chars() {
            let expanded = IUPAC_CODES.iter()
                .find(|(code, _)| *code == c.to_ascii_uppercase())
                .map(|(_, bases)| *bases)
                .ok_or(Error)?;
                
            possibilities = possibilities.into_iter()
                .flat_map(|prefix| expanded.chars().map(move |b| format!("{}{}", prefix, b)))
                .collect();
        }

        // Check if all possibilities map to the same protein
        let mut protein = None;
        
        for possible_codon in possibilities {
            if let Some(&name) = self.codon_map.get(possible_codon.as_str()) {
                match protein {
                    None => protein = Some(name),
                    Some(p) if p != name => return Err(Error),
                    _ => (),
                }
            } else {
                return Err(Error);
            }
        }

        protein.ok_or(Error)
    }

    pub fn of_rna(&self, rna: &str) -> Result<Vec<&'a str>, Error> {
        todo!("Return a list of protein names that correspond to the '{}' RNA string or Err if the RNA string is invalid", rna);
    }
}

pub fn parse<'a>(pairs: Vec<(&'a str, &'a str)>) -> CodonsInfo<'a> {
    let codon_map = pairs.into_iter().collect();
    CodonsInfo { codon_map }
}
