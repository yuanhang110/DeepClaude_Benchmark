package variablelengthquantity

import "errors"

func EncodeVarint(input []uint32) []byte {
	var result []byte
	for _, n := range input {
		// Handle zero case
		if n == 0 {
			result = append(result, 0)
			continue
		}

		var bytes []byte
		for n > 0 {
			// Take the lowest 7 bits
			byteVal := byte(n & 0x7F)
			n >>= 7
			// Set the continuation bit if there are more bytes
			if len(bytes) > 0 {
				byteVal |= 0x80
			}
			bytes = append([]byte{byteVal}, bytes...)
		}
		result = append(result, bytes...)
	}
	return result
}

func DecodeVarint(input []byte) ([]uint32, error) {
	var result []uint32
	var current uint32

	for i := 0; i < len(input); {
		b := input[i]
		// Add the 7 bits to the current value
		current = (current << 7) | uint32(b&0x7F)
		
		// If continuation bit is set, continue to next byte
		if b&0x80 != 0 {
			i++
			continue
		}
		
		// No continuation bit, this is the last byte of the number
		result = append(result, current)
		current = 0
		i++
	}

	return result, nil
}
