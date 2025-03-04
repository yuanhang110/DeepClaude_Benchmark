class StateOfTicTacToe {
    public enum GameState {
        ONGOING, DRAW, WIN
    }

    public GameState determineState(String[] board) {
        // Validate board is not null
        if (board == null) {
            throw new IllegalArgumentException("Board cannot be null");
        }
        
        // Validate board size and rows
        if (board.length != 3) {
            throw new IllegalArgumentException("Invalid board size");
        }
        
        for (String row : board) {
            if (row == null || row.length() != 3) {
                throw new IllegalArgumentException("Invalid board row");
            }
        }

        // Count X and O and validate characters
        int xCount = 0, oCount = 0;
        for (String row : board) {
            for (char c : row.toCharArray()) {
                if (c == 'X') {
                    xCount++;
                } else if (c == 'O') {
                    oCount++;
                } else if (c != ' ') {
                    throw new IllegalArgumentException("Invalid character on board");
                }
            }
        }

        // Validate turn order
        if (oCount > xCount || xCount > oCount + 1) {
            throw new IllegalArgumentException("Invalid turn order");
        }

        // Check for wins
        boolean xWins = checkWin(board, 'X');
        boolean oWins = checkWin(board, 'O');

        // Validate no double win
        if (xWins && oWins) {
            throw new IllegalArgumentException("Both players cannot win");
        }

        // Check if game continued after win
        if ((xWins || oWins) && (xCount + oCount != 9)) {
            if ((xWins && xCount != oCount + 1) || (oWins && xCount != oCount)) {
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

    private boolean checkWin(String[] board, char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i] != null && 
                board[i].charAt(0) == player && 
                board[i].charAt(1) == player && 
                board[i].charAt(2) == player) {
                return true;
            }
        }

        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[0].charAt(i) == player && 
                board[1].charAt(i) == player && 
                board[2].charAt(i) == player) {
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
