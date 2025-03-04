export const gamestate = (board) => {
  // Convert board string to array of rows
  const rows = board.split('\n').filter(row => row.trim() !== '');
  
  // Count Xs and Os
  let xCount = 0, oCount = 0;
  for (let row of rows) {
    for (let cell of row) {
      if (cell === 'X') xCount++;
      if (cell === 'O') oCount++;
    }
  }
  
  // Check turn order validity
  if (oCount > xCount) {
    throw new Error('Wrong turn order: O started');
  }
  if (xCount > oCount + 1) {
    throw new Error('Wrong turn order: X went twice');
  }
  
  // Check for wins
  const checkWin = (player) => {
    // Check rows
    for (let row of rows) {
      if (row[0] === player && row[1] === player && row[2] === player) {
        return true;
      }
    }
    // Check columns
    for (let col = 0; col < 3; col++) {
      if (rows[0][col] === player && 
          rows[1][col] === player && 
          rows[2][col] === player) {
        return true;
      }
    }
    // Check diagonals
    if (rows[0][0] === player && 
        rows[1][1] === player && 
        rows[2][2] === player) {
      return true;
    }
    if (rows[0][2] === player && 
        rows[1][1] === player && 
        rows[2][0] === player) {
      return true;
    }
    return false;
  };
  
  const xWins = checkWin('X');
  const oWins = checkWin('O');
  
  // Check for invalid win states
  if (xWins && oWins) {
    throw new Error('Both players cannot win');
  }
  
  // Check if game continued after win
  if ((xWins || oWins) && xCount + oCount > 5) {
    throw new Error('Impossible board: game should have ended after the game was won');
  }
  
  // Determine game state
  if (xWins || oWins) {
    return 'win';
  }
  if (xCount + oCount === 9) {
    return 'draw';
  }
  return 'ongoing';
};
