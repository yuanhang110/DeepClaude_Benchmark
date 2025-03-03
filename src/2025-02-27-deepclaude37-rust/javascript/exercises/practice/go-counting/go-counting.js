//
// This is only a SKELETON file for the 'Go Counting' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class GoCounting {
  constructor(board) {
    this.board = board.map(row => row.split(''));
    this.height = this.board.length;
    this.width = this.height > 0 ? this.board[0].length : 0;

    const visited = Array.from({ length: this.height }, () => 
      Array(this.width).fill(false)
    );
    this.territories = [];
    this.territoryMap = Array.from({ length: this.height }, () => 
      Array(this.width).fill(null)
    );

    // Process each cell on the board to find territories
    for (let y = 0; y < this.height; y++) {
      for (let x = 0; x < this.width; x++) {
        if (this.board[y][x] === ' ' && !visited[y][x]) {
          // Start a BFS to find a territory
          const queue = [[y, x]];
          visited[y][x] = true;
          const territoryCells = [];
          const adjacentOwners = new Set();

          while (queue.length > 0) {
            const [cy, cx] = queue.shift();
            territoryCells.push([y, x]); // Use the original coordinates to maintain consistency

            // Check all four adjacent directions
            const directions = [[-1, 0], [1, 0], [0, -1], [0, 1]];
            for (const [dy, dx] of directions) {
              const ny = cy + dy;
              const nx = cx + dx;
              if (ny >= 0 && ny < this.height && nx >= 0 && nx < this.width) {
                const cell = this.board[ny][nx];
                if (cell === 'B') {
                  adjacentOwners.add('B');
                } else if (cell === 'W') {
                  adjacentOwners.add('W');
                } else if (cell === ' ' && !visited[ny][nx]) {
                  visited[ny][nx] = true;
                  queue.push([ny, nx]);
                }
              }
            }
          }

          // Sort territory cells for consistent output
          territoryCells.sort((a, b) => a[0] - b[0] || a[1] - b[1]);
          
          // Determine territory owner
          let owner = 'NONE';
          if (adjacentOwners.size === 1) {
            owner = adjacentOwners.has('B') ? 'BLACK' : 'WHITE';
          }

          const territory = { owner, cells: territoryCells };
          this.territories.push(territory);
          
          // Map each cell to its territory for quick lookup
          for (const [ty, tx] of territoryCells) {
            this.territoryMap[ty][tx] = territory;
          }
        }
      }
    }
  }

  getTerritory(y, x) {
    // Handle out-of-bounds coordinates
    if (y < 0 || y >= this.height || x < 0 || x >= this.width) {
      throw new Error('Invalid coordinate');
    }

    const cell = this.board[y][x];
    if (cell !== ' ') {
      // Stone cells have no territory
      return { owner: 'NONE', territory: [] };
    }

    const territory = this.territoryMap[y][x];
    if (!territory) {
      return { owner: 'NONE', territory: [] };
    }

    return {
      owner: territory.owner,
      territory: territory.cells
    };
  }

  getTerritories() {
    const result = {
      territoryBlack: [],
      territoryWhite: [],
      territoryNone: []
    };

    // Collect all territories by owner
    for (const territory of this.territories) {
      switch (territory.owner) {
        case 'BLACK':
          result.territoryBlack.push(...territory.cells);
          break;
        case 'WHITE':
          result.territoryWhite.push(...territory.cells);
          break;
        default:
          result.territoryNone.push(...territory.cells);
          break;
      }
    }

    // Sort all territories for consistent output
    const sortCoords = (a, b) => {
      if (a[0] !== b[0]) return a[0] - b[0];
      return a[1] - b[1];
    };
    
    result.territoryBlack.sort(sortCoords);
    result.territoryWhite.sort(sortCoords);
    result.territoryNone.sort(sortCoords);

    return result;
  }
}
