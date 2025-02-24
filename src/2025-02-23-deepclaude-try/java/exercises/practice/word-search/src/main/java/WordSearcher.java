import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.HashMap;

class WordSearcher {
    private static final int[][] DIRECTIONS = {
        {0, 1},   // Right
        {0, -1},  // Left
        {1, 0},   // Down
        {-1, 0},  // Up
        {1, 1},   // Down-Right
        {1, -1},  // Down-Left
        {-1, 1},  // Up-Right
        {-1, -1}  // Up-Left
    };

    Map<String, Optional<WordLocation>> search(final Set<String> words, final char[][] grid) {
        if (grid == null || grid.length == 0) {
            return results; // Early return for empty grid
        }

        Map<String, Optional<WordLocation>> results = new HashMap<>();
        
        for (String word : words) {
            results.put(word, Optional.empty());
            char[] letters = word.toCharArray();
            
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    if (grid[row][col] == letters[0]) {
                        for (int[] dir : DIRECTIONS) {
                            int endRow = row + (letters.length - 1) * dir[0];
                            int endCol = col + (letters.length - 1) * dir[1];
                            
                            if (endRow < 0 || endRow >= grid.length || endCol < 0 || endCol >= grid[endRow].length) {
                                continue;
                            }
                            
                            boolean found = true;
                            for (int i = 0; i < letters.length; i++) {
                                int currentRow = row + i * dir[0];
                                int currentCol = col + i * dir[1];
                                // Add bounds check for currentRow and currentCol
                                if (currentRow < 0 || currentRow >= grid.length || currentCol < 0 || currentCol >= grid[currentRow].length || grid[currentRow][currentCol] != letters[i]) {
                                    found = false;
                                    break;
                                }
                            }
                            
                            if (found) {
                                Position start = new Position(row + 1, col + 1);
                                Position end = new Position(endRow + 1, endCol + 1);
                                results.put(word, Optional.of(new WordLocation(start, end)));
                                // Clear inner loops since we found the word
                                row = grid.length; // This will exit the outer loop
                                col = grid[row - 1].length; // This will also exit the inner loop safely
                                break;
                            }
                        }
                    }
                }
            }
        }
        
        return results;
    }

    private static class Position {
        final int row;
        final int col;

        Position(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private static class WordLocation {
        final Position start;
        final Position end;

        WordLocation(Position start, Position end) {
            this.start = start;
            this.end = end;
        }
    }
}
