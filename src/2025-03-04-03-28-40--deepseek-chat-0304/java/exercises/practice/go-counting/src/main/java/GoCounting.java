import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

class GoCounting {
    private enum Cell { BLACK, WHITE, EMPTY }
    private Cell[][] board;
    private int width;
    private int height;

    GoCounting(String board) {
        String[] lines = board.split("\n");
        this.height = lines.length;
        this.width = lines[0].length();
        this.board = new Cell[height][width];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                char c = lines[y].charAt(x);
                switch (c) {
                    case 'B': this.board[y][x] = Cell.BLACK; break;
                    case 'W': this.board[y][x] = Cell.WHITE; break;
                    default: this.board[y][x] = Cell.EMPTY; break;
                }
            }
        }
    }

    Player getTerritoryOwner(int x, int y) {
        if (!isValid(x, y)) {
            throw new IllegalArgumentException("Invalid coordinate");
        }
        if (board[y][x] != Cell.EMPTY) {
            return Player.NONE;
        }
        
        Set<Point> territory = new HashSet<>();
        Set<Player> borders = new HashSet<>();
        floodFill(x, y, territory, borders);
        
        if (borders.size() == 1) {
            return borders.iterator().next();
        }
        return Player.NONE;
    }

    Set<Point> getTerritory(int x, int y) {
        if (!isValid(x, y)) {
            throw new IllegalArgumentException("Invalid coordinate");
        }
        if (board[y][x] != Cell.EMPTY) {
            return Set.of();
        }
        
        Set<Point> territory = new HashSet<>();
        Set<Player> borders = new HashSet<>();
        floodFill(x, y, territory, borders);
        return territory;
    }

    Map<Player, Set<Point>> getTerritories() {
        Map<Player, Set<Point>> territories = new HashMap<>();
        territories.put(Player.BLACK, new HashSet<>());
        territories.put(Player.WHITE, new HashSet<>());
        territories.put(Player.NONE, new HashSet<>());
        
        boolean[][] visited = new boolean[height][width];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (board[y][x] == Cell.EMPTY && !visited[y][x]) {
                    Set<Point> territory = new HashSet<>();
                    Set<Player> borders = new HashSet<>();
                    floodFill(x, y, territory, borders);
                    
                    Player owner = borders.size() == 1 ? borders.iterator().next() : Player.NONE;
                    territories.get(owner).addAll(territory);
                    
                    for (Point p : territory) {
                        visited[p.y][p.x] = true;
                    }
                }
            }
        }
        return territories;
    }

    private void floodFill(int x, int y, Set<Point> territory, Set<Player> borders) {
        // Use a stack-based approach instead of recursion
        int[] stackX = new int[width * height];
        int[] stackY = new int[width * height];
        int stackPtr = 0;
        
        stackX[stackPtr] = x;
        stackY[stackPtr] = y;
        stackPtr++;
        
        while (stackPtr > 0) {
            stackPtr--;
            int cx = stackX[stackPtr];
            int cy = stackY[stackPtr];
            
            if (!isValid(cx, cy) || board[cy][cx] != Cell.EMPTY || territory.contains(new Point(cx, cy))) {
                continue;
            }
            
            territory.add(new Point(cx, cy));
            
            // Check neighbors
            if (cx > 0) {
                if (board[cy][cx-1] == Cell.BLACK) borders.add(Player.BLACK);
                else if (board[cy][cx-1] == Cell.WHITE) borders.add(Player.WHITE);
                else {
                    stackX[stackPtr] = cx-1;
                    stackY[stackPtr] = cy;
                    stackPtr++;
                }
            }
            if (cx < width-1) {
                if (board[cy][cx+1] == Cell.BLACK) borders.add(Player.BLACK);
                else if (board[cy][cx+1] == Cell.WHITE) borders.add(Player.WHITE);
                else {
                    stackX[stackPtr] = cx+1;
                    stackY[stackPtr] = cy;
                    stackPtr++;
                }
            }
            if (cy > 0) {
                if (board[cy-1][cx] == Cell.BLACK) borders.add(Player.BLACK);
                else if (board[cy-1][cx] == Cell.WHITE) borders.add(Player.WHITE);
                else {
                    stackX[stackPtr] = cx;
                    stackY[stackPtr] = cy-1;
                    stackPtr++;
                }
            }
            if (cy < height-1) {
                if (board[cy+1][cx] == Cell.BLACK) borders.add(Player.BLACK);
                else if (board[cy+1][cx] == Cell.WHITE) borders.add(Player.WHITE);
                else {
                    stackX[stackPtr] = cx;
                    stackY[stackPtr] = cy+1;
                    stackPtr++;
                }
            }
        }
    }

    private boolean isValid(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}
