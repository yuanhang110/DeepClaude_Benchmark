WHITE = 'W'
BLACK = 'B'
NONE = ''

class Board:
    """Count territories of each player in a Go game

    Args:
        board (list[str]): A two-dimensional Go board
    """

    def __init__(self, board):
        self.board = board

    def territory(self, x, y):
        """Find the owner and the territories given a coordinate on
           the board

        Args:
            x (int): Column on the board
            y (int): Row on the board

        Returns:
            (str, set): A tuple, the first element being the owner
                        of that area.  One of "W", "B", "".  The
                        second being a set of coordinates, representing
                        the owner's territories.
        """
        if not self.board:
            raise ValueError('Invalid coordinate')
        height = len(self.board)
        if height == 0:
            raise ValueError('Invalid coordinate')
        width = len(self.board[0])
        if x < 0 or x >= width or y < 0 or y >= height:
            raise ValueError('Invalid coordinate')
        
        # Check if current cell is a stone
        current_cell = self.board[y][x]
        if current_cell != ' ':
            return ('', set())
        
        from collections import deque
        visited = set()
        queue = deque([(x, y)])
        visited.add((x, y))
        territory = set()
        borders = set()
        touches_edge = False
        
        while queue:
            cx, cy = queue.popleft()
            territory.add((cx, cy))
            # Check all four directions
            for dx, dy in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                nx = cx + dx
                ny = cy + dy
                if 0 <= nx < width and 0 <= ny < height:
                    neighbor_cell = self.board[ny][nx]
                    if neighbor_cell == ' ':
                        if (nx, ny) not in visited:
                            visited.add((nx, ny))
                            queue.append((nx, ny))
                    else:
                        if neighbor_cell in ('B', 'W'):
                            borders.add(neighbor_cell)
                else:
                    touches_edge = True
        
        owner = ''
        if not touches_edge:
            if len(borders) == 1:
                owner = borders.pop()
        return (owner, territory)

    def territories(self):
        """Find the owners and the territories of the whole board

        Args:
            none

        Returns:
            dict(str, set): A dictionary whose key being the owner
                        , i.e. "W", "B", "".  The value being a set
                        of coordinates owned by the owner.
        """
        territories = {'B': set(), 'W': set(), '': set()}
        visited = set()
        
        for y in range(len(self.board)):
            row = self.board[y]
            for x in range(len(row)):
                if (x, y) not in visited and row[x] == ' ':
                    owner, coords = self.territory(x, y)
                    territories[owner].update(coords)
                    visited.update(coords)
        
        return territories
