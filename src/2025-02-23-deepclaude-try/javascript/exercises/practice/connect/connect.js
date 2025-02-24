//
// This is only a SKELETON file for the 'Connect' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Board {
  constructor(input) {
    if (typeof input === 'string') {
      this.grid = input.split('\n').map(line => line.trim().split(/\s+/));
    } else {
      this.grid = (input || []).map(line => line.trim().split(/\s+/));
    }
  }

  winner() {
    // Check O's win condition (top to bottom)
    const oStarts = [];
    if (this.grid.length === 0) return null;
    const firstRow = this.grid[0];
    for (let col = 0; col < firstRow.length; col++) {
      if (firstRow[col] === 'O') {
        oStarts.push([0, col]);
      }
    }
    if (this.checkWin(oStarts, (row) => row === this.grid.length - 1, 'O')) {
      return 'O';
    }

    // Check X's win condition (left to right)
    const xStarts = [];
    for (let row = 0; row < this.grid.length; row++) {
      if (this.grid[row].length > 0 && this.grid[row][0] === 'X') {
        xStarts.push([row, 0]);
      }
    }
    if (this.checkWin(xStarts, (_, col) => col === this.grid[0].length - 1, 'X')) {
      return 'X';
    }

    return null;
  }

  checkWin(starts, targetCheck, player) {
    const visited = new Set();
    const queue = [...starts];

    for (const [row, col] of queue) {
      const key = `${row},${col}`;
      if (visited.has(key)) continue;
      visited.add(key);
      if (targetCheck(row, col)) return true;

      const neighbors = this.getNeighbors(row, col);
      for (const [nr, nc] of neighbors) {
        if (this.grid[nr][nc] === player && !visited.has(`${nr},${nc}`)) {
          queue.push([nr, nc]);
        }
      }
    }
    return false;
  }

  getNeighbors(row, col) {
    const neighbors = [];
    const dirs = row % 2 === 0 
      ? [ [-1, 0], [-1, 1], [0, -1], [0, 1], [1, 0], [1, 1] ] 
      : [ [-1, -1], [-1, 0], [0, -1], [0, 1], [1, -1], [1, 0] ];

    for (const [dr, dc] of dirs) {
      const newRow = row + dr;
      const newCol = col + dc;
      if (newRow >= 0 && newRow < this.grid.length && newCol >= 0 && newCol < this.grid[newRow].length) {
        neighbors.push([newRow, newCol]);
      }
    }
    return neighbors;
  }
}
