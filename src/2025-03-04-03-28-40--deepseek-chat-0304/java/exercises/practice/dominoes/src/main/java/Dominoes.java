import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

class Domino {
    final int left;
    final int right;
    
    Domino(int left, int right) {
        this.left = left;
        this.right = right;
    }
    
    Domino reversed() {
        return new Domino(right, left);
    }
    
    @Override
    public String toString() {
        return "[" + left + "|" + right + "]";
    }
}

class Dominoes {

    private boolean canFormChain(List<Domino> dominoes, List<Domino> chain, boolean[] used) {
        if (chain.size() == dominoes.size()) {
            return chain.get(0).left == chain.get(chain.size() - 1).right;
        }
        
        for (int i = 0; i < dominoes.size(); i++) {
            if (!used[i]) {
                Domino current = dominoes.get(i);
                
                // Try adding domino normally
                if (chain.isEmpty() || chain.get(chain.size() - 1).right == current.left) {
                    chain.add(current);
                    used[i] = true;
                    if (canFormChain(dominoes, chain, used)) {
                        return true;
                    }
                    chain.remove(chain.size() - 1);
                    used[i] = false;
                }
                
                // Try adding domino reversed
                if (chain.isEmpty() || chain.get(chain.size() - 1).right == current.right) {
                    chain.add(current.reversed());
                    used[i] = true;
                    if (canFormChain(dominoes, chain, used)) {
                        return true;
                    }
                    chain.remove(chain.size() - 1);
                    used[i] = false;
                }
            }
        }
        return false;
    }

    List<Domino> formChain(List<Domino> inputDominoes) throws ChainNotFoundException {
        if (inputDominoes == null) {
            throw new ChainNotFoundException("Input dominoes cannot be null");
        }
        
        if (inputDominoes.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Check for null dominoes
        for (Domino domino : inputDominoes) {
            if (domino == null) {
                throw new ChainNotFoundException("Domino list contains null elements");
            }
        }
        
        List<Domino> chain = new ArrayList<>();
        boolean[] used = new boolean[inputDominoes.size()];
        
        if (canFormChain(inputDominoes, chain, used)) {
            return chain;
        }
        
        throw new ChainNotFoundException("No valid domino chain found");
    }
}
