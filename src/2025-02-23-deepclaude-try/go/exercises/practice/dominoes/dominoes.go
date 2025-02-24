package dominoes

type Domino [2]int

func MakeChain(input []Domino) ([]Domino, bool) {
	if len(input) == 0 {
		return []Domino{}, true
	}
	if len(input) == 1 {
		d := input[0]
		if d[0] == d[1] {
			return []Domino{d}, true
		}
		return nil, false
	}

	// Try each domino as first in both orientations
	for i := 0; i < len(input); i++ {
		// Original orientation
		used := make([]bool, len(input))
		used[i] = true
		start := input[i][0]
		end := input[i][1]
		if chain, ok := backtrack(input, used, []Domino{input[i]}, start, end); ok {
			return chain, true
		}
		// Flipped orientation
		used = make([]bool, len(input))
		used[i] = true
		flipped := Domino{input[i][1], input[i][0]}
		start = flipped[0]
		end = flipped[1]
		if chain, ok := backtrack(input, used, []Domino{flipped}, start, end); ok {
			return chain, true
		}
	}
	return nil, false
}

func backtrack(input []Domino, used []bool, chain []Domino, start, end int) ([]Domino, bool) {
	if len(chain) == len(input) {
		if start == end {
			return chain, true
		}
		return nil, false
	}

	for i := 0; i < len(input); i++ {
		if !used[i] {
			d := input[i]
			// Try original orientation
			if d[0] == end {
				newUsed := make([]bool, len(used))
				copy(newUsed, used)
				newUsed[i] = true
				newChain := append(chain, d)
				if result, ok := backtrack(input, newUsed, newChain, start, d[1]); ok {
					return result, true
				}
			}
			// Try flipped orientation
			if d[1] == end {
				newUsed := make([]bool, len(used))
				copy(newUsed, used)
				newUsed[i] = true
				flipped := Domino{d[1], d[0]}
				newChain := append(chain, flipped)
				if result, ok := backtrack(input, newUsed, newChain, start, flipped[1]); ok {
					return result, true
				}
			}
		}
	}

	return nil, false
}
