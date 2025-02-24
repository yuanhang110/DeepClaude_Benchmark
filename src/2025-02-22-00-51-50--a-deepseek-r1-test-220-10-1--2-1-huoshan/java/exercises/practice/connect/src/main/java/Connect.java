import java.util.LinkedList;
import java.util.Queue;

class Connect {
    private char[][] board;
    private int height;
    private int width;

    public Connect(String[] boardRows) {
        parseBoard(boardRows);
    }

    private void parseBoard(String[] boardRows) {
        height = boardRows.length;
        if (height == 0) {
            board = new char[0][0];
            width = 0;
            return;
        }
        
        board = new char[height][];
        for (int i = 0; i < height; i++) {
            String row = boardRows[i].trim();
            if (row.isEmpty()) {
                board[i] = new char[0];
                continue;
            }
            String[] cells = row.split(" +");
            board[i] = new char[cells.length];
            for (int j = 0; j < cells.length; j++) {
                board[i][j] = cells[j].charAt(0);
            }
        }
        width = (height > 0) ? board[0].length : 0;
    }

    public Winner computeWinner() {
        if (hasOWon()) {
            return Winner.O;
        } else if (hasXWon()) {
            return Winner.X;
        } else {
            return Winner.NONE;
        }
    }

    private boolean hasOWon() {
        if (height == 0 || width == 0) return false;
        boolean[][] visited = new boolean[height][width];
        Queue<int[]> queue = new LinkedList<>();

        // Initialize with all top-row 'O's
        for (int col = 0; col < width; col++) {
            if (board[0][col] == 'O') {
                queue.add(new int[]{0, col});
                visited[0][col] = true;
            }
        }

        int[][] directions = {{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int row = cell[0];

            if (row == height - 1) {
                return true;
            }

            for (int[] dir : directions) {
                int newRow = cell[0] + dir[0];
                int newCol = cell[1] + dir[1];

                if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < board[newRow].length
                        && !visited[newRow][newCol] && board[newRow][newCol] == 'O') {
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }
        return false;
    }

    private boolean hasXWon() {
        if (height == 0 || width == 0) return false;
        boolean[][] visited = new boolean[height][width];
        Queue<int[]> queue = new LinkedList<>();

        // Initialize with all left-column 'X's
        for (int row = 0; row < height; row++) {
            if (board[row].length > 0 && board[row][0] == 'X') {
                queue.add(new int[]{row, 0});
                visited[row][0] = true;
            }
        }

        int[][] directions = {{-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}};
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int col = cell[1];

            if (col == width - 1) {
                return true;
            }

            for (int[] dir : directions) {
                int newRow = cell[0] + dir[0];
                int newCol = cell[1] + dir[1];

                if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < board[newRow].length
                        && !visited[newRow][newCol] && board[newRow][newCol] == 'X') {
                    visited[newRow][newCol] = true;
                    queue.add(new int[]{newRow, newCol});
                }
            }
        }
        return false;
    }

    public enum Winner {
        O, X, NONE
    }
}
