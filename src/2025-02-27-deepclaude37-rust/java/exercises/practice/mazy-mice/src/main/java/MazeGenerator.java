import java.util.Random;
import java.util.Stack;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class MazeGenerator {

    public char[][] generatePerfectMaze(int rows, int columns) {
        validateDimensions(rows, columns);
        Random random = new Random();
        return generateMaze(rows, columns, random);
    }

    public char[][] generatePerfectMaze(int rows, int columns, int seed) {
        validateDimensions(rows, columns);
        Random random = new Random(seed);
        return generateMaze(rows, columns, random);
    }
    
    private void validateDimensions(int rows, int columns) {
        if (rows < 5 || rows > 100 || columns < 5 || columns > 100) {
            throw new IllegalArgumentException("Rows and columns must be between 5 and 100 inclusive.");
        }
    }
    
    private char[][] generateMaze(int rows, int columns, Random random) {
        // Dimensions for the character grid (2x+1 by 2y+1)
        int height = 2 * rows + 1;
        int width = 2 * columns + 1;
        char[][] maze = new char[height][width];
        
        // Initialize all cells as walls
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                maze[i][j] = ' ';
            }
        }
        
        // Draw the outer walls
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                // Every even row or column represents a wall
                if (i == 0 || i == height - 1 || j == 0 || j == width - 1 || i % 2 == 0 || j % 2 == 0) {
                    maze[i][j] = '│'; // Temporary wall marker
                }
            }
        }
        
        // Create cells in odd positions
        for (int i = 1; i < height; i += 2) {
            for (int j = 1; j < width; j += 2) {
                maze[i][j] = ' ';
            }
        }
        
        // Generate the maze using recursive backtracking
        boolean[][] visited = new boolean[rows][columns];
        Stack<int[]> stack = new Stack<>();
        
        // Start at a random cell
        int startRow = random.nextInt(rows);
        int startCol = random.nextInt(columns);
        stack.push(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;
        
        // Directions: North, South, West, East
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        while (!stack.isEmpty()) {
            int[] current = stack.peek();
            int row = current[0];
            int col = current[1];
            
            // Find unvisited neighbors
            List<int[]> neighbors = new ArrayList<>();
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < columns && !visited[newRow][newCol]) {
                    neighbors.add(new int[]{newRow, newCol, dir[0], dir[1]});
                }
            }
            
            if (!neighbors.isEmpty()) {
                // Choose random unvisited neighbor
                int[] next = neighbors.get(random.nextInt(neighbors.size()));
                int newRow = next[0];
                int newCol = next[1];
                int dirRow = next[2];
                int dirCol = next[3];
                
                // Remove wall between current and next cell
                int wallRow = 2 * row + 1 + dirRow;
                int wallCol = 2 * col + 1 + dirCol;
                maze[wallRow][wallCol] = ' ';
                
                // Mark as visited and push to stack
                visited[newRow][newCol] = true;
                stack.push(new int[]{newRow, newCol});
            } else {
                // Backtrack
                stack.pop();
            }
        }
        
        // Set entrance and exit
        int entranceRow = 1 + 2 * (rows / 2);
        maze[entranceRow][0] = '>';
        
        int exitRow = 1 + 2 * (rows / 2);
        maze[exitRow][width - 1] = '>';
        
        // Clear the path at entrance and exit
        maze[entranceRow][1] = ' ';
        maze[exitRow][width - 2] = ' ';
        
        // Replace temporary wall markers with proper box-drawing characters
        convertToBoxDrawing(maze);
        
        return maze;
    }
    
    private void convertToBoxDrawing(char[][] maze) {
        int height = maze.length;
        int width = maze[0].length;
        
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (maze[i][j] == '│') {
                    boolean north = i > 0 && maze[i-1][j] == '│';
                    boolean south = i < height-1 && maze[i+1][j] == '│';
                    boolean west = j > 0 && maze[i][j-1] == '│';
                    boolean east = j < width-1 && maze[i][j+1] == '│';
                    
                    // Determine the appropriate box-drawing character based on surrounding walls
                    if (north && south && west && east) maze[i][j] = '┼';
                    else if (north && south && west) maze[i][j] = '┤';
                    else if (north && south && east) maze[i][j] = '├';
                    else if (north && west && east) maze[i][j] = '┴';
                    else if (south && west && east) maze[i][j] = '┬';
                    else if (north && south) maze[i][j] = '│';
                    else if (west && east) maze[i][j] = '─';
                    else if (north && east) maze[i][j] = '└';
                    else if (north && west) maze[i][j] = '┘';
                    else if (south && east) maze[i][j] = '┌';
                    else if (south && west) maze[i][j] = '┐';
                    else if (north) maze[i][j] = '│';
                    else if (south) maze[i][j] = '│';
                    else if (west) maze[i][j] = '─';
                    else if (east) maze[i][j] = '─';
                    else maze[i][j] = ' ';
                }
            }
        }
    }
}
