//
// This is only a SKELETON file for the 'Queen Attack' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export class QueenAttack {
  constructor({
    black: [blackRow, blackColumn] = [0, 3],    // Default black at (0,3) - a8
    white: [whiteRow, whiteColumn] = [7, 3],     // Default white at (7,3) - d1
  } = {}) {
    
    // Validate positions are on the board
    const isValid = (r, c) => r >= 0 && r < 8 && c >= 0 && c < 8;
    if (!isValid(blackRow, blackColumn) || !isValid(whiteRow, whiteColumn)) {
      throw new Error('Queen must be placed on the board');
    }
    
    // Check for overlapping positions
    if (blackRow === whiteRow && blackColumn === whiteColumn) {
      throw new Error('Queens cannot share the same space');
    }
    
    // Store positions
    this.black = [blackRow, blackColumn];
    this.white = [whiteRow, whiteColumn];
  }

  toString() {
    const board = Array.from({length: 8}, () => Array(8).fill('_'));
    
    // Place queens on board
    const [bRow, bCol] = this.black;
    const [wRow, wCol] = this.white;
    board[bRow][bCol] = 'B';
    board[wRow][wCol] = 'W';
    
    // Convert to string representation
    return board.map(row => row.join(' ')).join('\n');
  }

  get canAttack() {
    const [bRow, bCol] = this.black;
    const [wRow, wCol] = this.white;
    
    // Same row or column
    if (bRow === wRow || bCol === wCol) return true;
    
    // Diagonal check (absolute difference of rows equals absolute difference of columns)
    return Math.abs(bRow - wRow) === Math.abs(bCol - wCol);
  }
}
