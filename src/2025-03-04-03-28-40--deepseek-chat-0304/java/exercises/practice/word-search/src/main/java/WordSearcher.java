import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashMap;

class WordLocation {
    private final Pair start;
    private final Pair end;

    WordLocation(Pair start, Pair end) {
        this.start = start;
        this.end = end;
    }

    public Pair getStart() {
        return start;
    }

    public Pair getEnd() {
        return end;
    }
}

class Pair {
    private final int x;
    private final int y;

    Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

class WordSearcher {
    private static final int[][] DIRECTIONS = {
        {1, 0},   // Right
        {0, 1},   // Down
        {1, 1},   // Down-right
        {1, -1},  // Down-left
        {-1, 0},  // Left
        {0, -1},  // Up
        {-1, -1}, // Up-left
        {-1, 1}   // Up-right
    };

    Map<String, Optional<WordLocation>> search(final Set<String> words, final char[][] grid) {
        if (words == null || grid == null) {
            throw new IllegalArgumentException("Input parameters cannot be null");
        }
        
        Map<String, Optional<WordLocation>> result = new HashMap<>();
        
        if (grid.length == 0 || grid[0].length == 0) {
            for (String word : words) {
                result.put(word, Optional.empty());
            }
            return result;
        }
        
        for (String word : words) {
            if (word == null || word.isEmpty()) {
                result.put(word, Optional.empty());
                continue;
            }
            Optional<WordLocation> location = findWord(word, grid);
            result.put(word, location);
        }
        
        return result;
    }

    private Optional<WordLocation> findWord(String word, char[][] grid) {
        if (word == null || grid == null) {
            return Optional.empty();
        }
        
        int rows = grid.length;
        int cols = rows > 0 ? grid[0].length : 0;
        
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (grid[row][col] == word.charAt(0)) {
                    for (int[] direction : DIRECTIONS) {
                        Optional<WordLocation> location = checkDirection(word, grid, row, col, direction);
                        if (location.isPresent()) {
                            return location;
                        }
                    }
                }
            }
        }
        
        return Optional.empty();
    }

    private Optional<WordLocation> checkDirection(String word, char[][] grid, int row, int col, int[] direction) {
        if (word == null || grid == null || direction == null || direction.length < 2) {
            return Optional.empty();
        }
        
        int dx = direction[0];
        int dy = direction[1];
        int currentRow = row;
        int currentCol = col;
        
        for (int i = 1; i < word.length(); i++) {
            currentRow += dy;
            currentCol += dx;
            
            if (currentRow < 0 || currentRow >= grid.length || 
                currentCol < 0 || currentCol >= grid[0].length || 
                grid[currentRow][currentCol] != word.charAt(i)) {
                return Optional.empty();
            }
        }
        
        Pair start = new Pair(col + 1, row + 1);
        Pair end = new Pair(currentCol + 1, currentRow + 1);
        return Optional.of(new WordLocation(start, end));
    }
}
