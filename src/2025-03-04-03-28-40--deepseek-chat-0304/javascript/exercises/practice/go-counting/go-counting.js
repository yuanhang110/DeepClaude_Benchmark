const OWNER = {
  NONE: 'NONE',
  BLACK: 'BLACK',
  WHITE: 'WHITE'
};

export class GoCounting {
  constructor(board) {
    this.board = board.map(row => row.split(''));
    this.rows = this.board.length;
    this.cols = this.rows > 0 ? this.board[0].length : 0;
  }

  getTerritory(x, y) {
    if (x < 0 || x >= this.rows || y < 0 || y >= this.cols) {
      return { error: 'Invalid coordinate' };
    }

    if (this.board[x][y] !== ' ') {
      return { owner: OWNER.NONE, territory: [] };
    }

    const visited = new Set();
    const territory = [];
    let borders = new Set();
    
    const stack = [[x, y]];
    
    while (stack.length > 0) {
      const [cx, cy] = stack.pop();
      const key = `${cx},${cy}`;
      
      if (visited.has(key)) continue;
      visited.add(key);
      
      if (this.board[cx][cy] !== ' ') continue;
      
      territory.push([cx, cy]);
      
      // Check all 4 neighbors
      for (const [nx, ny] of this.getNeighbors(cx, cy)) {
        const cell = this.board[nx][ny];
        if (cell === 'B' || cell === 'W') {
          borders.add(cell);
        } else if (cell === ' ') {
          stack.push([nx, ny]);
        }
      }
    }
    
    // Sort territory coordinates
    territory.sort((a, b) => a[0] - b[0] || a[1] - b[1]);
    
    // Determine owner based on borders
    let owner = OWNER.NONE;
    if (borders.size === 1) {
      owner = borders.has('B') ? OWNER.BLACK : OWNER.WHITE;
    }
    
    return { owner, territory };
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
        if (this.board[x][y] === ' ' && !visited.has(`${x},${y}`)) {
          const territory = this.getTerritory(x, y);
          if (territory.owner === OWNER.BLACK) {
            territories.territoryBlack.push(...territory.territory);
          } else if (territory.owner === OWNER.WHITE) {
            territories.territoryWhite.push(...territory.territory);
          } else {
            territories.territoryNone.push(...territory.territory);
          }
          territory.territory.forEach(([tx, ty]) => visited.add(`${tx},${ty}`));
        }
      }
    }
    
    // Sort all territories
    territories.territoryBlack.sort((a, b) => a[0] - b[0] || a[1] - b[1]);
    territories.territoryWhite.sort((a, b) => a[0] - b[0] || a[1] - b[1]);
    territories.territoryNone.sort((a, b) => a[0] - b[0] || a[1] - b[1]);
    
    return territories;
  }

  getNeighbors(x, y) {
    const neighbors = [];
    if (x > 0) neighbors.push([x - 1, y]);
    if (x < this.rows - 1) neighbors.push([x + 1, y]);
    if (y > 0) neighbors.push([x, y - 1]);
    if (y < this.cols - 1) neighbors.push([x, y + 1]);
    return neighbors;
  }
}
