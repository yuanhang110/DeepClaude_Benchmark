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

	for i, d := range input {
		for _, first := range []Domino{d, {d[1], d[0]}} {
			used := make([]bool, len(input))
			used[i] = true
			if chain, ok := backtrack(input, used, first[0], first[1], []Domino{first}, len(input)-1); ok {
				return chain, true
			}
		}
	}
	return nil, false
}

func backtrack(input []Domino, used []bool, start, end int, current []Domino, remaining int) ([]Domino, bool) {
	if remaining == 0 {
		if start == end {
			return current, true
		}
		return nil, false
	}

	for i, d := range input {
		if !used[i] {
			for _, rot := range []Domino{d, {d[1], d[0]}} {
				if rot[0] == end {
					newUsed := make([]bool, len(used))
					copy(newUsed, used)
					newUsed[i] = true
					newCurrent := append(current, rot)
					if chain, ok := backtrack(input, newUsed, start, rot[1], newCurrent, remaining-1); ok {
						return chain, true
					}
				}
			}
		}
	}
	return nil, false
}
