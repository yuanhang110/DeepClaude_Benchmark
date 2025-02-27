// This exercise is deprecated.
// Consider working on protein-translation instead.

use std::collections::HashMap;

pub struct CodonsInfo<'a> {
    codon_map: HashMap<&'a str, &'a str>,
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub struct Error;

impl<'a> CodonsInfo<'a> {
    fn get_nucleotides(c: char) -> Result<Vec<char>, Error> {
        match c {
            'A' => Ok(vec!['A']),
            'C' => Ok(vec!['C']),
            'G' => Ok(vec!['G']),
            'T' => Ok(vec!['T']),
            'R' => Ok(vec!['A', 'G']),
            'Y' => Ok(vec!['C', 'T']),
            'S' => Ok(vec!['G', 'C']),
            'W' => Ok(vec!['A', 'T']),
            'K' => Ok(vec!['G', 'T']),
            'M' => Ok(vec!['A', 'C']),
            'B' => Ok(vec!['C', 'G', 'T']),
            'D' => Ok(vec!['A', 'G', 'T']),
            'H' => Ok(vec!['A', 'C', 'T']),
            'V' => Ok(vec!['A', 'C', 'G']),
            'N' => Ok(vec!['A', 'C', 'G', 'T']),
            _ => Err(Error),
        }
    }

    pub fn name_for(&self, codon: &str) -> Result<&'a str, Error> {
        if codon.len() != 3 {
            return Err(Error);
        }

        let mut possible_nucleotides = Vec::new();
        for c in codon.chars() {
            let nucs = Self::get_nucleotides(c).map_err(|_| Error)?;
            possible_nucleotides.push(nucs);
        }

        let mut names = Vec::new();
        for n0 in &possible_nucleotides[0] {
            for n1 in &possible_nucleotides[1] {
                for n2 in &possible_nucleotides[2] {
                    let generated_codon = format!("{}{}{}", n0, n1, n2);
                    if let Some(&name) = self.codon_map.get(generated_codon.as_str()) {
                        names.push(name);
                    } else {
                        return Err(Error);
                    }
                }
            }
        }

        if names.is_empty() {
            return Err(Error);
        }

        let first_name = names[0];
        if names.iter().all(|&n| n == first_name) {
            Ok(first_name)
        } else {
            Err(Error)
        }
    }

    pub fn of_rna(&self, rna: &str) -> Result<Vec<&'a str>, Error> {
        todo!("Return a list of protein names that correspond to the '{}' RNA string or Err if the RNA string is invalid", rna);
    }
}

pub fn parse<'a>(pairs: Vec<(&'a str, &'a str)>) -> CodonsInfo<'a> {
    let mut codon_map = HashMap::new();
    for (codon, name) in pairs {
        codon_map.insert(codon, name);
    }
    CodonsInfo { codon_map }
}
