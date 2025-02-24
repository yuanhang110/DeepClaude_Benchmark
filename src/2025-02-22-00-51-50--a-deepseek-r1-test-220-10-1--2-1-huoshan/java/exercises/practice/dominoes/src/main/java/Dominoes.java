import java.util.List;
import java.util.ArrayList;

class Dominoes {

    public List<Domino> formChain(List<Domino> inputDominoes) throws ChainNotFoundException {
        if (inputDominoes.isEmpty()) {
            return List.of();
        }
        
        if (inputDominoes.size() == 1) {
            Domino domino = inputDominoes.get(0);
            if (domino.getLeft() == domino.getRight()) {
                return List.of(domino);
            } else {
                throw new ChainNotFoundException("No valid chain found");
            }
        }
        
        List<Domino> result = new ArrayList<>();
        boolean[] used = new boolean[inputDominoes.size()];
        
        for (int i = 0; i < inputDominoes.size(); i++) {
            Domino domino = inputDominoes.get(i);
            
            // Try original orientation
            result.add(domino);
            used[i] = true;
            if (backtrack(inputDominoes, used, result, domino.getRight())) {
                return result;
            }
            used[i] = false;
            result.remove(result.size() - 1);
            
            // Try flipped orientation
            Domino flipped = new Domino(domino.getRight(), domino.getLeft());
            result.add(flipped);
            used[i] = true;
            if (backtrack(inputDominoes, used, result, flipped.getRight())) {
                return result;
            }
            used[i] = false;
            result.remove(result.size() - 1);
        }
        
        throw new ChainNotFoundException("No valid chain found");
    }
    
    private boolean backtrack(List<Domino> inputDominoes, boolean[] used, 
                            List<Domino> currentChain, int currentEnd) {
        if (currentChain.size() == inputDominoes.size()) {
            return currentChain.get(0).getLeft() == currentChain.get(currentChain.size() - 1).getRight();
        }
        
        for (int i = 0; i < inputDominoes.size(); i++) {
            if (!used[i]) {
                Domino domino = inputDominoes.get(i);
                
                // Try original orientation
                if (domino.getLeft() == currentEnd) {
                    used[i] = true;
                    currentChain.add(domino);
                    if (backtrack(inputDominoes, used, currentChain, domino.getRight())) {
                        return true;
                    }
                    currentChain.remove(currentChain.size() - 1);
                    used[i] = false;
                }
                
                // Try flipped orientation
                if (domino.getRight() == currentEnd) {
                    Domino flipped = new Domino(domino.getRight(), domino.getLeft());
                    used[i] = true;
                    currentChain.add(flipped);
                    if (backtrack(inputDominoes, used, currentChain, flipped.getRight())) {
                        return true;
                    }
                    currentChain.remove(currentChain.size() - 1);
                    used[i] = false;
                }
            }
        }
        return false;
    }
    
}
