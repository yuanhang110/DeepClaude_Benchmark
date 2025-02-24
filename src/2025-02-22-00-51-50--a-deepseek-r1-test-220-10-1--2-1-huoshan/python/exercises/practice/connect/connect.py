
class ConnectGame:
    def __init__(self, board):
        self.grid = []
        for line in board:
            stripped = line.strip()
            if stripped:
                self.grid.append(list(stripped))
        self.rows = len(self.grid)
        self.cols = len(self.grid[0]) if self.rows else 0

    def get_winner(self):
        if not self.grid:
            return ''
        
        # Check O's vertical connection (top to bottom)
        visited_o = set()
        queue_o = []
        # Start positions are all 'O's in the first row
        for col in range(self.cols):
            if self.grid[0][col] == 'O':
                queue_o.append((0, col))
                visited_o.add((0, col))
        # BFS for O
        for row, col in queue_o:
            if row == self.rows - 1:
                return 'O'
        while queue_o:
            row, col = queue_o.pop(0)
            # Get neighbors based on hexagonal grid parity (even/odd row)
            if row % 2 == 0:
                neighbors = [
                    (row - 1, col),      # up
                    (row - 1, col + 1),  # up-right
                    (row, col - 1),      # left
                    (row, col + 1),      # right
                    (row + 1, col),      # down
                    (row + 1, col + 1),  # down-right
                ]
            else:
                neighbors = [
                    (row - 1, col - 1),  # up-left
                    (row - 1, col),      # up
                    (row, col - 1),      # left
                    (row, col + 1),      # right
                    (row + 1, col - 1),  # down-left
                    (row + 1, col),      # down
                ]
            
            for nr, nc in neighbors:
                if 0 <= nr < self.rows and 0 <= nc < self.cols:
                    if (nr, nc) not in visited_o and self.grid[nr][nc] == 'O':
                        if nr == self.rows - 1:
                            return 'O'
                        visited_o.add((nr, nc))
                        queue_o.append((nr, nc))
        
        # Check X's horizontal connection (left to right)
        visited_x = set()
        queue_x = []
        # Start positions are all 'X's in first column
        for row in range(self.rows):
            if self.grid[row][0] == 'X':
                queue_x.append((row, 0))
                visited_x.add((row, 0))
        # BFS for X
        for row, col in queue_x:
            if col == self.cols - 1:
                return 'X'
        while queue_x:
            row, col = queue_x.pop(0)
            # Get neighbors based on hexagonal grid parity (even/odd row)
            if row % 2 == 0:
                neighbors = [
                    (row - 1, col),      # up
                    (row - 1, col + 1),  # up-right
                    (row, col - 1),      # left
                    (row, col + 1),      # right
                    (row + 1, col),      # down
                    (row + 1, col + 1),  # down-right
                ]
            else:
                neighbors = [
                    (row - 1, col - 1),  # up-left
                    (row - 1, col),      # up
                    (row, col - 1),      # left
                    (row, col + 1),      # right
                    (row + 1, col - 1),  # down-left
                    (row + 1, col),      # down
                ]
            
            for nr, nc in neighbors:
                if 0 <= nr < self.rows and 0 <= nc < self.cols:
                    if (nr, nc) not in visited_x and self.grid[nr][nc] == 'X':
                        if nc == self.cols - 1:
                            return 'X'
                        visited_x.add((nr, nc))
                        queue_x.append((nr, nc))
        
        return ''
