# Constants for player colors and empty spaces
WHITE = "W"
BLACK = "B"
NONE = ""

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
        # Check if coordinates are valid
        if not (0 <= x < self.width and 0 <= y < self.height):
            raise ValueError("Invalid coordinate")
            
        # If the position is already occupied by a stone, it's not a territory
        if self.board[y][x] in (WHITE, BLACK):
            return NONE, set()
            
        # Use breadth-first search to find all connected empty spaces
        visited = set()
        territory = set()
        queue = [(x, y)]
        surrounding_stones = set()
        
        while queue:
            current_x, current_y = queue.pop(0)
            
            if (current_x, current_y) in visited:
                continue
                
            visited.add((current_x, current_y))
            
            # If this is an empty space, add to territory and explore neighbors
            if self.board[current_y][current_x] == " ":
                territory.add((current_x, current_y))
                
                # Check all four adjacent positions
                for nx, ny in [(current_x+1, current_y), (current_x-1, current_y), 
                               (current_x, current_y+1), (current_x, current_y-1)]:
                    if 0 <= nx < self.width and 0 <= ny < self.height:
                        if self.board[ny][nx] == " " and (nx, ny) not in visited:
                            queue.append((nx, ny))
                        elif self.board[ny][nx] != " ":
                            surrounding_stones.add(self.board[ny][nx])
        
        # Determine the owner of the territory
        if len(surrounding_stones) == 1:
            owner = next(iter(surrounding_stones))
        else:
            owner = NONE
            
        return owner, territory

    def territories(self):
        """Find the owners and the territories of the whole board

        Args:
            none

        Returns:
            dict(str, set): A dictionary whose key being the owner
                        , i.e. "W", "B", "".  The value being a set
                        of coordinates owned by the owner.
        """
        result = {WHITE: set(), BLACK: set(), NONE: set()}
        visited = set()
        
        # Check each position on the board
        for y in range(self.height):
            for x in range(self.width):
                # Skip if already visited or if it's a stone
                if (x, y) in visited or self.board[y][x] in (WHITE, BLACK):
                    continue
                
                # Find the territory starting from this point
                owner, territory = self.territory(x, y)
                
                # Add all points in this territory to the result and mark as visited
                result[owner].update(territory)
                visited.update(territory)
                
        return result
