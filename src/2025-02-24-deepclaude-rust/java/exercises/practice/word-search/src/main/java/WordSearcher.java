import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

class WordSearcher {
    Map<String, Optional<WordLocation>> search(final Set<String> words, final char[][] grid) {
        Map<String, Optional<WordLocation>> result = new HashMap<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};
        
        for (String word : words) {
            if (word.isEmpty()) {
                result.put(word, Optional.empty());
                continue;
            }
            
            boolean found = false;
            int wordLength = word.length();
            
            outerloop:
            for (int row = 0; row < grid.length; row++) {
                for (int col = 0; col < grid[row].length; col++) {
                    for (int[] dir : directions) {
                        int dr = dir[0];
                        int dc = dir[1];
                        
                        int endRow = row + dr * (wordLength - 1);
                        int endCol = col + dc * (wordLength - 1);
                        
                        if (endRow < 0 || endRow >= grid.length || endCol < 0 || endCol >= grid[endRow].length) {
                            continue;
                        }
                        
                        boolean match = true;
                        for (int i = 0; i < wordLength; i++) {
                            int currRow = row + dr * i;
                            int currCol = col + dc * i;
                            
                            if (grid[currRow][currCol] != word.charAt(i)) {
                                match = false;
                                break;
                            }
                        }
                        
                        if (match) {
                            Pair start = new Pair(col + 1, row + 1);
                            Pair end = new Pair(endCol + 1, endRow + 1);
                            result.put(word, Optional.of(new WordLocation(start, end)));
                            found = true;
                            break outerloop;
                        }
                    }
                }
            }
            
            if (!found) {
                result.put(word, Optional.empty());
            }
        }
        
        return result;
    }
}
