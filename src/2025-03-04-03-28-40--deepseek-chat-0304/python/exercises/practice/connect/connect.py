
class ConnectGame:
    def __init__(self, board):
        self.board = [line.strip() for line in board.split('\n') if line.strip()]
        self.height = len(self.board)
        self.width = max(len(line) for line in self.board)
        
    def get_winner(self):
        # Check if O has connected top to bottom
        if self._check_winner('O', 'top', 'bottom'):
            return 'O'
        # Check if X has connected left to right
        if self._check_winner('X', 'left', 'right'):
            return 'X'
        return ""
        
    def _check_winner(self, player, start_side, end_side):
        # Get starting positions
        if start_side == 'top':
            starts = [(0, i) for i in range(len(self.board[0])) if self.board[0][i] == player]
        elif start_side == 'left':
            starts = [(i, 0) for i in range(self.height) if self.board[i][0] == player]
        else:
            return False
            
        # DFS to find if any path reaches end side
        visited = set()
        for start in starts:
            stack = [start]
            while stack:
                row, col = stack.pop()
                if (row, col) in visited:
                    continue
                visited.add((row, col))
                
                # Check if reached end side
                if end_side == 'bottom' and row == self.height - 1:
                    return True
                if end_side == 'right' and col == len(self.board[row]) - 1:
                    return True
                    
                # Get valid neighbors - hexagonal pattern
                neighbors = []
                if row > 0:
                    neighbors.append((row-1, col))  # top
                    if col < len(self.board[row-1]) - 1:
                        neighbors.append((row-1, col+1))  # top-right
                if row < self.height - 1:
                    neighbors.append((row+1, col))  # bottom
                    if col > 0:
                        neighbors.append((row+1, col-1))  # bottom-left
                if col > 0:
                    neighbors.append((row, col-1))  # left
                if col < len(self.board[row]) - 1:
                    neighbors.append((row, col+1))  # right
                    
                for r, c in neighbors:
                    if self.board[r][c] == player:
                        stack.append((r, c))
        return False
