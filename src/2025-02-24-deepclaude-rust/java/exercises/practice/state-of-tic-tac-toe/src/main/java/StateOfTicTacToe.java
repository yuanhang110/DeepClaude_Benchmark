class StateOfTicTacToe {
    enum GameState {
        ONGOING,
        DRAW,
        WIN
    }
    public GameState determineState(String[] board) {
        int xCount = 0, oCount = 0;
        for (String row : board) {
            for (char c : row.toCharArray()) {
                if (c == 'X') xCount++;
                else if (c == 'O') oCount++;
            }
        }

        if (xCount != oCount && xCount != oCount + 1) {
            throw new IllegalArgumentException("Invalid turn order");
        }

        boolean xWins = isWin(board, 'X');
        boolean oWins = isWin(board, 'O');

        if (xWins && oWins) {
            throw new IllegalArgumentException("Multiple winners");
        }

        if (xWins) {
            if (xCount != oCount + 1) {
                throw new IllegalArgumentException("Invalid X win");
            }
            return GameState.WIN;
        } else if (oWins) {
            if (oCount != xCount) {
                throw new IllegalArgumentException("Invalid O win");
            }
            return GameState.WIN;
        }

        if (xCount + oCount == 9) {
            return GameState.DRAW;
        }

        return GameState.ONGOING;
    }

    private boolean isWin(String[] board, char player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i].charAt(0) == player && 
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
