package dominoes

type Domino [2]int

func MakeChain(input []Domino) ([]Domino, bool) {
    if len(input) == 0 {
        return []Domino{}, true
    }
    
    // Try all possible starting points
    for i := 0; i < len(input); i++ {
        // Make a copy of the input to avoid modifying original
        dominoes := make([]Domino, len(input))
        copy(dominoes, input)
        
        // Swap first element with current starting point
        dominoes[0], dominoes[i] = dominoes[i], dominoes[0]
        
        // Try both orientations of the first domino
        for _, flip := range []bool{false, true} {
            if flip {
                dominoes[0][0], dominoes[0][1] = dominoes[0][1], dominoes[0][0]
            }
            
            chain := []Domino{dominoes[0]}
            used := make([]bool, len(dominoes))
            used[0] = true
            
            if buildChain(dominoes, &chain, used) {
                // Check if first and last match
                if chain[0][0] == chain[len(chain)-1][1] {
                    return chain, true
                }
            }
        }
    }
    
    return nil, false
}

func buildChain(dominoes []Domino, chain *[]Domino, used []bool) bool {
    if len(*chain) == len(dominoes) {
        return true
    }
    
    last := (*chain)[len(*chain)-1][1]
    
    for i := 0; i < len(dominoes); i++ {
        if !used[i] {
            // Try both orientations
            for _, flip := range []bool{false, true} {
                if flip {
                    dominoes[i][0], dominoes[i][1] = dominoes[i][1], dominoes[i][0]
                }
                
                if dominoes[i][0] == last {
                    *chain = append(*chain, dominoes[i])
                    used[i] = true
                    
                    if buildChain(dominoes, chain, used) {
                        return true
                    }
                    
                    // Backtrack
                    *chain = (*chain)[:len(*chain)-1]
                    used[i] = false
                }
                
                // Restore orientation
                if flip {
                    dominoes[i][0], dominoes[i][1] = dominoes[i][1], dominoes[i][0]
                }
            }
        }
    }
    
    return false
}
