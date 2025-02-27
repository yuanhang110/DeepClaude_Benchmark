import java.util.Queue;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;

class TwoBucket {
    private int moves;
    private String finalBucket;
    private int otherLiters;

    TwoBucket(int bucketOneCap, int bucketTwoCap, int desiredLiters, String startBucket) {
        if (!startBucket.equals("one") && !startBucket.equals("two")) {
            throw new IllegalArgumentException("Invalid start bucket");
        }

        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        int aInitial = startBucket.equals("one") ? bucketOneCap : 0;
        int bInitial = startBucket.equals("two") ? bucketTwoCap : 0;
        queue.add(new State(aInitial, bInitial, 1));
        visited.add(aInitial + "," + bInitial);

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if (current.a == desiredLiters) {
                this.moves = current.moves;
                this.finalBucket = "one";
                this.otherLiters = current.b;
                return;
            } else if (current.b == desiredLiters) {
                this.moves = current.moves;
                this.finalBucket = "two";
                this.otherLiters = current.a;
                return;
            }

            List<State> nextStates = new ArrayList<>();

            // Generate all possible next states
            nextStates.add(new State(bucketOneCap, current.b, current.moves + 1)); // Fill bucket one
            nextStates.add(new State(current.a, bucketTwoCap, current.moves + 1)); // Fill bucket two
            nextStates.add(new State(0, current.b, current.moves + 1)); // Empty bucket one
            nextStates.add(new State(current.a, 0, current.moves + 1)); // Empty bucket two

            // Pour from one to two
            int pourAmount = Math.min(current.a, bucketTwoCap - current.b);
            nextStates.add(new State(current.a - pourAmount, current.b + pourAmount, current.moves + 1));

            // Pour from two to one
            pourAmount = Math.min(current.b, bucketOneCap - current.a);
            nextStates.add(new State(current.a + pourAmount, current.b - pourAmount, current.moves + 1));

            for (State next : nextStates) {
                String key = next.a + "," + next.b;
                boolean isStartingBucketEmpty = startBucket.equals("one") ? next.a == 0 : next.b == 0;
                boolean isOtherBucketFull = startBucket.equals("one") ? next.b == bucketTwoCap : next.a == bucketOneCap;
                
                if (!isStartingBucketEmpty || !isOtherBucketFull) {
                    String key = next.a + "," + next.b;
                    if (!visited.contains(key)) {
                        visited.add(key);
                        queue.add(next);
                    }
                }
            }
        }

        throw new IllegalArgumentException("No solution exists");
    }

    int getTotalMoves() {
        return moves;
    }

    String getFinalBucket() {
        return finalBucket;
    }

    int getOtherBucket() {
        return otherLiters;
    }

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
}
