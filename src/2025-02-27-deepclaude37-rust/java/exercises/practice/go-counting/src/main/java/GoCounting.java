import java.util.*;

class GoCounting {
    // Custom Point class to avoid using java.awt.Point
    static class Point {
        public final int x;
        public final int y;
        
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y;
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
    
    enum Player {
        BLACK, WHITE, NONE
    }
    
    private Player[][] board;
    private int width;
    private int height;
    private boolean[][] visited;
    private Player[][] territoryOwner;
    private Map<Point, Set<Point>> territories;
    private boolean initialized = false;
    
    GoCounting(String board) {
        String[] rows = board.split("\n");
        height = rows.length;
        width = rows[0].length();
        
        this.board = new Player[height][width];
        
        // Parse the board
        for (int y = 0; y < height; y++) {
            String row = rows[y];
            for (int x = 0; x < width; x++) {
                char c = x < row.length() ? row.charAt(x) : ' ';
                if (c == 'B') {
                    this.board[y][x] = Player.BLACK;
                } else if (c == 'W') {
                    this.board[y][x] = Player.WHITE;
                } else {
                    this.board[y][x] = Player.NONE;
                }
            }
        }
    }

    Player getTerritoryOwner(int x, int y) {
        if (!isValidPoint(x, y)) {
            throw new IllegalArgumentException("Invalid coordinate");
        }
        
        if (board[y][x] != Player.NONE) {
            return Player.NONE;
        }
        
        calculateTerritories();
        return territoryOwner[y][x];
    }

    Set<Point> getTerritory(int x, int y) {
        if (!isValidPoint(x, y)) {
            throw new IllegalArgumentException("Invalid coordinate");
        }
        
        if (board[y][x] != Player.NONE) {
            return Collections.emptySet();
        }
        
        calculateTerritories();
        Point key = new Point(x, y);
        return territories.getOrDefault(key, Collections.emptySet());
    }

    Map<Player, Set<Point>> getTerritories() {
        calculateTerritories();
        
        Map<Player, Set<Point>> result = new EnumMap<>(Player.class);
        result.put(Player.BLACK, new HashSet<>());
        result.put(Player.WHITE, new HashSet<>());
        result.put(Player.NONE, new HashSet<>());
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (board[y][x] == Player.NONE) {
                    Player owner = territoryOwner[y][x];
                    result.get(owner).add(new Point(x, y));
                }
            }
        }
        
        return result;
    }

    private boolean isValidPoint(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
    
    private void calculateTerritories() {
        if (initialized) {
            return;
        }
        
        initialized = true;
        territoryOwner = new Player[height][width];
        territories = new HashMap<>();
        visited = new boolean[height][width];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (board[y][x] == Player.NONE && !visited[y][x]) {
                    bfs(x, y);
                }
            }
        }
    }
    
    private void bfs(int startX, int startY) {
        Queue<Point> queue = new LinkedList<>();
        Set<Point> territory = new HashSet<>();
        Set<Player> borderingPlayers = new HashSet<>();
        
        Point start = new Point(startX, startY);
        queue.add(start);
        visited[startY][startX] = true;
        territory.add(start);
        
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        
        while (!queue.isEmpty()) {
            Point current = queue.poll();
            
            for (int[] dir : directions) {
                int newX = current.x + dir[0];
                int newY = current.y + dir[1];
                
                if (isValidPoint(newX, newY)) {
                    if (board[newY][newX] == Player.NONE) {
                        if (!visited[newY][newX]) {
                            Point newPoint = new Point(newX, newY);
                            visited[newY][newX] = true;
                            queue.add(newPoint);
                            territory.add(newPoint);
                        }
                    } else {
                        // Found a bordering stone
                        borderingPlayers.add(board[newY][newX]);
                    }
                }
            }
        }
        
        // Determine territory owner
        Player owner;
        if (borderingPlayers.size() == 1) {
            owner = borderingPlayers.iterator().next();
        } else {
            owner = Player.NONE; // Multiple players or no bordering stones
        }
        
        // Record territory information
        for (Point p : territory) {
            territoryOwner[p.y][p.x] = owner;
            territories.put(p, territory);
        }
    }
}
