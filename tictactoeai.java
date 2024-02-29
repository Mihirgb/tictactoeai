import java.util.*;
class tictactoeai {
    private char[][] board;
    private char aiPlayer;
    private char humanPlayer;

    public tictactoeai(char aiPlayer, char humanPlayer) {
        this.board = new char[3][3];
        this.aiPlayer = aiPlayer;
        this.humanPlayer = humanPlayer;
        initializeBoard();
    }

    public void initializeBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = '-';
            }
        }
    }

    public boolean isBoardFull() {
        boolean isFull = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    isFull = false;
                }
            }
        }
        return isFull;
    }

    public boolean checkForWin(char player) {
        return (checkRowsForWin(player) || checkColumnsForWin(player) || checkDiagonalsForWin(player));
    }

    private boolean checkRowsForWin(char player) {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board[i][0], board[i][1], board[i][2], player) == true) {
                return true;
            }
        }
        return false;
    }

    private boolean checkColumnsForWin(char player) {
        for (int i = 0; i < 3; i++) {
            if (checkRowCol(board[0][i], board[1][i], board[2][i], player) == true) {
                return true;
            }
        }
        return false;
    }

    private boolean checkDiagonalsForWin(char player) {
        return ((checkRowCol(board[0][0], board[1][1], board[2][2], player) == true) || (checkRowCol(board[0][2], board[1][1], board[2][0], player) == true));
    }

    private boolean checkRowCol(char c1, char c2, char c3, char player) {
        return ((c1 == player) && (c1 == c2) && (c2 == c3));
    }

    public boolean placeMark(int row, int col, char player) {
        if ((row >= 0) && (row < 3)) {
            if ((col >= 0) && (col < 3)) {
                if (board[row][col] == '-') {
                    board[row][col] = player;
                    return true;
                }
            }
        }
        return false;
    }

    public int[] getBestMove() {
        int[] bestMove = new int[2];
        int bestValue = Integer.MIN_VALUE;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == '-') {
                    board[i][j] = aiPlayer;
                    int moveValue = minimax(0, false);
                    board[i][j] = '-';
                    if (moveValue > bestValue) {
                        bestValue = moveValue;
                        bestMove[0] = i;
                        bestMove[1] = j;
                    }
                }
            }
        }
        return bestMove;
    }
    public void printBoard() {
        System.out.println("-------------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j] + " | ");
            }
            System.out.println();
            System.out.println("-------------");
        }
    }
    
    
    private int minimax(int depth, boolean isMaximizingPlayer) {
        if (checkForWin(aiPlayer)) {
            return 10 - depth;
        } else if (checkForWin(humanPlayer)) {
            return depth - 10;
        } else if (isBoardFull()) {
            return 0;
        }
    
        if (isMaximizingPlayer==true) {
            int bestValue = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '-') {
                        board[i][j] = aiPlayer;
                        int moveValue = minimax(depth + 1, false);
                        board[i][j] = '-';
                        bestValue = Math.max(bestValue, moveValue);
                        
                    }
                }
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (board[i][j] == '-') {
                        board[i][j] = humanPlayer;
                        int moveValue = minimax(depth + 1, true);
                        board[i][j] = '-';
                        bestValue = Math.min(bestValue, moveValue);
                    }
                }
            }
            return bestValue;
        } 
    }

    public static void main(String[] args) {
        tictactoeai game = new tictactoeai('O', 'X'); 
        Scanner scanner = new Scanner(System.in);

        // main game loop
        while (!game.isBoardFull() && !game.checkForWin(game.humanPlayer) && !game.checkForWin(game.aiPlayer)) {
            game.printBoard(); 
            System.out.println("Your turn, enter row and column (0-2):");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (game.placeMark(row, col, game.humanPlayer)) {
                if (game.checkForWin(game.humanPlayer)) {
                    game.printBoard();
                    System.out.println("Congratulations! You won.");
                    return;
                }
            } else {
                System.out.println("Invalid move, try again.");
                continue;
            }

            int[] aiMove = game.getBestMove();
            if (game.placeMark(aiMove[0], aiMove[1], game.aiPlayer)) {
                // check for a win
                if (game.checkForWin(game.aiPlayer)) {
                    game.printBoard();  

                    System.out.println("\nSorry, you lost.\nAI WON. Better luck next time!");
                    return;
                }
            }
        }

        if(!game.checkForWin(game.humanPlayer)&&!game.checkForWin(game.aiPlayer)) {
        	game.printBoard();
        System.out.println("TIED GAME");
        }
    }
}
