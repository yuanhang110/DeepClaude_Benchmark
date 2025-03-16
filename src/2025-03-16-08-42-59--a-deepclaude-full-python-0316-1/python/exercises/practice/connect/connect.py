
class ConnectGame:
    def __init__(self, board):
        # Parse the board into a 2D grid
        self.board = [line.strip().split() for line in board.strip().splitlines()]
        
    def get_winner(self):
        # Check if player O has won (connecting top to bottom)
        if self._has_path('O', self._top_positions(), self._bottom_positions()):
            return 'O'
        
        # Check if player X has won (connecting left to right)
        if self._has_path('X', self._left_positions(), self._right_positions()):
            return 'X'
        
        # No winner
        return ""
    
    def _has_path(self, player, start_positions, end_positions):
        """Check if player has a path from start to end positions using BFS"""
        from collections import deque
        
        # No positions to start from
        if not start_positions:
            return False
            
        # Create a set of visited positions
        visited = set()
        
        # Queue for BFS
        queue = deque(start_positions)
        for pos in start_positions:
            visited.add(pos)
        
        # BFS to find path
        while queue:
            row, col = queue.popleft()
            
            # Check if we've reached an end position
            if (row, col) in end_positions:
                return True
            
            # Check all six adjacent hexes
            neighbors = [
                (row-1, col), (row-1, col+1),  # Up-left, Up-right
                (row, col-1), (row, col+1),    # Left, Right
                (row+1, col-1), (row+1, col)   # Down-left, Down-right
            ]
            
            for nr, nc in neighbors:
                # Check if position is valid and contains player's piece
                if (0 <= nr < len(self.board) and 
                    0 <= nc < len(self.board[nr]) and 
                    self.board[nr][nc] == player and 
                    (nr, nc) not in visited):
                    queue.append((nr, nc))
                    visited.add((nr, nc))
        
        # No path found
        return False
    
    def _top_positions(self):
        """Get all positions on the top edge with O pieces"""
        if not self.board:
            return []
        return [(0, col) for col, cell in enumerate(self.board[0]) if cell == 'O']
    
    def _bottom_positions(self):
        """Get all positions on the bottom edge with O pieces"""
        if not self.board:
            return []
        last_row = len(self.board) - 1
        return [(last_row, col) for col, cell in enumerate(self.board[last_row]) if cell == 'O']
    
    def _left_positions(self):
        """Get all positions on the left edge with X pieces"""
        return [(row, 0) for row, row_cells in enumerate(self.board) if row_cells and row_cells[0] == 'X']
    
    def _right_positions(self):
        """Get all positions on the right edge with X pieces"""
        return [(row, len(row_cells)-1) for row, row_cells in enumerate(self.board) 
                if row_cells and row_cells[-1] == 'X']
