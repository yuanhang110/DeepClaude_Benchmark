
class ConnectGame:
    def __init__(self, board):
        # Split board into rows and parse each row into cells
        self.grid = []
        if isinstance(board, str):
            rows = board.strip().split('\n')
        else:
            rows = board
            
        for row in rows:
            # Split row into cells, filtering out empty strings from spaces
            cells = [cell for cell in row.split(' ') if cell]
            self.grid.append(cells)
    
    def get_winner(self):
        # Check O's win condition (top to bottom)
        if self._check_o_win():
            return 'O'
        # Check X's win condition (left to right) 
        if self._check_x_win():
            return 'X'
        return ''
    
    def _check_o_win(self):
        if not self.grid:
            return False
            
        # Check from first row to last row
        start_row = 0
        target_row = len(self.grid) - 1
        
        # Start from any O in first row
        for col in range(len(self.grid[0])):
            if self.grid[0][col] == 'O':
                if self._can_reach_bottom(start_row, col):
                    return True
        return False
        
    def _check_x_win(self):
        if not self.grid:
            return False
            
        # Check each row for left-to-right connection
        for row in range(len(self.grid)):
            # Start from leftmost X
            if len(self.grid[row]) > 0 and self.grid[row][0] == 'X':
                if self._can_reach_right(row, 0):
                    return True
        return False
    
    def _can_reach_bottom(self, row, col):
        visited = set()
        queue = [(row, col)]
        visited.add((row, col))
        
        while queue:
            current_row, current_col = queue.pop(0)
            
            # Check if reached bottom row
            if current_row == len(self.grid) - 1:
                return True
                
            # Check all neighbors
            for next_row, next_col in self._get_neighbors(current_row, current_col):
                if (next_row, next_col) not in visited and self.grid[next_row][next_col] == 'O':
                    queue.append((next_row, next_col))
                    visited.add((next_row, next_col))
        return False
        
    def _can_reach_right(self, row, col):
        visited = set()
        queue = [(row, col)]
        visited.add((row, col))
        
        while queue:
            current_row, current_col = queue.pop(0)
            
            # Check if reached rightmost position in any row
            if current_col == len(self.grid[current_row]) - 1:
                return True
                
            # Check all neighbors
            for next_row, next_col in self._get_neighbors(current_row, current_col):
                if (next_row, next_col) not in visited and self.grid[next_row][next_col] == 'X':
                    queue.append((next_row, next_col))
                    visited.add((next_row, next_col))
        return False
    
    def _get_neighbors(self, row, col):
        # Returns valid neighboring coordinates in hexagonal grid
        neighbors = []
        # Check all 6 possible neighbors in hexagonal grid
        directions = [
            (-1, -1), (-1, 0),  # Upper neighbors
            (0, -1), (0, 1),    # Same row neighbors
            (1, -1), (1, 0)     # Lower neighbors
        ]
        
        for dr, dc in directions:
            new_row = row + dr
            new_col = col + dc
            if (0 <= new_row < len(self.grid) and 
                0 <= new_col < len(self.grid[new_row])):
                neighbors.append((new_row, new_col))
        return neighbors
