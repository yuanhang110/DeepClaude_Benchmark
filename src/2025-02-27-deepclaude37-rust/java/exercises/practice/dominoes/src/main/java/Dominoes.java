import java.util.List;
import java.util.ArrayList;

class Dominoes {
    static class DominoState {
        int index;
        boolean flipped;
        
        DominoState(int index, boolean flipped) {
            this.index = index;
            this.flipped = flipped;
        }
    }

    List<Domino> formChain(List<Domino> inputDominoes) throws ChainNotFoundException {
        if (inputDominoes.isEmpty()) {
            return new ArrayList<>();
        }
        if (inputDominoes.size() == 1) {
            Domino domino = inputDominoes.get(0);
            if (domino.getLeft() != domino.getRight()) {
                throw new ChainNotFoundException("No valid chain found.");
            }
            return new ArrayList<>(inputDominoes);
        }

        boolean[] used = new boolean[inputDominoes.size()];
        List<DominoState> current = new ArrayList<>();
        List<DominoState> result = new ArrayList<>();

        // Try each domino as the first one
        for (int i = 0; i < inputDominoes.size(); i++) {
            Domino first = inputDominoes.get(i);
            used[i] = true;

            // Try original orientation
            current.clear();
            current.add(new DominoState(i, false));
            if (backtrack(inputDominoes, used, getValue(first, false, true), 1, current, result)) {
                return buildChain(inputDominoes, result);
            }

            // Try flipped orientation
            current.clear();
            current.add(new DominoState(i, true));
            if (backtrack(inputDominoes, used, getValue(first, true, true), 1, current, result)) {
                return buildChain(inputDominoes, result);
            }

            used[i] = false;
        }

        throw new ChainNotFoundException("No valid chain found.");
    }

    private boolean backtrack(List<Domino> dominoes, boolean[] used, int prevRight, int count,
                            List<DominoState> current, List<DominoState> result) {
        if (count == dominoes.size()) {
            DominoState first = current.get(0);
            DominoState last = current.get(current.size() - 1);
            Domino firstDomino = dominoes.get(first.index);
            Domino lastDomino = dominoes.get(last.index);
            
            int firstLeft = getValue(firstDomino, first.flipped, false);
            int lastRight = getValue(lastDomino, last.flipped, true);
            
            if (firstLeft == lastRight) {
                result.clear();
                result.addAll(current);
                return true;
            }
            return false;
        }

        for (int i = 0; i < dominoes.size(); i++) {
            if (!used[i]) {
                Domino domino = dominoes.get(i);

                // Try original orientation
                int left = getValue(domino, false, false);
                if (left == prevRight) {
                    used[i] = true;
                    current.add(new DominoState(i, false));
                    if (backtrack(dominoes, used, getValue(domino, false, true), count + 1, current, result)) {
                        return true;
                    }
                    current.remove(current.size() - 1);
                    used[i] = false;
                }

                // Try flipped orientation
                left = getValue(domino, true, false);
                if (left == prevRight) {
                    used[i] = true;
                    current.add(new DominoState(i, true));
                    if (backtrack(dominoes, used, getValue(domino, true, true), count + 1, current, result)) {
                        return true;
                    }
                    current.remove(current.size() - 1);
                    used[i] = false;
                }
            }
        }
        return false;
    }

    private int getValue(Domino domino, boolean flipped, boolean right) {
        if (!flipped) {
            return right ? domino.getRight() : domino.getLeft();
        } else {
            return right ? domino.getLeft() : domino.getRight();
        }
    }

    private List<Domino> buildChain(List<Domino> dominoes, List<DominoState> states) {
        List<Domino> result = new ArrayList<>();
        for (DominoState state : states) {
            Domino original = dominoes.get(state.index);
            result.add(state.flipped ? 
                new Domino(original.getRight(), original.getLeft()) :
                original);
        }
        return result;
    }
}
