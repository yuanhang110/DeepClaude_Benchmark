// This exercise is deprecated.
// Consider working on protein-translation instead.

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
        
        let codon = codon.to_uppercase();
        if codon.chars().any(|c| !"ACGTURYKMSWBDHVN".contains(c)) {
            return Err(Error);
        }

        let mut matches: Vec<&str> = Vec::new();
        for (key, &value) in &self.codon_map {
            if self.is_match(&codon, key) {
                matches.push(value);
            }
        }

        match matches.first() {
            Some(&name) if matches.iter().all(|&n| n == name) => Ok(name),
            _ => Err(Error)
        }
    }

    pub fn of_rna(&self, rna: &str) -> Result<Vec<&'a str>, Error> {
        if rna.is_empty() {
            return Ok(Vec::new());
        }
        if rna.len() % 3 != 0 {
            return Err(Error);
        }

        let mut result = Vec::new();
        for chunk in rna.as_bytes().chunks(3) {
            let codon = std::str::from_utf8(chunk).map_err(|_| Error)?;
            let name = self.name_for(codon)?;
            if name == "stop codon" {
                break;
            }
            result.push(name);
        }
        Ok(result)
    }

    fn is_match(&self, pattern: &str, codon: &str) -> bool {
        pattern.chars().zip(codon.chars()).all(|(p, c)| {
            match p {
                'A' | 'C' | 'G' | 'T' | 'U' => p == c || (p == 'T' && c == 'U') || (p == 'U' && c == 'T'),
                'R' => "AG".contains(c),
                'Y' => "CT".contains(c),
                'M' => "AC".contains(c),
                'K' => "GT".contains(c),
                'S' => "CG".contains(c),
                'W' => "AT".contains(c),
                'H' => "ACT".contains(c),
                'B' => "CGT".contains(c),
                'V' => "ACG".contains(c),
                'D' => "AGT".contains(c),
                'N' => "ACGT".contains(c),
                _ => false
            }
        })
    }
}

pub fn parse<'a>(pairs: Vec<(&'a str, &'a str)>) -> CodonsInfo<'a> {
    let mut codon_map = HashMap::new();
    for (codon, name) in pairs {
        codon_map.insert(codon.to_string(), name);
    }
    CodonsInfo { codon_map }
}
