import java.util.Queue;
import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Set;

class TwoBucket {
    private int totalMoves;
    private String finalBucket;
    private int otherLiters;

    private static class State {
        int a;
        int b;
        int moves;

        State(int a, int b, int moves) {
            this.a = a;
            this.b = b;
            this.moves = moves;
        }
    }

    TwoBucket(int bucketOneCap, int bucketTwoCap, int desiredLiters, String startBucket) {
        if (desiredLiters == 0) {
            throw new IllegalArgumentException("Desired liters cannot be zero");
        }

        Queue<State> queue = new ArrayDeque<>();
        Set<String> visited = new HashSet<>();

        int initialA = startBucket.equals("one") ? bucketOneCap : 0;
        int initialB = startBucket.equals("two") ? bucketTwoCap : 0;
        queue.add(new State(initialA, initialB, 1));
        visited.add(initialA + "," + initialB);

        boolean found = false;
        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.a == desiredLiters || current.b == desiredLiters) {
                if (current.a == desiredLiters) {
                    finalBucket = "one";
                    otherLiters = current.b;
                } else {
                    finalBucket = "two";
                    otherLiters = current.a;
                }
                totalMoves = current.moves;
                found = true;
                break;
            }

            // Generate all possible moves
            generateStates(current, bucketOneCap, bucketTwoCap, startBucket, queue, visited);
        }

        if (!found) {
            throw new IllegalArgumentException("No solution possible");
        }
    }

    private void generateStates(State current, int cap1, int cap2, String startBucket, Queue<State> queue, Set<String> visited) {
        // Fill bucket1
        if (current.a < cap1) {
            processState(new State(cap1, current.b, current.moves + 1), startBucket, cap1, cap2, queue, visited);
        }

        // Fill bucket2
        if (current.b < cap2) {
            processState(new State(current.a, cap2, current.moves + 1), startBucket, cap1, cap2, queue, visited);
        }

        // Empty bucket1
        if (current.a > 0) {
            processState(new State(0, current.b, current.moves + 1), startBucket, cap1, cap2, queue, visited);
        }

        // Empty bucket2
        if (current.b > 0) {
            processState(new State(current.a, 0, current.moves + 1), startBucket, cap1, cap2, queue, visited);
        }

        // Pour bucket1 to bucket2
        int transfer12 = Math.min(current.a, cap2 - current.b);
        if (transfer12 > 0) {
            processState(new State(current.a - transfer12, current.b + transfer12, current.moves + 1), startBucket, cap1, cap2, queue, visited);
        }

        // Pour bucket2 to bucket1
        int transfer21 = Math.min(current.b, cap1 - current.a);
        if (transfer21 > 0) {
            processState(new State(current.a + transfer21, current.b - transfer21, current.moves + 1), startBucket, cap1, cap2, queue, visited);
        }
    }

    private void processState(State newState, String startBucket, int cap1, int cap2, Queue<State> queue, Set<String> visited) {
        // Check invalid state rule
        if ((startBucket.equals("one") && newState.a == 0 && newState.b == cap2) ||
            (startBucket.equals("two") && newState.b == 0 && newState.a == cap1)) {
            return;
        }

        String key = newState.a + "," + newState.b;
        if (!visited.contains(key)) {
            visited.add(key);
            queue.add(newState);
        }
    }

    int getTotalMoves() {
        return totalMoves;
    }

    String getFinalBucket() {
        return finalBucket;
    }

    int getOtherBucket() {
        return otherLiters;
    }
}
