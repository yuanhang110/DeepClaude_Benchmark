class Connect {
    private String[] board;
    private int rows;
    private int cols;

    public Connect(String[] board) {
        this.board = board;
        this.rows = board.length;
        // Calculate columns by counting non-space characters in first row
        this.cols = 0;
        if (rows > 0) {
            for (int i = 0; i < board[0].length(); i++) {
                if (board[0].charAt(i) != ' ') {
                    cols++;
                }
            }
        }
    }

    public Winner computeWinner() {
        // Check if O has connected top to bottom
        if (hasPath('O', true)) {
            return Winner.PLAYER_O;
        }
        
        // Check if X has connected left to right
        if (hasPath('X', false)) {
            return Winner.PLAYER_X;
        }
        
        return Winner.NONE;
    }

    private boolean hasPath(char player, boolean vertical) {
        boolean[][] visited = new boolean[rows][cols];
        
        // Initialize starting positions
        for (int i = 0; i < (vertical ? cols : rows); i++) {
            int row = vertical ? 0 : i;
            int col = vertical ? i : 0;
            
            if (getCell(row, col) == player && 
                dfs(row, col, player, visited, vertical)) {
                return true;
            }
        }
        return false;
    }

    private boolean dfs(int row, int col, char player, boolean[][] visited, boolean vertical) {
        // Check if we've reached the opposite side
        if (vertical && row == rows - 1) return true;
        if (!vertical && col == cols - 1) return true;

        visited[row][col] = true;

        // Hex neighbors: right, left, down-right, down-left, up-right, up-left
        int[][] directions = {
            {0, 1}, {0, -1},
            {1, 0}, {1, 1},
            {-1, 0}, {-1, -1}
        };

        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];

            if (isValid(newRow, newCol) && 
                !visited[newRow][newCol] && 
                getCell(newRow, newCol) == player) {
                if (dfs(newRow, newCol, player, visited, vertical)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }

    private char getCell(int row, int col) {
        // Skip spaces in the board representation
        String line = board[row];
        int actualCol = 0;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c != ' ') {
                if (actualCol == col) {
                    return c;
                }
                actualCol++;
            }
        }
        return ' '; // Invalid cell
    }

    private boolean isValid(int row, int col) {
        if (row < 0 || row >= rows || col < 0) return false;
        // Calculate actual columns considering spaces
        if (rows == 0) return false;
        String line = board[row];
        int actualCols = 0;
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) != ' ') {
                actualCols++;
            }
        }
        return col < actualCols;
    }
}

enum Winner {
    PLAYER_X,
    PLAYER_O,
    NONE
}
