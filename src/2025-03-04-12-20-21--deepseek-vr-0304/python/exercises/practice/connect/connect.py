
class ConnectGame:
    def __init__(self, board):
        # Parse the board into a grid of rows and cells
        self.grid = []
        for line in board.split('\n'):
            stripped = line.strip()
            if stripped:
                self.grid.append(stripped.split())

    def get_winner(self):
        from collections import deque

        # Check for X player (left to right)
        if self._has_winner('X', lambda r, c: c == 0, lambda r, c: c == len(self.grid[0]) - 1):
            return 'X'
        # Check for O player (top to bottom)
        if self._has_winner('O', lambda r, c: r == 0, lambda r, c: r == len(self.grid) - 1):
            return 'O'
        return ''

    def _has_winner(self, player, start_condition, end_condition):
        from collections import deque
        if not self.grid:
            return False
        rows = len(self.grid)
        cols = len(self.grid[0]) if rows > 0 else 0
        visited = set()
        queue = deque()

        # Initialize queue with starting positions
        for r in range(rows):
            for c in range(cols):
                if start_condition(r, c) and self.grid[r][c] == player:
                    queue.append((r, c))
                    visited.add((r, c))

        # Hex neighbor directions
        directions = [(-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0)]

        while queue:
            r, c = queue.popleft()
            if end_condition(r, c):
                return True
            for dr, dc in directions:
                nr, nc = r + dr, c + dc
                if 0 <= nr < rows and 0 <= nc < cols:
                    if self.grid[nr][nc] == player and (nr, nc) not in visited:
                        visited.add((nr, nc))
                        queue.append((nr, nc))
        return False
