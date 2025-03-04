import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
    private static final char WALL = '|';
    private static final char PATH = ' ';
    private static final char ENTRANCE = '>';
    private static final char HORIZONTAL = '-';
    private static final char CORNER = '+';
    private static final char INTERSECTION = '+';
    
    public char[][] generatePerfectMaze(int rows, int columns) {
        return generatePerfectMaze(rows, columns, new Random().nextInt());
    }

    public char[][] generatePerfectMaze(int rows, int columns, int seed) {
        // Validate input
        if (rows < 5 || rows > 100 || columns < 5 || columns > 100) {
            throw new IllegalArgumentException("Maze dimensions must be between 5 and 100 cells");
        }

        // Initialize maze grid
        int height = 2 * rows + 1;
        int width = 2 * columns + 1;
        char[][] maze = new char[height][width];
        initializeMaze(maze);

        // Create random generator with seed
        Random random = new Random(seed);

        // Use depth-first search to generate maze
        Stack<int[]> stack = new Stack<>();
        int startX = 1;
        int startY = 1;
        maze[startY][startX] = PATH;
        stack.push(new int[]{startY, startX});

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int y = current[0];
            int x = current[1];

            // Get unvisited neighbors
            int[][] neighbors = getUnvisitedNeighbors(maze, y, x);

            if (neighbors.length > 0) {
                // Choose random neighbor
                int[] next = neighbors[random.nextInt(neighbors.length)];
                int ny = next[0];
                int nx = next[1];

                // Remove wall between current and next cell
                maze[(y + ny) / 2][(x + nx) / 2] = PATH;
                maze[ny][nx] = PATH;

                stack.push(next);
            } else {
                stack.pop();
            }
        }

        // Add entrance and exit
        maze[1][0] = ENTRANCE;
        maze[height - 2][width - 1] = ENTRANCE;
        
        // Clean up corners and intersections
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (maze[y][x] == CORNER) {
                    // Check if it's actually an intersection
                    boolean hasHorizontal = (x > 0 && maze[y][x-1] == HORIZONTAL) || 
                                          (x < maze[y].length-1 && maze[y][x+1] == HORIZONTAL);
                    boolean hasVertical = (y > 0 && maze[y-1][x] == WALL) || 
                                        (y < maze.length-1 && maze[y+1][x] == WALL);
                    if (hasHorizontal && hasVertical) {
                        maze[y][x] = INTERSECTION;
                    }
                }
            }
        }

        return maze;
    }

    private void initializeMaze(char[][] maze) {
        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length; x++) {
                if (y % 2 == 0 && x % 2 == 0) {
                    maze[y][x] = CORNER;
                } else if (y % 2 == 0) {
                    maze[y][x] = HORIZONTAL;
                } else if (x % 2 == 0) {
                    maze[y][x] = WALL;
                } else {
                    maze[y][x] = PATH;
                }
            }
        }
    }

    private int[][] getUnvisitedNeighbors(char[][] maze, int y, int x) {
        int[][] directions = {{-2, 0}, {2, 0}, {0, -2}, {0, 2}};
        int[][] neighbors = new int[4][];
        int count = 0;

        for (int[] dir : directions) {
            int ny = y + dir[0];
            int nx = x + dir[1];

            if (ny > 0 && ny < maze.length - 1 && 
                nx > 0 && nx < maze[0].length - 1 && 
                maze[ny][nx] != PATH) {
                neighbors[count++] = new int[]{ny, nx};
            }
        }

        return java.util.Arrays.copyOf(neighbors, count);
    }
}
