record Position(int x, int y) {}
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class GoCounting {
    private final char[][] grid;

    GoCounting(String board) {
        String[] rows = board.split("\n");
        int height = rows.length;
        int width = rows[0].length();
        grid = new char[height][width];
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = rows[y].charAt(x);
            }
        }
    }

    Player getTerritoryOwner(int x, int y) {
        TerritoryData data = calculateTerritory(x, y);
        return data.owner;
    }

    Set<Position> getTerritory(int x, int y) {
        TerritoryData data = calculateTerritory(x, y);
        return Collections.unmodifiableSet(data.territory.stream()
            .map(p -> new Position(p.x, p.y))
            .collect(Collectors.toSet()));
    }

    Map<Player, Set<Position>> getTerritories() {
        Map<Player, Set<Position>> territories = new EnumMap<>(Player.class);
        territories.put(Player.BLACK, new HashSet<>());
        territories.put(Player.WHITE, new HashSet<>());
        territories.put(Player.NONE, new HashSet<>());
        
        boolean[][] processed = new boolean[grid.length][grid[0].length];
        
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                if (grid[y][x] == ' ' && !processed[y][x]) {
                    TerritoryData data = calculateTerritory(x, y);
                    territories.get(data.owner).addAll(data.territory);
                    for (Position p : data.territory) {
                        processed[p.y][p.x] = true;
                    }
                }
            }
        }
        
        return territories;
    }

    private static class TerritoryData {
        final Set<Position> territory;
        final Player owner;

        TerritoryData(Set<Position> territory, Player owner) {
            this.territory = territory;
            this.owner = owner;
        }
    }

    private TerritoryData calculateTerritory(int x, int y) {
        if (y < 0 || y >= grid.length || x < 0 || x >= grid[0].length) {
            return new TerritoryData(Collections.emptySet(), Player.NONE);
        }
        if (grid[y][x] != ' ') {
            return new TerritoryData(Collections.emptySet(), Player.NONE);
        }
        
        Set<Position> territory = new HashSet<>();
        boolean edgeFound = false;
        Set<Player> borderingPlayers = new HashSet<>();
        Queue<Position> queue = new LinkedList<>();
        boolean[][] visited = new boolean[grid.length][grid[0].length];
        
        queue.add(new Position(x, y));
        visited[y][x] = true;
        
        while (!queue.isEmpty()) {
            Position p = queue.poll();
            territory.add(p);
            
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int nx = p.x + dir[0];
                int ny = p.y + dir[1];
                
                if (nx < 0 || nx >= grid[0].length || ny < 0 || ny >= grid.length) {
                    edgeFound = true;
                } else {
                    char cell = grid[ny][nx];
                    if (cell == 'B') {
                        borderingPlayers.add(Player.BLACK);
                    } else if (cell == 'W') {
                        borderingPlayers.add(Player.WHITE);
                    } else if (cell == ' ' && !visited[ny][nx]) {
                        visited[ny][nx] = true;
                        queue.add(new Position(nx, ny));
                    }
                }
            }
        }
        
        Player owner = Player.NONE;
        if (!edgeFound) {
            if (borderingPlayers.size() == 1) {
                owner = borderingPlayers.iterator().next();
            }
        }
        
        return new TerritoryData(territory, owner);
    }
}
