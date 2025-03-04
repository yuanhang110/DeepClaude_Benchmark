import java.util.Random;
import java.util.Collections;
import java.util.ArrayList;

public class MazeGenerator {

    private static class Cell {
        boolean north = true;
        boolean south = true;
        boolean east = true;
        boolean west = true;
        boolean visited = false;
        int x, y;

        Cell(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public char[][] generatePerfectMaze(int rows, int columns) {
        return generatePerfectMaze(rows, columns, new Random().nextInt());
    }

    public char[][] generatePerfectMaze(int rows, int columns, int seed) {
        if (rows < 5 || rows > 100 || columns < 5 || columns > 100) {
            throw new IllegalArgumentException("Maze dimensions must be between 5 and 100");
        }

        Random random = new Random(seed);
        Cell[][] grid = new Cell[rows][columns];
        
        // Initialize grid
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                grid[y][x] = new Cell(x, y);
            }
        }

        // Recursive backtracking algorithm
        ArrayList<Cell> stack = new ArrayList<>();
        Cell current = grid[0][0];
        current.visited = true;
        stack.add(current);

        while (!stack.isEmpty()) {
            current = stack.remove(stack.size() - 1);
            ArrayList<Cell> neighbors = getUnvisitedNeighbors(current, grid);
            
            if (!neighbors.isEmpty()) {
                stack.add(current);
                Collections.shuffle(neighbors, random);
                Cell next = neighbors.get(0);
                
                // Remove walls between current and next
                if (next.x == current.x && next.y == current.y - 1) {
                    current.north = false;
                    next.south = false;
                }
                if (next.x == current.x && next.y == current.y + 1) {
                    current.south = false;
                    next.north = false;
                }
                if (next.x == current.x + 1 && next.y == current.y) {
                    current.east = false;
                    next.west = false;
                }
                if (next.x == current.x - 1 && next.y == current.y) {
                    current.west = false;
                    next.east = false;
                }
                
                next.visited = true;
                stack.add(next);
            }
        }

        // Create character grid
        int charRows = rows * 2 + 1;
        int charCols = columns * 2 + 1;
        char[][] maze = new char[charRows][charCols];
        
        // Initialize with walls
        for (int y = 0; y < charRows; y++) {
            for (int x = 0; x < charCols; x++) {
                maze[y][x] = ' ';
            }
        }

        // Draw outer walls
        for (int x = 0; x < charCols; x++) {
            maze[0][x] = '─';
            maze[charRows - 1][x] = '─';
        }
        for (int y = 0; y < charRows; y++) {
            maze[y][0] = '│';
            maze[y][charCols - 1] = '│';
        }
        maze[0][0] = '┌';
        maze[0][charCols - 1] = '┐';
        maze[charRows - 1][0] = '└';
        maze[charRows - 1][charCols - 1] = '┘';

        // Draw inner walls based on cell walls
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                Cell cell = grid[y][x];
                int charY = y * 2 + 1;
                int charX = x * 2 + 1;

                if (cell.north) maze[charY - 1][charX] = '─';
                if (cell.south) maze[charY + 1][charX] = '─';
                if (cell.west) maze[charY][charX - 1] = '│';
                if (cell.east) maze[charY][charX + 1] = '│';
                
                // Handle corners
                if (cell.north && cell.west) maze[charY - 1][charX - 1] = '┌';
                if (cell.north && cell.east) maze[charY - 1][charX + 1] = '┐';
                if (cell.south && cell.west) maze[charY + 1][charX - 1] = '└';
                if (cell.south && cell.east) maze[charY + 1][charX + 1] = '┘';
            }
        }

        // Add entrance and exit
        maze[rows][0] = '⇨';
        maze[rows][charCols - 1] = '⇨';

        return maze;
    }

    private ArrayList<Cell> getUnvisitedNeighbors(Cell cell, Cell[][] grid) {
        ArrayList<Cell> neighbors = new ArrayList<>();
        int rows = grid.length;
        int columns = grid[0].length;

        if (cell.y > 0 && !grid[cell.y - 1][cell.x].visited) {
            neighbors.add(grid[cell.y - 1][cell.x]);
        }
        if (cell.y < rows - 1 && !grid[cell.y + 1][cell.x].visited) {
            neighbors.add(grid[cell.y + 1][cell.x]);
        }
        if (cell.x > 0 && !grid[cell.y][cell.x - 1].visited) {
            neighbors.add(grid[cell.y][cell.x - 1]);
        }
        if (cell.x < columns - 1 && !grid[cell.y][cell.x + 1].visited) {
            neighbors.add(grid[cell.y][cell.x + 1]);
        }

        return neighbors;
    }
}
