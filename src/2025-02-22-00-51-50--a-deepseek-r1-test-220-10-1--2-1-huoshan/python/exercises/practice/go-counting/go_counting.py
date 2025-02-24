
WHITE = 'W'
BLACK = 'B'
EMPTY = ' '

class Board:
    """Count territories of each player in a Go game

    Args:
        board (list[str]): A two-dimensional Go board
    """

    def __init__(self, board):
        self.board = board
        self.height = len(board)
        self.width = len(board[0]) if self.height > 0 else 0

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
            raise ValueError('Invalid coordinate')
        
        from collections import deque
        visited = set()
        queue = deque([(x, y)])
        visited.add((x, y))
        stones = set()
        directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        
        while queue:
            cx, cy = queue.popleft()
            for dx, dy in directions:
                nx = cx + dx
                ny = cy + dy
                if 0 <= nx < self.width and 0 <= ny < self.height:
                    cell = self.board[ny][nx]
                    if cell == ' ':
                        if (nx, ny) not in visited:
                            visited.add((nx, ny))
                            queue.append((nx, ny))
                    else:
                        stones.add(cell)
        
        owner = ''
        if len(stones) == 1:
            owner = stones.pop()
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
        from collections import deque
        visited = set()
        result = {'B': set(), 'W': set(), '': set()}
        
        for y in range(self.height):
            for x in range(self.width):
                if (x, y) in visited or self.board[y][x] != ' ':
                    continue
                
                territory_visited = set()
                queue = deque([(x, y)])
                territory_visited.add((x, y))
                stones = set()
                directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
                
                while queue:
                    cx, cy = queue.popleft()
                    for dx, dy in directions:
                        nx = cx + dx
                        ny = cy + dy
                        if 0 <= nx < self.width and 0 <= ny < self.height:
                            cell = self.board[ny][nx]
                            if cell == ' ':
                                if (nx, ny) not in territory_visited:
                                    territory_visited.add((nx, ny))
                                    queue.append((nx, ny))
                            else:
                                stones.add(cell)
                
                owner = ''
                if len(stones) == 1:
                    owner = stones.pop()
                result[owner].update(territory_visited)
                visited.update(territory_visited)
        
        return result
