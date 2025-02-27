import java.awt.Point;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class GoCounting {
    public enum Player {
        BLACK, WHITE, NONE
    }

    private final char[][] board;

    GoCounting(String board) {
        this.board = parseBoard(board);
    }

    private char[][] parseBoard(String input) {
        String[] rows = input.split("\n");
        if (rows.length == 0) {
            return new char[0][0];
        }
        int numRows = rows.length;
        int numCols = rows[0].length();
        char[][] grid = new char[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            if (rows[i].length() != numCols) {
                throw new IllegalArgumentException("All rows must have the same length");
            }
            for (int j = 0; j < numCols; j++) {
                grid[i][j] = rows[i].charAt(j);
            }
        }
        return grid;
    }

    private boolean isValid(int x, int y) {
        return y >= 0 && y < board.length && x >= 0 && x < board[y].length;
    }

    private List<Point> getNeighbors(int x, int y) {
        List<Point> neighbors = new ArrayList<>();
        int[][] directions = {{-1,0}, {1,0}, {0,-1}, {0,1}};
        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];
            if (isValid(nx, ny)) {
                neighbors.add(new Point(nx, ny));
            }
        }
        return neighbors;
    }

    Player getTerritoryOwner(int x, int y) {
        if (!isValid(x, y)) {
            return Player.NONE;
        }
        if (board[y][x] != ' ') {
            return Player.NONE;
        }
        Set<Point> territory = getTerritory(x, y);
        if (territory.isEmpty()) {
            return Player.NONE;
        }
        Set<Player> owners = new HashSet<>();
        for (Point p : territory) {
            for (Point neighbor : getNeighbors(p.x, p.y)) {
                char c = board[neighbor.y][neighbor.x];
                if (c == 'B') {
                    owners.add(Player.BLACK);
                } else if (c == 'W') {
                    owners.add(Player.WHITE);
                }
            }
        }
        if (owners.size() == 1) {
            return owners.iterator().next();
        }
        return Player.NONE;
    }

    Set<Point> getTerritory(int x, int y) {
        Set<Point> territory = new HashSet<>();
        if (!isValid(x, y) || board[y][x] != ' ') {
            return territory;
        }
        boolean[][] visited = new boolean[board.length][board[0].length];
        Queue<Point> queue = new LinkedList<>();
        queue.add(new Point(x, y));
        visited[y][x] = true;
        territory.add(new Point(x, y));
        
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            for (Point neighbor : getNeighbors(p.x, p.y)) {
                if (!visited[neighbor.y][neighbor.x] && board[neighbor.y][neighbor.x] == ' ') {
                    visited[neighbor.y][neighbor.x] = true;
                    territory.add(neighbor);
                    queue.add(neighbor);
                }
            }
        }
        return territory;
    }

    Map<Player, Set<Point>> getTerritories() {
        Map<Player, Set<Point>> territories = new EnumMap<>(Player.class);
        for (Player player : Player.values()) {
            territories.put(player, new HashSet<>());
        }
        boolean[][] visited = new boolean[board.length][board[0].length];
        
        for (int y = 0; y < board.length; y++) {
            for (int x = 0; x < board[y].length; x++) {
                if (board[y][x] == ' ' && !visited[y][x]) {
                    Set<Point> territory = getTerritory(x, y);
                    Player owner = getTerritoryOwner(x, y);
                    territories.get(owner).addAll(territory);
                    for (Point p : territory) {
                        visited[p.y][p.x] = true;
                    }
                }
            }
        }
        return territories;
    }

}
