package variablelengthquantity

import "errors"

func EncodeVarint(input []uint32) []byte {
	var encoded []byte
	for _, num := range input {
		if num == 0 {
			encoded = append(encoded, 0x00)
			continue
		}
		var chunks []byte
		for num > 0 {
			chunk := byte(num & 0x7F)
			chunks = append(chunks, chunk)
			num >>= 7
		}
		// Reverse chunks to get most significant part first
		for i, j := 0, len(chunks)-1; i < j; i, j = i+1, j-1 {
			chunks[i], chunks[j] = chunks[j], chunks[i]
		}
		// Set continuation bits on all but last chunk
		for i := 0; i < len(chunks)-1; i++ {
			chunks[i] |= 0x80
		}
		encoded = append(encoded, chunks...)
	}
	return encoded
}

func DecodeVarint(input []byte) ([]uint32, error) {
	var result []uint32
	var current uint32
	var hasPending bool
	for _, b := range input {
		current = (current << 7) | uint32(b&0x7F)
		if (b & 0x80) == 0 {
			result = append(result, current)
			current = 0
			hasPending = false
		} else {
			hasPending = true
		}
	}
	if hasPending {
		return nil, errors.New("incomplete sequence")
	}
	return result, nil
}
