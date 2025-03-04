package variablelengthquantity

import "errors"

func EncodeVarint(input []uint32) []byte {
	var result []byte
	for _, num := range input {
		var chunks []byte
		n := num
		if n == 0 {
			result = append(result, 0)
			continue
		}
		for n > 0 {
			chunks = append(chunks, byte(n%128))
			n = n / 128
		}
		// Reverse chunks
		for i, j := 0, len(chunks)-1; i < j; i, j = i+1, j-1 {
			chunks[i], chunks[j] = chunks[j], chunks[i]
		}
		// Set high bit on all but last chunk
		for i := 0; i < len(chunks)-1; i++ {
			chunks[i] |= 0x80
		}
		result = append(result, chunks...)
	}
	return result
}

func DecodeVarint(input []byte) ([]uint32, error) {
	var result []uint32
	var current uint32
	for _, b := range input {
		current = current<<7 | uint32(b&0x7F)
		if (b & 0x80) == 0 {
			result = append(result, current)
			current = 0
		}
	}
	if current != 0 {
		return nil, errors.New("incomplete sequence")
	}
	return result, nil
}
