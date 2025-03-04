
WHITE = 'W'
BLACK = 'B'
EMPTY = ' '

class Board:
    """Count territories of each player in a Go game

    Args:
        board (list[str]): A two-dimensional Go board
    """

    def __init__(self, board):
        self.board = [list(row) for row in board]
        self.height = len(self.board)
        self.width = len(self.board[0]) if self.height else 0

    def _is_valid(self, x, y):
        """Check if coordinates are within board bounds"""
        return 0 <= x < self.width and 0 <= y < self.height

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

        Raises:
            ValueError: If coordinates are invalid
        """
        if not self._is_valid(x, y):
            raise ValueError('Invalid coordinate')

        if self.board[y][x] != ' ':
            return ('', set())

        visited = set()
        queue = [(x, y)]
        borders = set()

        while queue:
            cx, cy = queue.pop(0)
            if (cx, cy) in visited:
                continue
            visited.add((cx, cy))

            for dx, dy in [(-1, 0), (1, 0), (0, -1), (0, 1)]:
                nx, ny = cx + dx, cy + dy
                if not self._is_valid(nx, ny):
                    continue
                if self.board[ny][nx] == ' ':
                    if (nx, ny) not in visited:
                        queue.append((nx, ny))
                else:
                    borders.add(self.board[ny][nx])

        if len(borders) == 1:
            owner = borders.pop()
        else:
            owner = ''

        return (owner, visited)

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
                if self.board[y][x] == ' ' and (x, y) not in visited:
                    owner, territory = self.territory(x, y)
                    result[owner].update(territory)
                    visited.update(territory)

        return result
