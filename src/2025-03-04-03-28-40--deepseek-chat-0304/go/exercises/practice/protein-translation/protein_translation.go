package protein

import (
	"errors"
)

var (
	ErrStop        = errors.New("stop codon encountered")
	ErrInvalidBase = errors.New("invalid codon")
)

var codonMap = map[string]string{
	"AUG": "Methionine",
	"UUU": "Phenylalanine", 
	"UUC": "Phenylalanine",
	"UUA": "Leucine",
	"UUG": "Leucine",
	"UCU": "Serine",
	"UCC": "Serine",
	"UCA": "Serine",
	"UCG": "Serine",
	"UAU": "Tyrosine",
	"UAC": "Tyrosine",
	"UGU": "Cysteine",
	"UGC": "Cysteine",
	"UGG": "Tryptophan",
	"UAA": "STOP",
	"UAG": "STOP",
	"UGA": "STOP",
}

func FromCodon(codon string) (string, error) {
	protein, exists := codonMap[codon]
	if !exists {
		return "", ErrInvalidBase
	}
	if protein == "STOP" {
		return "", ErrStop
	}
	return protein, nil
}

func FromRNA(rna string) ([]string, error) {
	var proteins []string
	
	// Split RNA into codons (3 nucleotides each)
	for i := 0; i < len(rna); i += 3 {
		end := i + 3
		if end > len(rna) {
			end = len(rna)
		}
		codon := rna[i:end]
		
		protein, err := FromCodon(codon)
		if err != nil {
			return nil, err
		}
		if protein == "" { // STOP codon
			break
		}
		proteins = append(proteins, protein)
	}
	
	return proteins, nil
}
