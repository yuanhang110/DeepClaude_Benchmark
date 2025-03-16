
class ConnectGame:
    def __init__(self, board):
        """
        Initialize the game board from a string representation.
        
        Args:
            board: String representation of the game board with '.' for empty cells,
                  'O' for player O's pieces, and 'X' for player X's pieces.
        """
        self.board = []
        for line in board.strip().split('\n'):
            # Remove leading spaces and split by spaces
            stripped = line.lstrip()
            if stripped:
                self.board.append(stripped.split())

    def get_winner(self):
        """
        Determine the winner of the game.
        
        Player O wins by connecting top to bottom.
        Player X wins by connecting left to right.
        
        Returns:
            'O' if player O wins, 'X' if player X wins, or '' if no winner.
        """
        if not self.board:
            return ''
            
        rows = len(self.board)
        
        # Check if player O has connected top to bottom
        from collections import deque
        
        # Find all O's in the top row to start search
        start_cells = []
        for col in range(len(self.board[0])):
            if self.board[0][col] == 'O':
                start_cells.append((0, col))
        
        if start_cells:
            # BFS to find path from top to bottom
            visited = set(start_cells)
            queue = deque(start_cells)
            
            while queue:
                row, col = queue.popleft()
                
                # Check if we've reached the bottom row
                if row == rows - 1:
                    return 'O'  # Player O wins
                
                # Check all six adjacent hexes
                directions = [(-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0)]
                for dr, dc in directions:
                    nr, nc = row + dr, col + dc
                    
                    # Check if the new position is valid
                    if 0 <= nr < rows and 0 <= nc < len(self.board[nr]):
                        if self.board[nr][nc] == 'O' and (nr, nc) not in visited:
                            visited.add((nr, nc))
                            queue.append((nr, nc))
        
        # Check if player X has connected left to right
        # Find all X's in the leftmost column of each row
        start_cells = []
        for row in range(rows):
            if self.board[row][0] == 'X':
                start_cells.append((row, 0))
        
        if start_cells:
            # BFS to find path from left to right
            visited = set(start_cells)
            queue = deque(start_cells)
            
            while queue:
                row, col = queue.popleft()
                
                # Check if we've reached the rightmost column for this row
                if col == len(self.board[row]) - 1:
                    return 'X'  # Player X wins
                
                # Check all six adjacent hexes
                directions = [(-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0)]
                for dr, dc in directions:
                    nr, nc = row + dr, col + dc
                    
                    # Check if the new position is valid
                    if 0 <= nr < rows and 0 <= nc < len(self.board[nr]):
                        if self.board[nr][nc] == 'X' and (nr, nc) not in visited:
                            visited.add((nr, nc))
                            queue.append((nr, nc))
        
        return ''  # No winner yet
