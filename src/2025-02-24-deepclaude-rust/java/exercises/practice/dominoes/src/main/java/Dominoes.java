import java.util.List;
import java.util.ArrayList;

class Dominoes {

    List<Domino> formChain(List<Domino> inputDominoes) throws ChainNotFoundException {
        if (inputDominoes.isEmpty()) {
            return new ArrayList<>();
        }

        List<Domino> result = new ArrayList<>();
        boolean[] used = new boolean[inputDominoes.size()];

        if (inputDominoes.size() == 1) {
            Domino domino = inputDominoes.get(0);
            if (domino.getLeft() != domino.getRight()) {
                throw new ChainNotFoundException("No chain found.");
            }
            return new ArrayList<>(inputDominoes);
        }

        for (int i = 0; i < inputDominoes.size(); i++) {
            Domino first = inputDominoes.get(i);
            // Try original orientation
            used[i] = true;
            result.add(first);
            if (backtrack(inputDominoes, used, result, first.getRight(), first.getLeft())) {
                return result;
            }
            result.remove(result.size() - 1);
            used[i] = false;

            // Try flipped orientation
            Domino flipped = new Domino(first.getRight(), first.getLeft());
            used[i] = true;
            result.add(flipped);
            if (backtrack(inputDominoes, used, result, flipped.getRight(), flipped.getLeft())) {
                return result;
            }
            result.remove(result.size() - 1);
            used[i] = false;
        }

        throw new ChainNotFoundException("No chain found.");
    }

    private boolean backtrack(List<Domino> dominoes, boolean[] used, List<Domino> current, int lastRight, int firstLeft) {
        if (current.size() == dominoes.size()) {
            return lastRight == firstLeft;
        }

        for (int i = 0; i < dominoes.size(); i++) {
            if (!used[i]) {
                Domino dom = dominoes.get(i);

                // Try original orientation
                if (dom.getLeft() == lastRight) {
                    used[i] = true;
                    current.add(dom);
                    if (backtrack(dominoes, used, current, dom.getRight(), firstLeft)) {
                        return true;
                    }
                    current.remove(current.size() - 1);
                    used[i] = false;
                }

                // Try flipped orientation
                if (dom.getRight() == lastRight) {
                    used[i] = true;
                    Domino flipped = new Domino(dom.getRight(), dom.getLeft());
                    current.add(flipped);
                    if (backtrack(dominoes, used, current, flipped.getRight(), firstLeft)) {
                        return true;
                    }
                    current.remove(current.size() - 1);
                    used[i] = false;
                }
            }
        }

        return false;
    }

}
