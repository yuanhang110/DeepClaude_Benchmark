import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
    private static final char WALL = '│';
    private static final char HORIZONTAL_WALL = '─';
    private static final char CORNER_TOP_LEFT = '┌';
    private static final char CORNER_TOP_RIGHT = '┐';
    private static final char CORNER_BOTTOM_LEFT = '└';
    private static final char CORNER_BOTTOM_RIGHT = '┘';
    private static final char ENTRANCE = '⇨';
    private static final char EXIT = '⇨';
    private static final char SPACE = ' ';
    private static final char T_RIGHT = '├';
    private static final char T_LEFT = '┤';
    private static final char T_DOWN = '┬';
    private static final char T_UP = '┴';
    private static final char CROSS = '┼';

    private final int[] dx = {0, 2, 0, -2};  // east, south, west, north
    private final int[] dy = {2, 0, -2, 0};

    public char[][] generatePerfectMaze(int rows, int columns) {
        return generatePerfectMaze(rows, columns, new Random().nextInt());
    }

    public char[][] generatePerfectMaze(int rows, int columns, int seed) {
        if (rows < 5 || columns < 5 || rows > 100 || columns > 100) {
            throw new IllegalArgumentException("Maze dimensions must be between 5 and 100");
        }

        Random random = new Random(seed);
        int height = 2 * rows + 1;
        int width = 2 * columns + 1;
        char[][] maze = initializeMaze(height, width);
        
        // Start from a random cell (odd indices)
        int startY = 2 * random.nextInt(rows - 2) + 1;
        int startX = 2 * random.nextInt(columns - 2) + 1;
        
        carvePath(maze, startX, startY, random);
        
        // Add entrance and exit
        int entranceRow = height / 2;
        maze[entranceRow][0] = ENTRANCE;
        maze[entranceRow][1] = SPACE;
        
        maze[entranceRow][width - 1] = EXIT;
        maze[entranceRow][width - 2] = SPACE;
        
        return maze;
    }

    private char[][] initializeMaze(int height, int width) {
        char[][] maze = new char[height][width];
        
        // Fill with walls
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i % 2 == 0 && j % 2 == 0) {
                    // Corners and intersections
                    if (i == 0 && j == 0) maze[i][j] = CORNER_TOP_LEFT;
                    else if (i == 0 && j == width - 1) maze[i][j] = CORNER_TOP_RIGHT;
                    else if (i == height - 1 && j == 0) maze[i][j] = CORNER_BOTTOM_LEFT;
                    else if (i == height - 1 && j == width - 1) maze[i][j] = CORNER_BOTTOM_RIGHT;
                    else if (i == 0 || i == height - 1) maze[i][j] = T_DOWN;
                    else if (j == 0 || j == width - 1) maze[i][j] = T_RIGHT;
                    else maze[i][j] = CROSS;
                } else if (i % 2 == 0) {
                    maze[i][j] = HORIZONTAL_WALL;
                } else if (j % 2 == 0) {
                    maze[i][j] = WALL;
                } else {
                    maze[i][j] = WALL;
                }
            }
        }
        return maze;
    }

    private void carvePath(char[][] maze, int x, int y, Random random) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{x, y});
        maze[y][x] = SPACE;

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int cx = current[0];
            int cy = current[1];

            // Get unvisited neighbors
            int[] unvisitedDirections = new int[4];
            int unvisitedCount = 0;

            for (int i = 0; i < 4; i++) {
                int nx = cx + dx[i];
                int ny = cy + dy[i];
                
                if (isValid(maze, nx, ny) && maze[ny][nx] == WALL) {
                    unvisitedDirections[unvisitedCount++] = i;
                }
            }

            if (unvisitedCount > 0) {
                // Choose random direction
                int direction = unvisitedDirections[random.nextInt(unvisitedCount)];
                int nx = cx + dx[direction];
                int ny = cy + dy[direction];
                
                // Remove wall between cells
                maze[cy + dy[direction]/2][cx + dx[direction]/2] = SPACE;
                maze[ny][nx] = SPACE;
                
                stack.push(new int[]{nx, ny});
            } else {
                stack.pop();
            }
        }
    }

    private boolean isValid(char[][] maze, int x, int y) {
        return y > 0 && y < maze.length - 1 && x > 0 && x < maze[0].length - 1;
    }
}
