package variablelengthquantity

import "errors"

func EncodeVarint(input []uint32) []byte {
	var result []byte
	for _, n := range input {
		if n == 0 {
			result = append(result, 0x00)
			continue
		}

		var chunks []byte
		for n > 0 {
			chunks = append(chunks, byte(n&0x7F))
			n >>= 7
		}

		// Reverse chunks
		for i, j := 0, len(chunks)-1; i < j; i, j = i+1, j-1 {
			chunks[i], chunks[j] = chunks[j], chunks[i]
		}

		// Set high bits on all but last chunk
		for i := 0; i < len(chunks)-1; i++ {
			chunks[i] |= 0x80
		}

		result = append(result, chunks...)
	}
	return result
}

func DecodeVarint(input []byte) ([]uint32, error) {
	var result []uint32
	var current uint64

	for _, b := range input {
		current = (current << 7) | uint64(b&0x7F)

		if current > 0xFFFFFFFF {
			return nil, errors.New("overflow")
		}

		if (b & 0x80) == 0 {
			result = append(result, uint32(current))
			current = 0
		} else if current > (0xFFFFFFFF >> 7) {
			return nil, errors.New("overflow")
		}
	}

	if current != 0 {
		return nil, errors.New("incomplete sequence")
	}

	return result, nil
}
