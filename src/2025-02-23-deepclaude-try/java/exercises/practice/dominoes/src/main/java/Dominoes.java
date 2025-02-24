import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class Dominoes {

    List<Domino> formChain(List<Domino> inputDominoes) throws ChainNotFoundException {
        if (inputDominoes.isEmpty()) {
            return new ArrayList<>();
        }

        boolean[] used = new boolean[inputDominoes.size()];
        List<Domino> chain = new ArrayList<>();
        List<Domino> result = backtrack(inputDominoes, used, chain, -1);
        if (result != null) {
            return result;
        } else {
            throw new ChainNotFoundException();
        }
    }

    private List<Domino> backtrack(List<Domino> dominoes, boolean[] used, List<Domino> chain, int currentEnd) {
        if (chain.size() == dominoes.size()) {
            if (chain.get(0).getLeft() == chain.get(chain.size() - 1).getRight()) {
                return new ArrayList<>(chain);
            } else {
                return null;
            }
        }

        for (int i = 0; i < dominoes.size(); i++) {
            if (!used[i]) {
                Domino original = dominoes.get(i);
                // Try original orientation
                if (chain.isEmpty() || original.getLeft() == currentEnd) {
                    used[i] = true;
                    chain.add(original);
                    List<Domino> result = backtrack(dominoes, used, chain, original.getRight());
                    if (result != null) {
                        return result;
                    }
                    chain.remove(chain.size() - 1);
                    used[i] = false;
                }
                // Try reversed orientation
                Domino reversed = new Domino(original.getRight(), original.getLeft());
                if (chain.isEmpty() || reversed.getLeft() == currentEnd) {
                    used[i] = true;
                    chain.add(reversed);
                    List<Domino> result = backtrack(dominoes, used, chain, reversed.getRight());
                    if (result != null) {
                        return result;
                    }
                    chain.remove(chain.size() - 1);
                    used[i] = false;
                }
            }
        }
        return null;
    }

}
