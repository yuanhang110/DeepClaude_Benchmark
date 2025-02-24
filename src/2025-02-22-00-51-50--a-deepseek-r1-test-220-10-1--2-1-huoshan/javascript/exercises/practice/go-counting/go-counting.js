const NONE = 'NONE';
const BLACK = 'BLACK';
const WHITE = 'WHITE';
const DIRECTIONS = [[0, 1], [1, 0], [0, -1], [-1, 0]];

export class GoCounting {
  constructor(board) {
    this.board = board.map(row => row.split(''));
    this.rows = board.length;
    this.cols = this.rows > 0 ? board[0].length : 0;
  }

  isValid(x, y) {
    return x >= 0 && x < this.rows && y >= 0 && y < this.cols;
  }

  getAdjacentStones(x, y) {
    const stones = new Set();
    for (const [dx, dy] of DIRECTIONS) {
      const nx = x + dx, ny = y + dy;
      if (this.isValid(nx, ny)) {
        const cell = this.board[nx][ny];
        if (cell === 'B' || cell === 'W') stones.add(cell);
      }
    }
    return stones;
  }

  getTerritory(x, y) {
    if (!this.isValid(x, y)) {
      return { error: 'Invalid coordinate' };
    }
    if (this.board[x][y] !== ' ') {
      return { owner: NONE, territory: [] };
    }

    const visited = new Set();
    const queue = [[x, y]];
    const territory = [];
    const adjacentColors = new Set();

    while (queue.length > 0) {
      const [cx, cy] = queue.shift();
      const key = `${cx},${cy}`;
      
      if (visited.has(key)) continue;
      
      visited.add(key);
      territory.push([cx, cy]);
      
      const neighbors = this.getAdjacentStones(cx, cy);
      neighbors.forEach(color => adjacentColors.add(color));

      for (const [dx, dy] of DIRECTIONS) {
        const nx = cx + dx, ny = cy + dy;
        if (this.isValid(nx, ny) && this.board[nx][ny] === ' ' && !visited.has(`${nx},${ny}`)) {
          queue.push([nx, ny]);
        }
      }
    }

    let owner = NONE;
    const isEdgeTerritory = Array.from(visited).some(pos => {
      const [x, y] = pos.split(',').map(Number);
      return x === 0 || x === this.rows - 1 || y === 0 || y === this.cols - 1;
    });
    
    if (!isEdgeTerritory && adjacentColors.size === 1) {
      owner = adjacentColors.has('B') ? BLACK : WHITE;
    }

    return { owner, territory: territory.sort((a, b) => 
      a[0] - b[0] || a[1] - b[1]) };
  }

  getTerritories() {
    const territories = {
      territoryBlack: [],
      territoryWhite: [],
      territoryNone: []
    };
    const visited = new Set();

    for (let x = 0; x < this.rows; x++) {
      for (let y = 0; y < this.cols; y++) {
        const key = `${x},${y}`;
        if (this.board[x][y] === ' ' && !visited.has(key)) {
          const { owner, territory } = this.getTerritory(x, y);
          territory.forEach(([tx, ty]) => visited.add(`${tx},${ty}`));
          
          const target = owner === BLACK ? 'territoryBlack' :
                        owner === WHITE ? 'territoryWhite' : 'territoryNone';
          territories[target].push(...territory);
        }
      }
    }

    // Sort all territories
    for (const key in territories) {
      territories[key].sort((a, b) => a[0] - b[0] || a[1] - b[1]);
    }

    return territories;
  }
}
