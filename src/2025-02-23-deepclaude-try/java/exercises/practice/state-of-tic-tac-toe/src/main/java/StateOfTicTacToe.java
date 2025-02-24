class StateOfTicTacToe {
    public GameState determineState(String[] board) {
        // Count X's and O's to verify turn order
        int xCount = 0;
        int oCount = 0;
        for (String row : board) {
            for (char c : row.toCharArray()) {
                if (c == 'X') xCount++;
                if (c == 'O') oCount++;
            }
        }

        // Verify valid turn order (X starts, so X count should be equal to or one more than O count)
        if (oCount > xCount || xCount > oCount + 1) {
            throw new IllegalArgumentException("Invalid turn order");
        }

        // Check for wins
        boolean xWin = hasWon('X', board);
        boolean oWin = hasWon('O', board);

        // Check invalid states
        if ((xWin && oWin) || // Both players can't win
            (xWin && xCount <= oCount) || // If X wins, they must have more marks
            (oWin && xCount > oCount)) { // If O wins, counts must be equal
            throw new IllegalArgumentException("Invalid board state");
        }

        // Return game state
        if (xWin || oWin) {
            return GameState.WIN;
        }

        // Check if board is full (draw)
        boolean isFull = true;
        for (String row : board) {
            if (row.contains(" ")) {
                isFull = false;
                break;
            }
        }

        return isFull ? GameState.DRAW : GameState.ONGOING;
    }

    private boolean hasWon(char player, String[] board) {
        // Check rows
        for (String row : board) {
            if (row.equals("" + player + player + player)) {
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
