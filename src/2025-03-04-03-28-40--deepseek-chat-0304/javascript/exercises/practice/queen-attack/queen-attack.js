export class QueenAttack {
  constructor({
    black: [blackRow, blackColumn] = [0, 3],
    white: [whiteRow, whiteColumn] = [7, 3],
  } = {}) {
    // Validate positions
    if (blackRow < 0 || blackRow > 7 || blackColumn < 0 || blackColumn > 7 ||
        whiteRow < 0 || whiteRow > 7 || whiteColumn < 0 || whiteColumn > 7) {
      throw new Error('Queen must be placed on the board');
    }
    
    // Check if queens are on same square
    if (blackRow === whiteRow && blackColumn === whiteColumn) {
      throw new Error('Queens cannot share the same space');
    }
    
    this.black = [blackRow, blackColumn];
    this.white = [whiteRow, whiteColumn];
  }

  toString() {
    const board = Array.from({length: 8}, () => Array(8).fill('_'));
    
    // Place queens
    board[this.black[0]][this.black[1]] = 'B';
    board[this.white[0]][this.white[1]] = 'W';
    
    // Convert to string representation
    return board.map(row => row.join(' ')).join('\n');
  }

  get canAttack() {
    const [blackRow, blackCol] = this.black;
    const [whiteRow, whiteCol] = this.white;
    
    // Check same row or column
    if (blackRow === whiteRow || blackCol === whiteCol) {
      return true;
    }
    
    // Check diagonals
    return Math.abs(blackRow - whiteRow) === Math.abs(blackCol - whiteCol);
  }
}
