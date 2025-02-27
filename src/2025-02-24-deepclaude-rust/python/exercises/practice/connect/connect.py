
class ConnectGame:
    def __init__(self, board):
        self.grid = []
        for line in board.splitlines():
            stripped = line.lstrip()
            cells = stripped.split()
            if cells:  # Only append non-empty rows
                self.grid.append(cells)

    def get_winner(self):
        if not self.grid:
            return ''
        rows = len(self.grid)
        cols = len(self.grid[0]) if rows else 0
        if cols == 0:
            return ''
        
        # Check O's path (top to bottom)
        visited_o = set()
        queue = []
        for c in range(cols):
            if self.grid[0][c] == 'O':
                queue.append((0, c))
                visited_o.add((0, c))
        
        while queue:
            r, c = queue.pop(0)
            if r == rows - 1:
                return 'O'
            for nr, nc in self.get_neighbors(r, c):
                if (nr, nc) not in visited_o and self.grid[nr][nc] == 'O':
                    visited_o.add((nr, nc))
                    queue.append((nr, nc))
        
        # Check X's path (left to right)
        visited_x = set()
        queue = []
        for r in range(rows):
            if self.grid[r][0] == 'X':
                queue.append((r, 0))
                visited_x.add((r, 0))
        
        while queue:
            r, c = queue.pop(0)
            if c == cols - 1:
                return 'X'
            for nr, nc in self.get_neighbors(r, c):
                if (nr, nc) not in visited_x and self.grid[nr][nc] == 'X':
                    visited_x.add((nr, nc))
                    queue.append((nr, nc))
        
        return ''
    
    def get_neighbors(self, r, c):
        directions = [ (-1, 0), (-1, 1),
                       (0, -1), (0, 1),
                       (1, -1), (1, 0) ]
        neighbors = []
        rows = len(self.grid)
        for dr, dc in directions:
            nr = r + dr
            nc = c + dc
            if 0 <= nr < rows and 0 <= nc < len(self.grid[nr]):
                neighbors.append((nr, nc))
        return neighbors
