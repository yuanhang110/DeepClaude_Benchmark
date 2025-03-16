# Constants representing players and empty territories
WHITE = 'W'
BLACK = 'B'
NONE = ''

class Board:
    """Count territories of each player in a Go game

    Args:
        board (list[str]): A two-dimensional Go board
    """

    def __init__(self, board):
        self.board = [list(row) for row in board]
        self.height = len(board)
        self.width = len(board[0]) if self.height > 0 else 0

    def _get_territory(self, x, y):
        """Helper method to get the territory, edge status, and adjacent colors for a coordinate."""
        if x < 0 or x >= self.width or y < 0 or y >= self.height:
            raise ValueError("Invalid coordinate")
        
        if self.board[y][x] != ' ':
            return set(), False, set()
        
        directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]
        queue = [(x, y)]
        visited = {(x, y)}
        territory = {(x, y)}
        edge_found = False
        adjacent_colors = set()
        
        while queue:
            current_x, current_y = queue.pop(0)
            for dx, dy in directions:
                nx = current_x + dx
                ny = current_y + dy
                if nx < 0 or nx >= self.width or ny < 0 or ny >= self.height:
                    edge_found = True
                else:
                    cell = self.board[ny][nx]
                    if cell == ' ':
                        if (nx, ny) not in visited:
                            visited.add((nx, ny))
                            queue.append((nx, ny))
                            territory.add((nx, ny))
                    else:
                        adjacent_colors.add(cell)
        
        return territory, edge_found, adjacent_colors

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
            raise ValueError("Invalid coordinate")
            
        if self.board[y][x] != ' ':
            return ('', set())
            
        territory, edge_found, adjacent_colors = self._get_territory(x, y)
        
        owner = ''
        if not edge_found and len(adjacent_colors) == 1:
            owner = adjacent_colors.pop()
            
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
        territories_dict = {'W': set(), 'B': set(), '': set()}
        visited = set()
        
        for y in range(self.height):
            for x in range(self.width):
                if self.board[y][x] == ' ' and (x, y) not in visited:
                    territory, edge_found, adjacent_colors = self._get_territory(x, y)
                    owner = ''
                    if not edge_found and len(adjacent_colors) == 1:
                        owner = adjacent_colors.pop()
                    
                    territories_dict[owner].update(territory)
                    visited.update(territory)
        
        return territories_dict
