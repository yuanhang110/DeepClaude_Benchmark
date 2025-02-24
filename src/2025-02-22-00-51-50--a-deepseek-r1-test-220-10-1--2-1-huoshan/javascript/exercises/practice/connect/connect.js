export class Board {
  constructor(input) {
    this.board = input.map(row => row.trim().split(/\s+/));
    this.rows = this.board.length;
    this.cols = this.rows > 0 ? this.board[0].length : 0;
  }

  winner() {
    if (this.rows === 0 || this.cols === 0) return '';

    // Check O's top-to-bottom connection
    for (let c = 0; c < this.cols; c++) {
      if (this.board[0][c] === 'O' && this.bfs(0, c, 'O', (r, _) => r === this.rows - 1)) {
        return 'O';
      }
    }

    // Check X's left-to-right connection
    for (let r = 0; r < this.rows; r++) {
      if (this.board[r][0] === 'X' && this.bfs(r, 0, 'X', (_, c) => c === this.cols - 1)) {
        return 'X';
      }
    }

    return '';
  }

  bfs(startR, startC, player, isGoal) {
    const visited = Array.from({ length: this.rows }, () => 
      Array(this.cols).fill(false)
    );
    const queue = [[startR, startC]];
    visited[startR][startC] = true;

    const directions = [
      [-1, 0], [-1, 1],  // North, Northeast
      [0, -1], [0, 1],   // West, East
      [1, -1], [1, 0]    // Southwest, South
    ];

    while (queue.length > 0) {
      const [r, c] = queue.shift();
      // Check if we reached the correct edge for the player
      if (player === 'O' && r === this.rows - 1) return true;
      if (player === 'X' && c === this.cols - 1) return true;

      for (const [dr, dc] of directions) {
        const nr = r + dr;
        const nc = c + dc;
        if (nr >= 0 && nr < this.rows && nc >= 0 && nc < this.cols) {
          if (!visited[nr][nc] && this.board[nr][nc] === player) {
            visited[nr][nc] = true;
            queue.push([nr, nc]);
          }
        }
      }
    }
    return false;
  }
}
