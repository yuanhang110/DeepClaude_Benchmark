export const gamestate = (board) => {
  let xCount = 0, oCount = 0;
  for (const row of board) {
    for (const cell of row) {
      if (cell === 'X') xCount++;
      else if (cell === 'O') oCount++;
    }
  }

  if (xCount !== oCount && xCount !== oCount + 1) {
    if (oCount > xCount) {
      throw new Error('Wrong turn order: O started');
    } else {
      throw new Error('Wrong turn order: X went twice');
    }
  }

  const hasWon = (player) => {
    // Check rows
    for (const row of board) {
      if (row[0] === player && row[1] === player && row[2] === player) {
        return true;
      }
    }

    // Check columns
    for (let col = 0; col < 3; col++) {
      if (board[0][col] === player && board[1][col] === player && board[2][col] === player) {
        return true;
      }
    }

    // Check diagonals
    if (board[0][0] === player && board[1][1] === player && board[2][2] === player) {
      return true;
    }
    if (board[0][2] === player && board[1][1] === player && board[2][0] === player) {
      return true;
    }

    return false;
  };

  const xWon = hasWon('X');
  const oWon = hasWon('O');

  if (xWon && oWon) {
    throw new Error('Impossible board: game should have ended after the game was won');
  }

  if (xWon && xCount !== oCount + 1) {
    throw new Error('Impossible board: game should have ended after the game was won');
  }

  if (oWon && xCount !== oCount) {
    throw new Error('Impossible board: game should have ended after the game was won');
  }

  const isBoardFull = () => {
    for (const row of board) {
      for (const cell of row) {
        if (cell === ' ') {
          return false;
        }
      }
    }
    return true;
  };

  if (xWon || oWon) {
    return 'win';
  } else if (isBoardFull()) {
    return 'draw';
  } else {
    return 'ongoing';
  }
};
