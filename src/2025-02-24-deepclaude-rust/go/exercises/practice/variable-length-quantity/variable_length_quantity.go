package variablelengthquantity

import "fmt"

func EncodeVarint(input []uint32) []byte {
	var output []byte
	for _, num := range input {
		if num == 0 {
			output = append(output, 0x00)
			continue
		}
		var groups []byte
		for n := num; n > 0; n /= 128 {
			groups = append(groups, byte(n%128))
		}
		// Reverse groups
		for i, j := 0, len(groups)-1; i < j; i, j = i+1, j-1 {
			groups[i], groups[j] = groups[j], groups[i]
		}
		// Set high bit on all bytes except the last
		for i := 0; i < len(groups)-1; i++ {
			groups[i] |= 0x80
		}
		output = append(output, groups...)
	}
	return output
}

func DecodeVarint(input []byte) ([]uint32, error) {
	var numbers []uint32
	i := 0
	for i < len(input) {
		var num uint64
		for {
			if i >= len(input) {
				return nil, fmt.Errorf("incomplete sequence")
			}
			b := input[i]
			i++
			num = (num << 7) | uint64(b&0x7F)
			if (b & 0x80) == 0 {
				break
			}
			if num > 0xFFFFFFFF {
				return nil, fmt.Errorf("overflow")
			}
		}
		if num > 0xFFFFFFFF {
			return nil, fmt.Errorf("overflow")
		}
		numbers = append(numbers, uint32(num))
	}
	return numbers, nil
}
