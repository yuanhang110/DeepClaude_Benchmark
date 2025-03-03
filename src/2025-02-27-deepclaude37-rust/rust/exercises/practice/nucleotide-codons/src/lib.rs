// This exercise is deprecated.
// Consider working on protein-translation instead.

use std::collections::HashMap;

pub struct CodonsInfo<'a> {
    codon_map: HashMap<&'a str, &'a str>,
}

#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub struct Error;

impl<'a> CodonsInfo<'a> {
    pub fn name_for(&self, codon: &str) -> Result<&'a str, Error> {
        if codon.len() != 3 {
            return Err(Error);
        }

        let mut codons = vec![String::new()];
        for c in codon.chars() {
            let possible = match c {
                'A' => &['A'][..],
                'C' => &['C'][..],
                'G' => &['G'][..],
                'T' => &['T'][..],
                'U' => &['U'][..],
                'R' => &['A', 'G'][..],
                'Y' => &['C', 'T'][..],
                'S' => &['G', 'C'][..],
                'W' => &['A', 'T'][..],
                'K' => &['G', 'T'][..],
                'M' => &['A', 'C'][..],
                'B' => &['C', 'G', 'T'][..],
                'D' => &['A', 'G', 'T'][..],
                'H' => &['A', 'C', 'T'][..],
                'V' => &['A', 'C', 'G'][..],
                'N' => &['A', 'C', 'G', 'T'][..],
                _ => return Err(Error),
            };
            let mut new_codons = Vec::new();
            for s in &codons {
                for &n in possible {
                    let mut new_s = s.clone();
                    new_s.push(n);
                    new_codons.push(new_s);
                }
            }
            codons = new_codons;
        }

        let mut names = Vec::new();
        for c in &codons {
            if let Some(&name) = self.codon_map.get(c.as_str()) {
                names.push(name);
            } else {
                return Err(Error);
            }
        }

        if names.is_empty() {
            return Err(Error);
        }

        let first = names[0];
        if names.iter().all(|&n| n == first) {
            Ok(first)
        } else {
            Err(Error)
        }
    }

    pub fn of_rna(&self, rna: &str) -> Result<Vec<&'a str>, Error> {
        if rna.len() % 3 != 0 {
            return Err(Error);
        }

        let mut proteins = Vec::new();
        for i in (0..rna.len()).step_by(3) {
            if i + 3 > rna.len() {
                break;
            }
            
            let codon = &rna[i..i+3];
            let protein = self.name_for(codon)?;
            
            if protein == "stop codon" {
                break;
            }
            
            proteins.push(protein);
        }

        Ok(proteins)
    }
}

pub fn parse<'a>(pairs: Vec<(&'a str, &'a str)>) -> CodonsInfo<'a> {
    let mut codon_map = HashMap::new();
    for (codon, name) in pairs {
        codon_map.insert(codon, name);
    }
    CodonsInfo { codon_map }
}
