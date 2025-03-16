
class ConnectGame:
    def __init__(self, board):
        # Parse the board into a 2D grid
        self.board = []
        for line in board.split('\n'):
            stripped = line.strip()
            if stripped:
                self.board.append(stripped.split())

    def get_winner(self):
        if not self.board:
            return ""
        
        rows = len(self.board)
        cols = len(self.board[0]) if rows > 0 else 0
        
        if rows == 0 or cols == 0:
            return ""

        # Check if player O has connected top to bottom
        visited = set()
        queue = []
        
        # Start from all O's in the top row
        for c in range(cols):
            if c < len(self.board[0]) and self.board[0][c] == 'O':
                queue.append((0, c))
                visited.add((0, c))
        
        # BFS to find path from top to bottom
        while queue:
            row, col = queue.pop(0)
            
            # Check if reached bottom row
            if row == rows - 1:
                return 'O'
            
            # Check all six possible hex grid neighbors
            for dr, dc in [(-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0)]:
                r, c = row + dr, col + dc
                if 0 <= r < rows and 0 <= c < len(self.board[r]):
                    if (r, c) not in visited and self.board[r][c] == 'O':
                        visited.add((r, c))
                        queue.append((r, c))

        # Check if player X has connected left to right
        visited = set()
        queue = []
        
        # Start from all X's in the leftmost column
        for r in range(rows):
            if self.board[r][0] == 'X':
                queue.append((r, 0))
                visited.add((r, 0))
        
        # BFS to find path from left to right
        while queue:
            row, col = queue.pop(0)
            
            # Check if reached rightmost column
            if col == len(self.board[row]) - 1:
                return 'X'
            
            # Check all six possible hex grid neighbors
            for dr, dc in [(-1, 0), (-1, 1), (0, -1), (0, 1), (1, -1), (1, 0)]:
                r, c = row + dr, col + dc
                if 0 <= r < rows and 0 <= c < len(self.board[r]):
                    if (r, c) not in visited and self.board[r][c] == 'X':
                        visited.add((r, c))
                        queue.append((r, c))

        # No winner found
        return ""
