enum GameState {
    WIN, DRAW, ONGOING
}

class StateOfTicTacToe {
    public GameState determineState(String[] board) {
        int xCount = 0;
        int oCount = 0;
        
        // Count X and O marks
        for (String row : board) {
            for (char c : row.toCharArray()) {
                if (c == 'X') xCount++;
                else if (c == 'O') oCount++;
            }
        }
        
        // Validate turn order (X starts and alternates)
        if (xCount != oCount && xCount != oCount + 1) {
            throw new IllegalArgumentException("Invalid turn order");
        }
        
        boolean xWins = hasWin(board, 'X');
        boolean oWins = hasWin(board, 'O');
        
        // Validate only one possible winner
        if (xWins && oWins) {
            throw new IllegalArgumentException("Multiple winners");
        }
        
        // Check for continued play after win
        if (xWins) {
            if (xCount != oCount + 1) {
                throw new IllegalArgumentException("Game continued after X win");
            }
            return GameState.WIN;
        }
        
        if (oWins) {
            if (xCount != oCount) {
                throw new IllegalArgumentException("Game continued after O win");
            }
            return GameState.WIN;
        }
        
        // Determine final state
        return (xCount + oCount) == 9 ? GameState.DRAW : GameState.ONGOING;
    }
    
    private boolean hasWin(String[] board, char player) {
        // Check rows and columns
        for (int i = 0; i < 3; i++) {
            if (board[i].charAt(0) == player && board[i].charAt(1) == player && board[i].charAt(2) == player) {
                return true;
            }
            if (board[0].charAt(i) == player && board[1].charAt(i) == player && board[2].charAt(i) == player) {
                return true;
            }
        }
        
        // Check diagonals
        return (board[0].charAt(0) == player && board[1].charAt(1) == player && board[2].charAt(2) == player)
            || (board[0].charAt(2) == player && board[1].charAt(1) == player && board[2].charAt(0) == player);
    }
}
