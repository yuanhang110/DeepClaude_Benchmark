import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

class WordSearcher {
    private static final int[][] DIRECTIONS = {
        {0, 1},  // Right
        {1, 1},  // Down-right
        {1, 0},  // Down
        {1, -1}, // Down-left
        {0, -1}, // Left
        {-1, -1},// Up-left
        {-1, 0}, // Up
        {-1, 1}  // Up-right
    };

    Map<String, Optional<WordLocation>> search(final Set<String> words, final char[][] grid) {
        Map<String, Optional<WordLocation>> result = new HashMap<>();
        
        for (String word : words) {
            if (word.isEmpty()) {
                result.put(word, Optional.empty());
                continue;
            }
            
            boolean found = false;
            for (int r = 0; r < grid.length; r++) {
                for (int c = 0; c < grid[r].length; c++) {
                    if (grid[r][c] != word.charAt(0)) {
                        continue;
                    }
                    
                    for (int[] dir : DIRECTIONS) {
                        int dr = dir[0];
                        int dc = dir[1];
                        
                        int endR = r + dr * (word.length() - 1);
                        int endC = c + dc * (word.length() - 1);
                        
                        if (endR < 0 || endR >= grid.length || endC < 0 || endC >= grid[endR].length) {
                            continue;
                        }
                        
                        boolean match = true;
                        for (int i = 0; i < word.length(); i++) {
                            int currR = r + dr * i;
                            int currC = c + dc * i;
                            
                            if (currR < 0 || currR >= grid.length || currC < 0 || currC >= grid[currR].length) {
                                match = false;
                                break;
                            }
                            
                            if (grid[currR][currC] != word.charAt(i)) {
                                match = false;
                                break;
                            }
                        }
                        
                        if (match) {
                            Pair start = new Pair(c + 1, r + 1);
                            Pair end = new Pair(endC + 1, endR + 1);
                            result.put(word, Optional.of(new WordLocation(start, end)));
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
                if (found) break;
            }
            
            if (!found) {
                result.put(word, Optional.empty());
            }
        }
        
        return result;
    }
    
    private static class Pair {
        private final int x;
        private final int y;
        
        Pair(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    
    private static class WordLocation {
        private final Pair start;
        private final Pair end;
        
        WordLocation(Pair start, Pair end) {
            this.start = start;
            this.end = end;
        }
    }
}
