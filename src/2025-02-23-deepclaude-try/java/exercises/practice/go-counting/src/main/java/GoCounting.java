import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.Queue;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Collections;

class GoCounting {
    private final Player[][] boardGrid;
    
    enum Player {
        BLACK, WHITE, NONE
    }
    
    record Point(int x, int y) {}

    GoCounting(String board) {
        String[] rows = board.split("\n");
        boardGrid = new Player[rows.length][rows[0].length()];
        
        for (int y = 0; y < rows.length; y++) {
            String row = rows[y];
            for (int x = 0; x < row.length(); x++) {
                char c = row.charAt(x);
                if (c == 'B') {
                    boardGrid[y][x] = Player.BLACK;
                } else if (c == 'W') {
                    boardGrid[y][x] = Player.WHITE;
                } else {
                    boardGrid[y][x] = Player.NONE;
                }
            }
        }
    }
    
    private boolean isValidPoint(int x, int y) {
        return y >= 0 && y < boardGrid.length && x >= 0 && x < boardGrid[0].length;
    }
    
    private Set<Point> getNeighbors(int x, int y) {
        Set<Point> neighbors = new HashSet<>();
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        
        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];
            if (isValidPoint(newX, newY)) {
                neighbors.add(new Point(newX, newY));
            }
        }
        return neighbors;
    }
    
    private Map.Entry<Set<Point>, Set<Player>> calculateTerritory(int x, int y) {
        if (!isValidPoint(x, y) || boardGrid[y][x] != Player.NONE) {
            return Map.entry(Collections.emptySet(), Collections.emptySet());
        }
        
        Set<Point> territory = new HashSet<>();
        Set<Player> borderingPlayers = new HashSet<>();
        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();
        
        Point start = new Point(x, y);
        queue.add(start);
        visited.add(start);
        
        while (!queue.isEmpty()) {
            Point p = queue.poll();
            territory.add(p);
            
            for (Point neighbor : getNeighbors(p.x, p.y)) {
                Player neighborPlayer = boardGrid[neighbor.y][neighbor.x];
                if (neighborPlayer == Player.NONE) {
                    if (!visited.contains(neighbor)) {
                        visited.add(neighbor);
                        queue.add(neighbor);
                    }
                } else {
                    borderingPlayers.add(neighborPlayer);
                }
            }
        }
        
        return Map.entry(territory, borderingPlayers);
    }

    Player getTerritoryOwner(int x, int y) {
        var result = calculateTerritory(x, y);
        Set<Player> borderingPlayers = result.getValue();
        
        if (borderingPlayers.size() != 1) {
            return Player.NONE;
        }
        return borderingPlayers.iterator().next();
    }

    Set<Point> getTerritory(int x, int y) {
        return calculateTerritory(x, y).getKey();
    }

    Map<Player, Set<Point>> getTerritories() {
        Map<Player, Set<Point>> territories = new HashMap<>();
        territories.put(Player.BLACK, new HashSet<>());
        territories.put(Player.WHITE, new HashSet<>());
        territories.put(Player.NONE, new HashSet<>());
        
        Set<Point> visited = new HashSet<>();
        
        for (int y = 0; y < boardGrid.length; y++) {
            for (int x = 0; x < boardGrid[0].length; x++) {
                if (boardGrid[y][x] == Player.NONE) {
                    Point p = new Point(x, y);
                    if (!visited.contains(p)) {
                        var result = calculateTerritory(x, y);
                        Set<Point> territory = result.getKey();
                        Set<Player> borderingPlayers = result.getValue();
                        
                        Player owner = Player.NONE;
                        if (borderingPlayers.size() == 1) {
                            owner = borderingPlayers.iterator().next();
                        }
                        
                        territories.get(owner).addAll(territory);
                        visited.addAll(territory);
                    }
                }
            }
        }
        
        return territories;
    }

}
