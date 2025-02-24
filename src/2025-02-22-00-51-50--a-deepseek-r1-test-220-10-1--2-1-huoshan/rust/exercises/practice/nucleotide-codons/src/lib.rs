use std::collections::HashMap;

pub struct CodonsInfo<'a> {
    codon_map: HashMap<String, &'a str>,
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub struct Error;

impl<'a> CodonsInfo<'a> {
    pub fn name_for(&self, codon: &str) -> Result<&'a str, Error> {
        if codon.len() != 3 {
            return Err(Error);
        }

        let mut possible = vec![vec![]; 3];
        for (i, c) in codon.chars().enumerate() {
            possible[i] = match c {
                'A' => vec!['A'],
                'C' => vec!['C'],
                'G' => vec!['G'],
                'T' => vec!['T'],
                'R' => vec!['A', 'G'],
                'Y' => vec!['C', 'T'],
                'S' => vec!['C', 'G'],
                'W' => vec!['A', 'T'],
                'K' => vec!['G', 'T'],
                'M' => vec!['A', 'C'],
                'B' => vec!['C', 'G', 'T'],
                'D' => vec!['A', 'G', 'T'],
                'H' => vec!['A', 'C', 'T'],
                'V' => vec!['A', 'C', 'G'],
                'N' => vec!['A', 'C', 'G', 'T'],
                _ => return Err(Error),
            };
        }

        for b0 in &possible[0] {
            for b1 in &possible[1] {
                for b2 in &possible[2] {
                    let candidate: String = [*b0, *b1, *b2].iter().collect();
                    if let Some(name) = self.codon_map.get(&candidate) {
                        return Ok(name);
                    }
                }
            }
        }

        Err(Error)
    }

    pub fn of_rna(&self, rna: &str) -> Result<Vec<&'a str>, Error> {
        todo!("Return a list of protein names that correspond to the '{}' RNA string or Err if the RNA string is invalid", rna);
    }
}

pub fn parse<'a>(pairs: Vec<(&'a str, &'a str)>) -> CodonsInfo<'a> {
    let mut codon_map = HashMap::new();
    for (codon, name) in pairs {
        codon_map.insert(codon.to_string(), name);
    }
    CodonsInfo { codon_map }
}
