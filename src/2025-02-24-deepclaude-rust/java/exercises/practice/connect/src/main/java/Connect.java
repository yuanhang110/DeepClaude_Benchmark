import java.util.Queue;
import java.util.LinkedList;

class Connect {
    private char[][] board;
    private int rows;
    private int cols;
    private static final int[][] DIRECTIONS = {
        {-1, 0}, {-1, 1},  // up-left, up-right
        {0, -1}, {0, 1},   // left, right
        {1, -1}, {1, 0}    // down-left, down-right
    };

    public Connect(String[] input) {
        parseBoard(input);
    }

    private void parseBoard(String[] input) {
        rows = input.length;
        if (rows == 0) {
            board = new char[0][0];
            cols = 0;
            return;
        }
        
        // Calculate the board size from first non-empty row
        cols = 0;
        for (String line : input) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty()) {
                cols = trimmed.split(" +").length;
                break;
            }
        }
        
        board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            String trimmed = input[i].trim();
            if (trimmed.isEmpty()) continue;
            
            String[] tokens = trimmed.split(" +");
            for (int j = 0; j < tokens.length; j++) {
                board[i][j] = tokens[j].charAt(0);
            }
        }
    }

    public Winner computeWinner() {
        if (checkWinner('O', true)) {
            return Winner.O;
        } else if (checkWinner('X', false)) {
            return Winner.X;
        }
        return Winner.NONE;
    }

    private boolean checkWinner(char player, boolean verticalWin) {
        Queue<int[]> queue = new LinkedList<>();
        boolean[][] visited = new boolean[rows][cols];

        // Initialize starting positions
        if (verticalWin) { // O - check top to bottom
            for (int c = 0; c < cols; c++) {
                if (board[0][c] == player) {
                    queue.add(new int[]{0, c});
                    visited[0][c] = true;
                }
            }
        } else { // X - check left to right
            for (int r = 0; r < rows; r++) {
                if (board[r][0] == player) {
                    queue.add(new int[]{r, 0});
                    visited[r][0] = true;
                }
            }
        }

        // BFS traversal
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int r = cell[0], c = cell[1];

            // Check if reached target edge
            if (verticalWin && r == rows - 1) return true;
            if (!verticalWin && c == cols - 1) return true;

            for (int[] dir : DIRECTIONS) {
                int nr = r + dir[0];
                int nc = c + dir[1];
                
                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols &&
                    !visited[nr][nc] && board[nr][nc] == player) {
                    visited[nr][nc] = true;
                    queue.add(new int[]{nr, nc});
                }
            }
        }
        return false;
    }
}
