//
// This is only a SKELETON file for the 'Connect' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class Board {
  constructor(board) {
    this.board = board.map(row => row.replace(/ /g, ''));
    this.rows = this.board.length;
    this.cols = this.rows > 0 ? this.board[0].length : 0;
  }

  winner() {
    // Check O's win condition (top to bottom)
    if (this.checkConnectivity('O', (row, col) => row === 0, (row, col) => row === this.rows - 1)) {
      return 'O';
    }

    // Check X's win condition (left to right)
    if (this.checkConnectivity('X', (row, col) => col === 0, (row, col) => col === this.cols - 1)) {
      return 'X';
    }

    return '';
  }

  checkConnectivity(player, isStart, isEnd) {
    const visited = Array.from({length: this.rows}, () => new Array(this.cols).fill(false));
    const queue = [];

    // Initialize BFS with all starting positions
    for (let row = 0; row < this.rows; row++) {
      for (let col = 0; col < this.cols; col++) {
        if (this.board[row][col] === player && isStart(row, col)) {
          queue.push([row, col]);
          visited[row][col] = true;
        }
      }
    }

    // Perform BFS
    while (queue.length > 0) {
      const [r, c] = queue.shift();
      if (isEnd(r, c)) {
        return true;
      }
      for (const [nr, nc] of this.getNeighbors(r, c)) {
        if (nr >= 0 && nr < this.rows && nc >= 0 && nc < this.cols &&
            !visited[nr][nc] && this.board[nr][nc] === player) {
          visited[nr][nc] = true;
          queue.push([nr, nc]);
        }
      }
    }

    return false;
  }

  getNeighbors(row, col) {
    const neighbors = [];
    // Directions depend on whether the row is even or odd (for hexagonal grid)
    const directions = row % 2 === 0 ?
      [[-1, -1], [-1, 0], [0, -1], [0, 1], [1, -1], [1, 0]] :  // Even row
      [[-1, 0], [-1, 1], [0, -1], [0, 1], [1, 0], [1, 1]]; // Odd row

    for (const [dr, dc] of directions) {
      const nr = row + dr;
      const nc = col + dc;
      if (nr >= 0 && nr < this.rows && nc >= 0 && nc < this.cols) {
        neighbors.push([nr, nc]);
      }
    }
    return neighbors;
  }
}
