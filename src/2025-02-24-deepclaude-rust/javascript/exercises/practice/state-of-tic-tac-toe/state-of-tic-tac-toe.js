//
// This is only a SKELETON file for the 'State of Tic Tac Toe' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const gamestate = (board) => {
  // Filter out separator lines and validate row count
  const gameRows = board.filter(row => !row.includes('___'));
  if (gameRows.length !== 3) {
    throw new Error('Invalid board');
  }

  // Parse board into grid
  const grid = gameRows.map(row => {
    // Trim entire row to handle leading/trailing pipes
    const cells = row.trim().split('|').map(cell => cell.trim()).filter(cell => cell !== '');
    if (cells.length !== 3) {
      throw new Error('Invalid board');
    }
    return cells;
  });

  // Validate grid dimensions (redundant check but safe)
  if (grid.some(row => row.length !== 3)) {
    throw new Error('Invalid board');
  }

  // Count X's and O's
  let xCount = 0;
  let oCount = 0;
  for (const row of grid) {
    for (const cell of row) {
      if (cell === 'X') xCount++;
      else if (cell === 'O') oCount++;
      else if (cell !== '') {
        throw new Error('Invalid cell');
      }
    }
  }

  // Validate turn order with specific error messages
  if (xCount < oCount) {
    throw new Error('Wrong turn order: O started');
  }
  if (xCount > oCount + 1) {
    throw new Error('Wrong turn order: X went twice');
  }

  // Check for wins
  const hasWin = (player) => {
    // Check rows
    for (let i = 0; i < 3; i++) {
      if (grid[i][0] === player && grid[i][1] === player && grid[i][2] === player) {
        return true;
      }
    }

    // Check columns
    for (let i = 0; i < 3; i++) {
      if (grid[0][i] === player && grid[1][i] === player && grid[2][i] === player) {
        return true;
      }
    }

    // Check diagonals
    if (grid[0][0] === player && grid[1][1] === player && grid[2][2] === player) {
      return true;
    }
    if (grid[0][2] === player && grid[1][1] === player && grid[2][0] === player) {
      return true;
    }

    return false;
  };

  const xWin = hasWin('X');
  const oWin = hasWin('O');

  // Check for multiple winners or continued play after win
  if (xWin && oWin) {
    throw new Error('Impossible board: game should have ended after the game was won');
  }

  // Validate win conditions
  if (xWin && xCount !== oCount + 1) {
    throw new Error('Impossible board: game should have ended after the game was won');
  }
  if (oWin && oCount !== xCount) {
    throw new Error('Impossible board: game should have ended after the game was won');
  }

  // Determine game state
  if (xWin || oWin) {
    return 'win';
  } else if (xCount + oCount === 9) {
    return 'draw';
  } else {
    return 'ongoing';
  }
};
