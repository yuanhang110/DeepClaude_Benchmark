package protein

import (
	"errors"
)

var (
	ErrInvalidBase = errors.New("invalid codon")
	ErrStop        = errors.New("stop codon")
)

func FromRNA(rna string) ([]string, error) {
	var proteins []string
	for i := 0; i <= len(rna)-3; i += 3 {
		codon := rna[i : i+3]
		protein, err := FromCodon(codon)
		if err != nil {
			return proteins, err
		}
		if protein == "STOP" {
			return proteins, ErrStop
		}
		proteins = append(proteins, protein)
	}
	return proteins, nil
}

func FromCodon(codon string) (string, error) {
	codonMap := map[string]string{
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
	if protein, ok := codonMap[codon]; ok {
		return protein, nil
	}
	return "", ErrInvalidBase
}
