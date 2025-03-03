//
// This is only a SKELETON file for the 'State of Tic Tac Toe' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

export const gamestate = (board) => {
  // Board is already a 3x3 grid
  const grid = board;

  // Count X's and O's
  let countX = 0, countO = 0;
  for (const row of grid) {
    for (const cell of row) {
      if (cell === 'X') {
        countX++;
      } else if (cell === 'O') {
        countO++;
      } else if (cell !== ' ') {
        throw new Error('Invalid character in board');
      }
    }
  }

  // Check for valid turn order
  if (countO > countX) {
    throw new Error('Wrong turn order: O started');
  }
  if (countX > countO + 1) {
    throw new Error('Wrong turn order: X went twice');
  }

  // Check for winning lines
  const xWin = hasWon('X');
  const oWin = hasWon('O');

  // Check if game continued after a win
  if (xWin && oWin) {
    throw new Error('Impossible board: game should have ended after the game was won');
  }

  // Validate win conditions
  if (xWin) {
    // Check if game continued after X won
    if (countO > countX - 1) {
      throw new Error('Impossible board: game should have ended after the game was won');
    }
    if (countX !== countO + 1) {
      throw new Error('Wrong turn order: X went twice');
    }
  }
  
  if (oWin) {
    // Check if game continued after O won
    if (countX > countO) {
      throw new Error('Impossible board: game should have ended after the game was won');
    }
    if (countX !== countO) {
      throw new Error('Wrong turn order: O started');
    }
  }

  // Determine game state
  if (xWin || oWin) {
    return 'win';
  } else if (countX + countO === 9) {
    return 'draw';
  } else {
    return 'ongoing';
  }

  // Helper function to check if a player has won
  function hasWon(player) {
    // Check rows
    for (let i = 0; i < 3; i++) {
      if (grid[i][0] === player && grid[i][1] === player && grid[i][2] === player) {
        return true;
      }
    }
    // Check columns
    for (let j = 0; j < 3; j++) {
      if (grid[0][j] === player && grid[1][j] === player && grid[2][j] === player) {
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
  }
};
