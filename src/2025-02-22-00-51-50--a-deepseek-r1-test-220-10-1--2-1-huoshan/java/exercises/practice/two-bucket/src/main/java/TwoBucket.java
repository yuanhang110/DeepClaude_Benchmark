class TwoBucket {
    private int bucketOneCap;
    private int bucketTwoCap;
    private int desiredLiters;
    private String startBucket;
    private int moves;
    private String finalBucket;
    private int otherLiters;

    public TwoBucket(int bucketOneCap, int bucketTwoCap, int desiredLiters, String startBucket) {
        this.bucketOneCap = bucketOneCap;
        this.bucketTwoCap = bucketTwoCap;
        this.desiredLiters = desiredLiters;
        this.startBucket = startBucket;
        this.moves = 0;
        this.finalBucket = "";
        this.otherLiters = 0;
        
        solve();
    }

    private void solve() {
        // Check if initial fill meets the desired liters
        int initialB1 = startBucket.equals("one") ? bucketOneCap : 0;
        int initialB2 = startBucket.equals("two") ? bucketTwoCap : 0;
        moves = 1;
        
        if (initialB1 == desiredLiters) {
            finalBucket = "one";
            otherLiters = initialB2;
            return;
        } else if (initialB2 == desiredLiters) {
            finalBucket = "two";
            otherLiters = initialB1;
            return;
        }
        
        java.util.Queue<int[]> queue = new java.util.ArrayDeque<>();
        java.util.Set<String> visited = new java.util.HashSet<>();
        queue.add(new int[]{initialB1, initialB2, 1});
        visited.add(initialB1 + "," + initialB2);
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int b1 = current[0];
            int b2 = current[1];
            int currentMoves = current[2];
            
            java.util.List<int[]> nextStates = new java.util.ArrayList<>();
            
            // Generate all possible next states
            nextStates.add(new int[]{0, b2, currentMoves + 1}); // Empty b1
            nextStates.add(new int[]{b1, 0, currentMoves + 1}); // Empty b2
            nextStates.add(new int[]{bucketOneCap, b2, currentMoves + 1}); // Fill b1
            nextStates.add(new int[]{b1, bucketTwoCap, currentMoves + 1}); // Fill b2
            
            // Pour b1 to b2
            int pourAmount = Math.min(b1, bucketTwoCap - b2);
            nextStates.add(new int[]{b1 - pourAmount, b2 + pourAmount, currentMoves + 1});
            
            // Pour b2 to b1
            pourAmount = Math.min(b2, bucketOneCap - b1);
            nextStates.add(new int[]{b1 + pourAmount, b2 - pourAmount, currentMoves + 1});
            
            for (int[] state : nextStates) {
                int newB1 = state[0];
                int newB2 = state[1];
                int newMoves = state[2];
                String stateKey = newB1 + "," + newB2;
                
                // Check if state is invalid (starting bucket empty and other full)
                boolean invalid = false;
                if (startBucket.equals("one")) {
                    if (newB1 == 0 && newB2 == bucketTwoCap) invalid = true;
                } else {
                    if (newB2 == 0 && newB1 == bucketOneCap) invalid = true;
                }
                
                if (invalid || visited.contains(stateKey)) continue;
                
                // Check if desired liters found
                if (newB1 == desiredLiters) {
                    moves = newMoves;
                    finalBucket = "one";
                    otherLiters = newB2;
                    return;
                } else if (newB2 == desiredLiters) {
                    moves = newMoves;
                    finalBucket = "two";
                    otherLiters = newB1;
                    return;
                }
                
                visited.add(stateKey);
                queue.add(state);
            }
        }
        
        throw new IllegalArgumentException("No solution possible");
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
}
