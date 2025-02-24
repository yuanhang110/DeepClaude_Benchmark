import java.util.Random;
import java.util.Stack;

public class MazeGenerator {
    private static final char WALL = '│';
    private static final char PATH = ' ';
    private static final char CORNER = '┌';
    private static final char HORIZONTAL = '─';
    private static final char ENTRANCE = '⇨';
    
    private static final int NORTH = 1;
    private static final int SOUTH = 2;
    private static final int EAST = 3;
    private static final int WEST = 4;
    
    private Random rand;
    private char[][] maze;
    private int[][] grid;
    private int rows;
    private int columns;

    public char[][] generatePerfectMaze(int rows, int columns) {
        return generatePerfectMaze(rows, columns, new Random().nextInt());
    }

    public char[][] generatePerfectMaze(int rows, int columns, int seed) {
        validateDimensions(rows, columns);
        initialize(rows, columns, seed);
        carvePath(1, 1);
        setEntrances();
        return maze;
    }

    private void validateDimensions(int rows, int columns) {
        if (rows < 5 || rows > 100 || columns < 5 || columns > 100) {
            throw new IllegalArgumentException("Maze dimensions must be between 5x5 and 100x100 cells");
        }
        if (rows % 2 == 0 || columns % 2 == 0) {
            throw new IllegalArgumentException("Maze dimensions must be odd numbers between 5-100");
        }
    }

    private void initialize(int rows, int columns, int seed) {
        this.rows = rows;
        this.columns = columns;
        this.rand = new Random(seed);
        this.grid = new int[rows * 2 + 1][columns * 2 + 1];
        this.maze = new char[rows * 2 + 1][columns * 2 + 1];
        initializeMaze();
    }

    private void initializeMaze() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (i % 2 == 0 || j % 2 == 0) {
                    maze[i][j] = WALL;
                    grid[i][j] = 1;
                } else {
                    maze[i][j] = PATH;
                    grid[i][j] = 0;
                }
            }
        }
    }

    private void carvePath(int x, int y) {
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{x, y});
        grid[x][y] = 1;

        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int dir = getRandomDirection(current[0], current[1]);

            if (dir != 0) {
                int nx = current[0], ny = current[1];
                switch (dir) {
                    case NORTH: nx--; break;
                    case SOUTH: nx++; break;
                    case EAST: ny++; break;
                    case WEST: ny--; break;
                }
                
                // Carve through wall
                maze[(current[0] + nx)/2][(current[1] + ny)/2] = PATH;
                grid[nx][ny] = 1;
                stack.push(new int[]{nx, ny});
            } else {
                stack.pop();
            }
        }
    }

    private int getRandomDirection(int x, int y) {
        int[] dirs = {NORTH, SOUTH, EAST, WEST};
        shuffleArray(dirs);
        
        for (int dir : dirs) {
            int nx = x, ny = y;
            switch (dir) {
                case NORTH: nx -= 2; break;
                case SOUTH: nx += 2; break;
                case EAST: ny += 2; break;
                case WEST: ny -= 2; break;
            }
            
            if (nx > 0 && nx < grid.length - 1 && 
                ny > 0 && ny < grid[0].length - 1 && grid[nx][ny] == 0) {
                return dir;
            }
        }
        return 0;
    }

    private void shuffleArray(int[] array) {
        for (int i = array.length - 1; i > 0; i--) {
            int index = rand.nextInt(i + 1);
            int temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    private void setEntrances() {
        // Left entrance
        maze[1][0] = ENTRANCE;
        // Right exit
        maze[maze.length - 2][maze[0].length - 1] = ENTRANCE;
        
        // Add horizontal walls and corners
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[i].length; j++) {
                if (maze[i][j] == WALL) {
                    if ((i == 0 || i == maze.length - 1) && j % 2 == 0) {
                        maze[i][j] = HORIZONTAL;
                    }
                    if (i % 2 == 0 && j % 2 == 0) {
                        maze[i][j] = CORNER;
                    }
                }
            }
        }
    }
}
