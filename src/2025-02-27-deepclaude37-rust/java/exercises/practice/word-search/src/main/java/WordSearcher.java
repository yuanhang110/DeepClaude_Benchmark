import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

class Pair {
    private final int x;
    private final int y;
    
    Pair(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    
    int getX() {
        return x;
    }
    
    int getY() {
        return y;
    }
}

class WordLocation {
    private final Pair start;
    private final Pair end;
    
    WordLocation(final Pair start, final Pair end) {
        this.start = start;
        this.end = end;
    }
    
    Pair getStart() {
        return start;
    }
    
    Pair getEnd() {
        return end;
    }
}

class WordSearcher {
    // Define all possible search directions
    private static final int[][] DIRECTIONS = {
        {1, 0},   // Right
        {-1, 0},  // Left
        {0, 1},   // Down
        {0, -1},  // Up
        {1, 1},   // Down-Right
        {-1, 1},  // Down-Left
        {1, -1},  // Up-Right
        {-1, -1}  // Up-Left
    };

    Map<String, Optional<WordLocation>> search(final Set<String> words, final char[][] grid) {
        Map<String, Optional<WordLocation>> result = new HashMap<>();
        
        for (String word : words) {
            // Handle empty words
            if (word.isEmpty()) {
                result.put(word, Optional.empty());
                continue;
            }
            
            Optional<WordLocation> location = findWord(word, grid);
            result.put(word, location);
        }
        
        return result;
    }
    
    private Optional<WordLocation> findWord(String word, char[][] grid) {
        int len = word.length();
        
        // Check every position in the grid
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                // Try each direction
                for (int[] dir : DIRECTIONS) {
                    int dx = dir[0];
                    int dy = dir[1];
                    
                    // Check if word matches in this direction
                    if (checkWord(word, grid, x, y, dx, dy)) {
                        // Found! Return location with 1-based indexing
                        Pair start = new Pair(x + 1, y + 1);
                        Pair end = new Pair(x + dx * (len - 1) + 1, y + dy * (len - 1) + 1);
                        return Optional.of(new WordLocation(start, end));
                    }
                }
            }
        }
        
        return Optional.empty();
    }
    
    private boolean checkWord(String word, char[][] grid, int startX, int startY, int dx, int dy) {
        int len = word.length();
        
        for (int i = 0; i < len; i++) {
            int x = startX + dx * i;
            int y = startY + dy * i;
            
            // Check if position is within bounds
            if (y < 0 || y >= grid.length || x < 0 || x >= grid[y].length) {
                return false;
            }
            
            // Check if character matches
            if (grid[y][x] != word.charAt(i)) {
                return false;
            }
        }
        
        return true;
    }
}
