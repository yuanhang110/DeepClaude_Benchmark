class TwoBucket {
    private int totalMoves;
    private String finalBucket;
    private int otherBucketLiters;

    private static class State {
        final int a;
        final int b;
        final int moves;

        State(int a, int b, int moves) {
            this.a = a;
            this.b = b;
            this.moves = moves;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return a == state.a && b == state.b && moves == state.moves;
        }

        @Override
        public int hashCode() {
            return java.util.Objects.hash(a, b, moves);
        }
    }

    TwoBucket(int bucketOneCap, int bucketTwoCap, int desiredLiters, String startBucket) {
        // Check initial conditions
        if (startBucket.equals("one")) {
            // First action: fill bucket one
            int a = bucketOneCap;
            int b = 0;
            
            // Check if we've already reached the goal
            if (a == desiredLiters) {
                totalMoves = 1;
                finalBucket = "one";
                otherBucketLiters = b;
                return;
            }
        } else { // startBucket equals "two"
            // First action: fill bucket two
            int a = 0;
            int b = bucketTwoCap;
            
            // Check if we've already reached the goal
            if (b == desiredLiters) {
                totalMoves = 1;
                finalBucket = "two";
                otherBucketLiters = a;
                return;
            }
        }

        // BFS to find solution
        java.util.Queue<State> queue = new java.util.LinkedList<>();
        java.util.Set<State> visited = new java.util.HashSet<>();

        // Initialize with starting state
        State initialState = startBucket.equals("one") 
            ? new State(bucketOneCap, 0, 1) 
            : new State(0, bucketTwoCap, 1);
        
        queue.add(initialState);
        visited.add(initialState);

        while (!queue.isEmpty()) {
            State current = queue.poll();
            
            // Check if current state has the desired liters in either bucket
            if (current.a == desiredLiters) {
                totalMoves = current.moves;
                finalBucket = "one";
                otherBucketLiters = current.b;
                return;
            } else if (current.b == desiredLiters) {
                totalMoves = current.moves;
                finalBucket = "two";
                otherBucketLiters = current.a;
                return;
            }

            // Generate all possible next states
            java.util.List<State> nextStates = generateNextStates(
                current, bucketOneCap, bucketTwoCap, startBucket);
            
            for (State next : nextStates) {
                if (!visited.contains(next)) {
                    visited.add(next);
                    queue.add(next);
                }
            }
        }
        
        throw new IllegalArgumentException("No solution exists");
    }

    private java.util.List<State> generateNextStates(
            State current, int maxA, int maxB, String startBucket) {
        
        java.util.List<State> nextStates = new java.util.ArrayList<>();
        int a = current.a;
        int b = current.b;
        int moves = current.moves + 1;

        // 1. Pour from bucket one to bucket two
        int pourAmount = Math.min(a, maxB - b);
        if (pourAmount > 0) {
            State newState = new State(a - pourAmount, b + pourAmount, moves);
            if (!isForbidden(newState, startBucket, maxA, maxB)) {
                nextStates.add(newState);
            }
        }

        // 2. Pour from bucket two to bucket one
        pourAmount = Math.min(b, maxA - a);
        if (pourAmount > 0) {
            State newState = new State(a + pourAmount, b - pourAmount, moves);
            if (!isForbidden(newState, startBucket, maxA, maxB)) {
                nextStates.add(newState);
            }
        }

        // 3. Empty bucket one
        if (a > 0) {
            State newState = new State(0, b, moves);
            if (!isForbidden(newState, startBucket, maxA, maxB)) {
                nextStates.add(newState);
            }
        }

        // 4. Empty bucket two
        if (b > 0) {
            State newState = new State(a, 0, moves);
            if (!isForbidden(newState, startBucket, maxA, maxB)) {
                nextStates.add(newState);
            }
        }

        // 5. Fill bucket one
        if (a < maxA) {
            State newState = new State(maxA, b, moves);
            if (!isForbidden(newState, startBucket, maxA, maxB)) {
                nextStates.add(newState);
            }
        }

        // 6. Fill bucket two
        if (b < maxB) {
            State newState = new State(a, maxB, moves);
            if (!isForbidden(newState, startBucket, maxA, maxB)) {
                nextStates.add(newState);
            }
        }

        return nextStates;
    }

    private boolean isForbidden(State state, String startBucket, int maxA, int maxB) {
        // Rule: After an action, you may not arrive at a state where the initial
        // starting bucket is empty and the other bucket is full
        if (startBucket.equals("one")) {
            return state.a == 0 && state.b == maxB;
        } else {
            return state.b == 0 && state.a == maxA;
        }
    }

    int getTotalMoves() {
        return totalMoves;
    }

    String getFinalBucket() {
        return finalBucket;
    }

    int getOtherBucket() {
        return otherBucketLiters;
    }
}
