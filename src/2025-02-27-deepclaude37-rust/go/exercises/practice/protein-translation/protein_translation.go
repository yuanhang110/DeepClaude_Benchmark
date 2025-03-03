package protein

import "errors"

var (
	codonMap = map[string]string{
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
	}
	stopCodons = map[string]bool{
		"UAA": true,
		"UAG": true,
		"UGA": true,
	}
	ErrStop        = errors.New("stop codon")
	ErrInvalidBase = errors.New("invalid codon")
)

func FromRNA(rna string) ([]string, error) {
	var proteins []string
	for i := 0; i < len(rna); i += 3 {
		end := i + 3
		if end > len(rna) {
			end = len(rna)
		}
		codon := rna[i:end]
		protein, err := FromCodon(codon)
		if err != nil {
			if err == ErrStop {
				return proteins, nil
			}
			return nil, err
		}
		proteins = append(proteins, protein)
	}
	return proteins, nil
}

func FromCodon(codon string) (string, error) {
	if len(codon) != 3 {
		return "", ErrInvalidBase
	}
	if stopCodons[codon] {
		return "", ErrStop
	}
	if protein, ok := codonMap[codon]; ok {
		return protein, nil
	}
	return "", ErrInvalidBase
}
