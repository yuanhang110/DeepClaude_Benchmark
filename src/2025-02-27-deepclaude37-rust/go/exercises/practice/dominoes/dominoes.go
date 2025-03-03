package dominoes

type Domino [2]int

func MakeChain(input []Domino) ([]Domino, bool) {
	// Empty input is considered a valid chain
	if len(input) == 0 {
		return []Domino{}, true
	}
	
	// Single domino is valid only if both sides match
	if len(input) == 1 {
		d := input[0]
		if d[0] == d[1] {
			return []Domino{d}, true
		}
		return nil, false
	}

	// Process special cases for single dominoes with matching sides
	for _, d := range input {
		if d[0] == d[1] {
			// If there's a self-loop domino, it can be inserted anywhere in the chain
			// So we can just proceed with the remaining dominoes
			newInput := make([]Domino, 0, len(input)-1)
			foundSelfLoop := false
			var selfLoop Domino
			
			for _, domino := range input {
				if !foundSelfLoop && domino[0] == domino[1] {
					foundSelfLoop = true
					selfLoop = domino
					continue
				}
				newInput = append(newInput, domino)
			}
			
			if len(newInput) == 0 {
				// Just the self-loop
				return []Domino{selfLoop}, true
			}
			
			chain, ok := MakeChain(newInput)
			if ok {
				// Insert the self-loop at the beginning
				return append([]Domino{selfLoop}, chain...), true
			}
		}
	}
	
	// Check if we can form a cycle
	// In a valid domino chain, either all degrees are even (Eulerian circuit)
	// or exactly two nodes have odd degree (Eulerian path)
	degrees := make(map[int]int)
	for _, d := range input {
		degrees[d[0]]++
		degrees[d[1]]++
	}
	
	oddNodes := 0
	for _, cnt := range degrees {
		if cnt%2 != 0 {
			oddNodes++
		}
	}
	
	if oddNodes > 0 && oddNodes != 2 {
		return nil, false
	}

	// Build adjacency list
	adj := make(map[int][]int)
	for i, d := range input {
		adj[d[0]] = append(adj[d[0]], i)
		adj[d[1]] = append(adj[d[1]], i)
	}

	used := make([]bool, len(input))
	var chain []Domino
	
	// Find a good starting node - prefer nodes with odd degree if any
	var startNode int
	hasOddNode := false
	
	for node, degree := range degrees {
		if degree%2 != 0 {
			startNode = node
			hasOddNode = true
			break
		}
	}
	
	// If no odd degree nodes, start with any node
	if !hasOddNode {
		for node := range adj {
			startNode = node
			break
		}
	}

	// Hierholzer's algorithm with preserved adjacency list
	edgePtr := make(map[int]int) // Track position in each node's adjacency list
	var stack []Domino
	
	var hierholzer func(int)
	hierholzer = func(u int) {
		for edgePtr[u] < len(adj[u]) {
			idx := adj[u][edgePtr[u]]
			edgePtr[u]++
			if used[idx] {
				continue
			}
			used[idx] = true

			d := input[idx]
			v := d[0]
			if d[0] == u {
				v = d[1]
			} else {
				v = d[0]
			}

			stack = append(stack, Domino{u, v})
			hierholzer(v)
		}

		if len(stack) > 0 {
			chain = append(chain, stack[len(stack)-1])
			stack = stack[:len(stack)-1]
		}
	}

	hierholzer(startNode)

	// Check if all dominoes are used
	for _, u := range used {
		if !u {
			return nil, false
		}
	}

	// Check if the chain is a valid loop
	if len(chain) == 0 {
		return nil, false
	}
	if chain[0][0] != chain[len(chain)-1][1] {
		return nil, false
	}

	reverse(chain)

	return chain, true
}

func find(parent map[int]int, x int) int {
	if parent[x] != x {
		parent[x] = find(parent, parent[x])
	}
	return parent[x]
}

func union(parent map[int]int, x, y int) {
	fx := find(parent, x)
	fy := find(parent, y)
	if fx != fy {
		parent[fy] = fx
	}
}

func reverse(s []Domino) {
	for i, j := 0, len(s)-1; i < j; i, j = i+1, j-1 {
		s[i], s[j] = s[j], s[i]
	}
}
