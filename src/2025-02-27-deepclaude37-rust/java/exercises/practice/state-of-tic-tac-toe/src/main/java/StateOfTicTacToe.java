class StateOfTicTacToe {
    enum GameState {
        ONGOING,
        DRAW,
        WIN
    }
    public GameState determineState(String[] board) {
        // Validate board structure
        if (board.length != 3) {
            throw new IllegalArgumentException("Invalid board structure");
        }
        
        // Count X's and O's
        int xCount = 0;
        int oCount = 0;
        for (String row : board) {
            if (row.length() != 3) {
                throw new IllegalArgumentException("Invalid board structure");
            }
            for (int i = 0; i < 3; i++) {
                char c = row.charAt(i);
                if (c == 'X') {
                    xCount++;
                } else if (c == 'O') {
                    oCount++;
                } else if (c != ' ') {
                    throw new IllegalArgumentException("Invalid board character");
                }
            }
        }

        // Check if turn order is valid (X starts, so X count should be equal to O count or one more)
        if (!(xCount == oCount || xCount == oCount + 1)) {
            throw new IllegalArgumentException("Invalid turn order");
        }

        // Check for wins
        boolean xWins = hasWon('X', board);
        boolean oWins = hasWon('O', board);

        // Both players can't win
        if (xWins && oWins) {
            throw new IllegalArgumentException("Both players cannot win");
        }

        // Validate X win
        if (xWins) {
            if (xCount != oCount + 1) {
                throw new IllegalArgumentException("Invalid X win state");
            }
        }

        // Validate O win
        if (oWins) {
            if (xCount != oCount) {
                throw new IllegalArgumentException("Invalid O win state");
            }
        }

        // Check if game continued after a win
        if (xWins || oWins) {
            int expectedMoves = xWins ? (2 * oCount + 1) : (2 * oCount);
            if (xCount + oCount > expectedMoves) {
                throw new IllegalArgumentException("Game continued after win");
            }
        }

        // Determine game state
        if (xWins || oWins) {
            return GameState.WIN;
        } else if (xCount + oCount == 9) {
            return GameState.DRAW;
        } else {
            return GameState.ONGOING;
        }
    }

    private boolean hasWon(char player, String[] board) {
        // Check rows
        for (int row = 0; row < 3; row++) {
            if (board[row].charAt(0) == player && 
                board[row].charAt(1) == player && 
                board[row].charAt(2) == player) {
                return true;
            }
        }

        // Check columns
        for (int col = 0; col < 3; col++) {
            if (board[0].charAt(col) == player && 
                board[1].charAt(col) == player && 
                board[2].charAt(col) == player) {
                return true;
            }
        }

        // Check diagonals
        if (board[0].charAt(0) == player && 
            board[1].charAt(1) == player && 
            board[2].charAt(2) == player) {
            return true;
        }
        if (board[0].charAt(2) == player && 
            board[1].charAt(1) == player && 
            board[2].charAt(0) == player) {
            return true;
        }

        return false;
    }
}
