
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
        
        # If the coordinate is not an empty intersection, it's not territory
        if self.board[y][x] != " ":
            return NONE, set()
            
        # Find all connected empty intersections
        territory = set()
        visited = set()
        surrounding_stones = set()
        
        def explore(i, j):
            # Out of bounds or already visited
            if not (0 <= i < self.width and 0 <= j < self.height) or (i, j) in visited:
                return
                
            visited.add((i, j))
            
            # If it's a stone, add to surrounding stones and don't explore further
            if self.board[j][i] != " ":
                surrounding_stones.add(self.board[j][i])
                return
                
            # It's an empty intersection, add to territory
            territory.add((i, j))
            
            # Explore neighbors (only horizontal and vertical)
            explore(i+1, j)
            explore(i-1, j)
            explore(i, j+1)
            explore(i, j-1)
            
        # Start exploring from the given coordinate
        explore(x, y)
        
        # Determine the owner based on surrounding stones
        owner = NONE
        if surrounding_stones == {WHITE}:
            owner = WHITE
        elif surrounding_stones == {BLACK}:
            owner = BLACK
            
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
        
        # Check each intersection on the board
        for y in range(self.height):
            for x in range(self.width):
                # Skip if it's not an empty intersection or already visited
                if self.board[y][x] != " " or (x, y) in visited:
                    continue
                    
                # Find the territory and owner for this empty intersection
                owner, territory = self.territory(x, y)
                
                # Add all points in this territory to the owner's set
                result[owner].update(territory)
                
                # Mark all points in this territory as visited
                visited.update(territory)
                
        return result
