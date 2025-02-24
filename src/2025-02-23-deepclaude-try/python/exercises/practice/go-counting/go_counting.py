
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
        self.height = len(board)
        self.width = len(board[0]) if self.height > 0 else 0

    def _bfs_territory(self, x, y):
        """Helper method to perform BFS and find territory and owner for given coordinates."""
        from collections import deque

        queue = deque([(x, y)])
        visited = set([(x, y)])
        owners = set()
        has_edge = False
        directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]

        while queue:
            x_, y_ = queue.popleft()
            for dx, dy in directions:
                nx = x_ + dx
                ny = y_ + dy
                if 0 <= nx < self.width and 0 <= ny < self.height:
                    cell = self.board[ny][nx]
                    if cell == ' ':
                        if (nx, ny) not in visited:
                            visited.add((nx, ny))
                            queue.append((nx, ny))
                    else:
                        owners.add(cell)
                else:
                    has_edge = True

        if has_edge or len(owners) != 1:
            owner = ''
        else:
            owner = owners.pop() if owners else ''
        return (owner, visited)

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
        if x < 0 or x >= self.width or y < 0 or y >= self.height:
            raise ValueError('Invalid coordinate')
        if self.board[y][x] != ' ':
            return ('', set())
        return self._bfs_territory(x, y)

    def territories(self):
        """Find the owners and the territories of the whole board

        Args:
            none

        Returns:
            dict(str, set): A dictionary whose key being the owner
                        , i.e. "W", "B", "".  The value being a set
                        of coordinates owned by the owner.
        """
        result = {'B': set(), 'W': set(), '': set()}
        visited = set()

        for y in range(self.height):
            for x in range(self.width):
                if (x, y) in visited or self.board[y][x] != ' ':
                    continue
                owner, territory = self._bfs_territory(x, y)
                result[owner].update(territory)
                visited.update(territory)
        return result
