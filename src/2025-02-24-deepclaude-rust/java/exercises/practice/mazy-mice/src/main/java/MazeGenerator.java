import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;

public class MazeGenerator {

    public char[][] generatePerfectMaze(int rows, int columns) {
        return generatePerfectMaze(rows, columns, (int) (System.currentTimeMillis() ^ (System.currentTimeMillis() >>> 32)));
    }

    public char[][] generatePerfectMaze(int rows, int columns, int seed) {
        if (rows < 5 || rows > 100 || columns < 5 || columns > 100) {
            throw new IllegalArgumentException("Rows and columns must be between 5 and 100");
        }

        int gridRows = 2 * rows + 1;
        int gridCols = 2 * columns + 1;
        char[][] grid = new char[gridRows][gridCols];

        // Initialize grid with box drawing characters
        for (int i = 0; i < gridRows; i++) {
            for (int j = 0; j < gridCols; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    if (i == 0 && j == 0) {
                        grid[i][j] = '┌';
                    } else if (i == 0 && j == gridCols - 1) {
                        grid[i][j] = '┐';
                    } else if (i == gridRows - 1 && j == 0) {
                        grid[i][j] = '└';
                    } else if (i == gridRows - 1 && j == gridCols - 1) {
                        grid[i][j] = '┘';
                    } else if (i == 0) {
                        grid[i][j] = '┬';
                    } else if (i == gridRows - 1) {
                        grid[i][j] = '┴';
                    } else if (j == 0) {
                        grid[i][j] = '├';
                    } else if (j == gridCols - 1) {
                        grid[i][j] = '┤';
                    } else {
                        grid[i][j] = '┼';
                    }
                } else if (i % 2 == 0) {
                    grid[i][j] = '─';
                } else if (j % 2 == 0) {
                    grid[i][j] = '│';
                } else {
                    grid[i][j] = ' ';
                }
            }
        }

        Random rand = new Random(seed);
        boolean[][] visited = new boolean[rows][columns];
        Stack<int[]> stack = new Stack<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Start from random cell
        int startRow = rand.nextInt(rows);
        int startCol = rand.nextInt(columns);
        stack.push(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int x = current[0];
            int y = current[1];
            List<int[]> neighbors = new ArrayList<>();

            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];

                if (nx >= 0 && nx < rows && ny >= 0 && ny < columns && !visited[nx][ny]) {
                    neighbors.add(new int[]{nx, ny, dir[0], dir[1]});
                }
            }

            if (!neighbors.isEmpty()) {
                int[] next = neighbors.get(rand.nextInt(neighbors.size()));
                int nx = next[0];
                int ny = next[1];
                int dx = next[2];
                int dy = next[3];

                // Remove wall between current and next cell
                int wallX = x * 2 + 1 + dx;
                int wallY = y * 2 + 1 + dy;
                grid[wallX][wallY] = ' ';

                visited[nx][ny] = true;
                stack.push(new int[]{nx, ny});
            } else {
                stack.pop();
            }
        }

        // Set entrance and exit
        int midRow = rows;
        grid[midRow][0] = '⇨';
        grid[midRow][gridCols - 1] = '⇨';

        return grid;
    }
}
