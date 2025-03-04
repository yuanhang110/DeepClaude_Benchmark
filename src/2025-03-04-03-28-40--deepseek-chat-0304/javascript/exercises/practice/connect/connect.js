export class Board {
  constructor(board) {
    this.board = board.map(row => row.replace(/ /g, ''));
    this.height = this.board.length;
    this.width = this.height > 0 ? this.board[0].length : 0;
  }

  winner() {
    // Check for O's vertical connection
    if (this.checkConnection('O', 'vertical')) {
      return 'O';
    }
    // Check for X's horizontal connection
    if (this.checkConnection('X', 'horizontal')) {
      return 'X';
    }
    return '';
  }

  checkConnection(player, direction) {
    const visited = new Set();
    const toVisit = [];
    
    // Initialize starting positions
    if (direction === 'vertical') {
      for (let x = 0; x < this.width; x++) {
        if (this.getCell(x, 0) === player) {
          toVisit.push([x, 0]);
        }
      }
    } else {
      for (let y = 0; y < this.height; y++) {
        if (this.getCell(0, y) === player) {
          toVisit.push([0, y]);
        }
      }
    }

    // Perform BFS
    while (toVisit.length > 0) {
      const [x, y] = toVisit.pop();
      const key = `${x},${y}`;
      
      if (visited.has(key)) continue;
      visited.add(key);

      // Check if reached the opposite side
      if (direction === 'vertical' && y === this.height - 1) {
        return true;
      }
      if (direction === 'horizontal' && x === this.width - 1) {
        return true;
      }

      // Add neighbors
      for (const [dx, dy] of this.getNeighbors(x, y)) {
        if (this.getCell(dx, dy) === player) {
          toVisit.push([dx, dy]);
        }
      }
    }

    return false;
  }

  getCell(x, y) {
    if (x < 0 || x >= this.width || y < 0 || y >= this.height) {
      return null;
    }
    return this.board[y][x];
  }

  getNeighbors(x, y) {
    return [
      [x - 1, y],    // left
      [x + 1, y],    // right
      [x, y - 1],    // top-left
      [x + 1, y - 1],// top-right
      [x, y + 1],    // bottom-left
      [x - 1, y + 1] // bottom-right
    ].filter(([nx, ny]) => this.getCell(nx, ny) !== null);
  }
}
