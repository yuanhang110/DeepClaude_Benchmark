export const gamestate = (board) => {
  // Parse board into 2D array
  const rows = board.split('\n').filter(row => !row.includes('___') && row.trim() !== '');
  const grid = rows.map(row => 
    row.split('|').map(cell => cell.trim())
  );

  // Count X's and O's
  let xCount = 0, oCount = 0;
  for (let i = 0; i < grid.length; i++) {
    for (let j = 0; j < grid[i].length; j++) {
      if (grid[i][j] === 'X') xCount++;
      if (grid[i][j] === 'O') oCount++;
    }
  }

  // Validate turn order
  if (oCount > xCount) {
    throw new Error('Wrong turn order: O started');
  }
  if (xCount > oCount + 1) {
    throw new Error('Wrong turn order: X went twice');
  }

  // Check for wins
  const checkWin = (player) => {
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

  const xWin = checkWin('X');
  const oWin = checkWin('O');

  // Validate game didn't continue after win
  if ((xWin || oWin) && xCount + oCount > 5) {
    throw new Error('Impossible board: game should have ended after the game was won');
  }

  // Return game state
  if (xWin || oWin) {
    return 'win';
  }
  if (xCount + oCount === 9) {
    return 'draw';
  }
  return 'ongoing';
};
