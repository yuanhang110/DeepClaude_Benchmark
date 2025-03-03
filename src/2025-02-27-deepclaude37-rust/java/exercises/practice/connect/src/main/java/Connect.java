import java.util.List;
import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;

class Connect {
    public enum Winner {
        PLAYER_O, PLAYER_X, NONE
    }

    private char[][] board;
    private int rows;
    private int cols;

    public Connect(String[] board) {
        parseBoard(board);
    }

    private void parseBoard(String[] input) {
        List<List<Character>> grid = new ArrayList<>();
        for (String s : input) {
            String trimmed = s.trim();
            List<Character> row = new ArrayList<>();
            if (!trimmed.isEmpty()) {
                String[] parts = trimmed.split(" +");
                for (String part : parts) {
                    if (!part.isEmpty()) {
                        row.add(part.charAt(0));
                    }
                }
            }
            grid.add(row);
        }

        rows = grid.size();
        cols = rows > 0 ? grid.get(0).size() : 0;
        this.board = new char[rows][cols];
        for (int i = 0; i < rows; i++) {
            List<Character> rowData = grid.get(i);
            for (int j = 0; j < cols; j++) {
                if (j < rowData.size()) {
                    board[i][j] = rowData.get(j);
                } else {
                    board[i][j] = '.';
                }
            }
        }
    }

    private boolean hasOPath() {
        if (rows == 0 || cols == 0) return false;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();

        for (int c = 0; c < cols; c++) {
            if (board[0][c] == 'O') {
                queue.add(new int[]{0, c});
                visited[0][c] = true;
            }
        }

        int[][] directions = {{-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];

            if (r == rows - 1) {
                return true;
            }

            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc]) {
                    if (board[nr][nc] == 'O') {
                        visited[nr][nc] = true;
                        queue.add(new int[]{nr, nc});
                    }
                }
            }
        }

        return false;
    }

    private boolean hasXPath() {
        if (rows == 0 || cols == 0) return false;
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();

        for (int r = 0; r < rows; r++) {
            if (board[r][0] == 'X') {
                queue.add(new int[]{r, 0});
                visited[r][0] = true;
            }
        }

        int[][] directions = {{-1,0}, {-1,1}, {0,-1}, {0,1}, {1,-1}, {1,0}};

        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int r = current[0];
            int c = current[1];

            if (c == cols - 1) {
                return true;
            }

            for (int[] dir : directions) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !visited[nr][nc]) {
                    if (board[nr][nc] == 'X') {
                        visited[nr][nc] = true;
                        queue.add(new int[]{nr, nc});
                    }
                }
            }
        }

        return false;
    }

    public Winner computeWinner() {
        if (hasOPath()) {
            return Winner.PLAYER_O;
        } else if (hasXPath()) {
            return Winner.PLAYER_X;
        } else {
            return Winner.NONE;
        }
    }
}
