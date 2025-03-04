import java.util.*;

class TwoBucket {
    private int moves;
    private String finalBucket;
    private int otherBucket;
    
    TwoBucket(int bucketOneCap, int bucketTwoCap, int desiredLiters, String startBucket) {
        // Validate inputs
        if (bucketOneCap <= 0 || bucketTwoCap <= 0 || desiredLiters <= 0) {
            throw new IllegalArgumentException("Bucket sizes and desired liters must be positive");
        }
        if (!startBucket.equals("one") && !startBucket.equals("two")) {
            throw new IllegalArgumentException("startBucket must be either 'one' or 'two'");
        }
        if (desiredLiters > Math.max(bucketOneCap, bucketTwoCap)) {
            throw new IllegalArgumentException("Desired liters cannot be greater than largest bucket");
        }
        
        try {
            if (startBucket.equals("one")) {
                solve(bucketOneCap, bucketTwoCap, desiredLiters, true);
            } else {
                solve(bucketTwoCap, bucketOneCap, desiredLiters, false);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Error solving bucket puzzle", e);
        }
    }
    
    private void solve(int bucketACap, int bucketBCap, int desiredLiters, boolean startWithA) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        // Initial state based on starting bucket
        State initialState = startWithA ? 
            new State(bucketACap, 0, 1) : 
            new State(0, bucketBCap, 1);
        queue.add(initialState);
        visited.add(initialState.toString());
        
        while (!queue.isEmpty()) {
            State current = queue.poll();
            
            // Check if we've reached the desired liters
            if (current.a == desiredLiters) {
                this.moves = current.moves;
                this.finalBucket = startWithA ? "one" : "two";
                this.otherBucket = current.b;
                return;
            }
            if (current.b == desiredLiters) {
                this.moves = current.moves;
                this.finalBucket = startWithA ? "two" : "one";
                this.otherBucket = current.a;
                return;
            }
            
            // Generate all possible next states
            List<State> nextStates = new ArrayList<>();
            
            // 1. Fill A
            nextStates.add(new State(bucketACap, current.b, current.moves + 1));
            
            // 2. Fill B
            nextStates.add(new State(current.a, bucketBCap, current.moves + 1));
            
            // 3. Empty A
            nextStates.add(new State(0, current.b, current.moves + 1));
            
            // 4. Empty B
            nextStates.add(new State(current.a, 0, current.moves + 1));
            
            // 5. Pour A -> B
            int pourAmount = Math.min(current.a, bucketBCap - current.b);
            nextStates.add(new State(current.a - pourAmount, current.b + pourAmount, current.moves + 1));
            
            // 6. Pour B -> A
            pourAmount = Math.min(current.b, bucketACap - current.a);
            nextStates.add(new State(current.a + pourAmount, current.b - pourAmount, current.moves + 1));
            
            // Add valid states to queue
            for (State state : nextStates) {
                // Check the special rule
                if (startWithA && state.a == 0 && state.b == bucketBCap) continue;
                if (!startWithA && state.b == 0 && state.a == bucketACap) continue;
                
                if (!visited.contains(state.toString())) {
                    visited.add(state.toString());
                    queue.add(state);
                }
            }
        }
        
        throw new IllegalStateException("No solution exists for the given parameters");
    }
    
    int getTotalMoves() {
        return moves;
    }

    String getFinalBucket() {
        return finalBucket;
    }

    int getOtherBucket() {
        return otherBucket;
    }
    
    private class State {
        int a;
        int b;
        int moves;
        
        State(int a, int b, int moves) {
            this.a = a;
            this.b = b;
            this.moves = moves;
        }
        
        @Override
        public String toString() {
            return a + "," + b;
        }
    }
}
