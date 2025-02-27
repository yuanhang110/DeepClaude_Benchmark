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
    if (this.checkXWin()) return 'X';
    if (this.checkOWin()) return 'O';
    return '';
  }

  checkXWin() {
    if (this.cols === 0) return false;
    const visited = Array.from({length: this.rows}, () => Array(this.cols).fill(false));
    const queue = [];
    for (let r = 0; r < this.rows; r++) {
      if (this.board[r][0] === 'X') {
        queue.push([r, 0]);
        visited[r][0] = true;
      }
    }
    const directions = [[-1,0], [-1,1], [0,-1], [0,1], [1,-1], [1,0]];
    while (queue.length > 0) {
      const [r, c] = queue.shift();
      if (c === this.cols - 1) return true;
      for (const [dr, dc] of directions) {
        const newR = r + dr;
        const newC = c + dc;
        if (newR >= 0 && newR < this.rows && newC >= 0 && newC < this.cols) {
          if (!visited[newR][newC] && this.board[newR][newC] === 'X') {
            visited[newR][newC] = true;
            queue.push([newR, newC]);
          }
        }
      }
    }
    return false;
  }

  checkOWin() {
    if (this.rows === 0) return false;
    const visited = Array.from({length: this.rows}, () => Array(this.cols).fill(false));
    const queue = [];
    for (let c = 0; c < this.cols; c++) {
      if (this.board[0][c] === 'O') {
        queue.push([0, c]);
        visited[0][c] = true;
      }
    }
    const directions = [[-1,0], [-1,1], [0,-1], [0,1], [1,-1], [1,0]];
    while (queue.length > 0) {
      const [r, c] = queue.shift();
      if (r === this.rows - 1) return true;
      for (const [dr, dc] of directions) {
        const newR = r + dr;
        const newC = c + dc;
        if (newR >= 0 && newR < this.rows && newC >= 0 && newC < this.cols) {
          if (!visited[newR][newC] && this.board[newR][newC] === 'O') {
            visited[newR][newC] = true;
            queue.push([newR, newC]);
          }
        }
      }
    }
    return false;
  }
}
