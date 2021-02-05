import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class Position {
    private Piece[][] board;
    private boolean turn;
    private int move;
    private int whiteKingX = 4;
    private int whiteKingY = 0;
    private int blackKingX = 4;
    private int blackKingY = 7;
    private Position nextPosition;
    private boolean isDraw = false;
    private boolean isMate = false;

    public static final int MAX_AI_DEPTH = 2;

    public static final int WHITE_PAWN = 1;
    public static final int WHITE_ROOK = 2;
    public static final int WHITE_KNIGHT = 3;
    public static final int WHITE_BISHOP = 4;
    public static final int WHITE_KING = 5;
    public static final int WHITE_QUEEN = 6;
    public static final int WHITE_KING_MOVED = 7;
    public static final int WHITE_ROOK_MOVED = 8;
    public static final int WHITE_CAN_BE_EN_PASSANTED_PAWN = 9;
    public static final int BLACK_PAWN = 10;
    public static final int BLACK_ROOK = 11;
    public static final int BLACK_KNIGHT = 12;
    public static final int BLACK_BISHOP = 13;
    public static final int BLACK_KING = 14;
    public static final int BLACK_QUEEN = 15;
    public static final int BLACK_KING_MOVED = 16;
    public static final int BLACK_ROOK_MOVED = 17;
    public static final int BLACK_CAN_BE_EN_PASSANTED_PAWN = 18;

    public Position(boolean turn, int move) {
        this.board = new Piece[8][8];
        this.turn = turn;
        this.move = move;
    }

    public void placePiece(Piece piece) {
        board[piece.getX()][piece.getY()] = piece;
    }

    public Piece getPiece(int x, int y) {
        if (Helpers.onBoard(x, y)) {
            return board[x][y];
        }
        return null;
    }

    public int getMove() {
        return move;
    }

    public Position clone() {
        Position newPosition = new Position(turn, move);
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] != null) newPosition.placePiece(board[i][j].clone());
            }
        }
        return newPosition;
    }

    private int findPieceIndex(Piece piece){
        int index;
        boolean pieceColor = piece.color;
        if (piece instanceof Rook){
            index = pieceColor ? (((Rook)piece).hasMoved() ? WHITE_ROOK_MOVED : WHITE_ROOK) : (((Rook)piece).hasMoved() ? BLACK_ROOK_MOVED : BLACK_ROOK);
        } else if (piece instanceof Knight){
            index = pieceColor ? WHITE_KNIGHT : BLACK_KNIGHT;
        } else if (piece instanceof Bishop){
            index = pieceColor ? WHITE_BISHOP : BLACK_BISHOP;
        } else if (piece instanceof Queen){
            index = pieceColor ? WHITE_QUEEN : BLACK_QUEEN;
        } else if (piece instanceof King){
            index = pieceColor ? (((King)piece).hasMoved() ? WHITE_KING_MOVED : WHITE_KING) : (((King)piece).hasMoved() ? BLACK_KING_MOVED : BLACK_KING);
        } else {
            if(piece.color != turn && piece.y == (turn ? 4 : 3) && ((Pawn)piece).getFirstMove() == move - 1){
                index = pieceColor ? WHITE_CAN_BE_EN_PASSANTED_PAWN : BLACK_CAN_BE_EN_PASSANTED_PAWN;
            } else {
                index = pieceColor ? WHITE_PAWN : BLACK_PAWN;
            }
        }
        return index;
    }

    public int hash(int[][] table, int whiteTurn, int blackTurn){
        int hash = 0, k;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] != null){
                    k = findPieceIndex(board[i][j]);
                    hash = hash ^ table[j * board[0].length + i][k];
                }
            }
        }
        hash = hash ^ (turn ? whiteTurn : blackTurn);
        return hash;
    }

    private void updateStatus(int x, int y) {
        if (board[x][y] instanceof Pawn) {
            Pawn pawn = (Pawn) board[x][y];
            if (pawn.getFirstMove() == -1) {
                pawn.setFirstMove(move);
            }
        } else if (board[x][y] instanceof Rook) {
            Rook rook = (Rook) board[x][y];
            rook.move();
        } else if (board[x][y] instanceof King) {
            King king = (King) board[x][y];
            king.move();
            setKingPosition(x, y);
        }
    }

    private void setKingPosition(int x, int y) {
        if (turn) {
            whiteKingX = x;
            whiteKingY = y;
        } else {
            blackKingX = x;
            blackKingY = y;
        }
    }

    public Position move(int oldX, int oldY, int newX, int newY) {
        // checks if on board and if i am trying to take my own piece
        if (!Helpers.onBoard(newX, newY) || (board[newX][newY] != null && board[newX][newY].color == turn))
            return null;
        Position newPosition = this.clone();
        newPosition.board[newX][newY] = newPosition.board[oldX][oldY];
        newPosition.board[newX][newY].setCoordinates(newX, newY);
        newPosition.board[oldX][oldY] = null;
        newPosition.turn = !turn;
        newPosition.move = move + 1;
        newPosition.updateStatus(newX, newY);
        return newPosition.valid() ? newPosition : null;
    }

    public Position promote(int oldX, int oldY, int newX, int newY, Piece piece) {
        // checks if on board and if i am trying to take my own piece
        if (!Helpers.onBoard(newX, newY) || (board[newX][newY] != null && board[newX][newY].color == turn))
            return null;
        Position newPosition = this.clone();
        newPosition.board[newX][newY] = piece;
        newPosition.board[newX][newY].setCoordinates(newX, newY);
        newPosition.board[oldX][oldY] = null;
        newPosition.turn = !turn;
        newPosition.move = move + 1;
        return newPosition.valid() ? newPosition : null;
    }

    public Position enPassant(int oldX, int oldY, int newX, int newY) {
        // checks if on board and if i am trying to take my own piece
        if (!Helpers.onBoard(newX, newY) || (board[newX][newY] != null && board[newX][newY].color == turn))
            return null;
        Position newPosition = this.clone();
        newPosition.board[newX][newY] = newPosition.board[oldX][oldY];
        newPosition.board[newX][newY].setCoordinates(newX, newY);
        newPosition.board[oldX][oldY] = null;
        newPosition.board[oldX][newY] = null;
        newPosition.turn = !turn;
        newPosition.move = move + 1;
        return newPosition.valid() ? newPosition : null;
    }

    public Position castle(int kingX, int kingY, int rookX, int rookY) {
        Position newPosition = this.clone();
        if (kingX > rookX) {
            newPosition.board[kingX - 2][kingY] = newPosition.board[kingX][kingY];
            newPosition.board[kingX - 1][kingY] = newPosition.board[rookX][rookY];
            newPosition.board[kingX][kingY] = null;
            newPosition.board[rookX][rookY] = null;
        } else {
            newPosition.board[kingX + 2][kingY] = newPosition.board[kingX][kingY];
            newPosition.board[kingX + 1][kingY] = newPosition.board[rookX][rookY];
            newPosition.board[kingX][kingY] = null;
            newPosition.board[rookX][rookY] = null;
        }
        newPosition.turn = !turn;
        newPosition.move = move + 1;
        newPosition.updateStatus(kingX, kingY);
        newPosition.updateStatus(rookX, rookY);

        return newPosition;
    }

    public boolean valid() {
        int kingX = turn ? whiteKingX : blackKingX, kingY = turn ? whiteKingY : blackKingY;
        for (int k = 0; k < board[0].length; k++) {
            for (int l = 0; l < board.length; l++) {
                if (board[k][l] != null && board[k][l].color != turn && board[k][l].canReach(kingX, kingY, this))
                    return false;
            }
        }
        return true;
    }

    public boolean canBeAttacked(int x, int y) {
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null && board[i][j].color == !turn && board[i][j].canReach(x, y, this))
                    return true;
            }
        }
        return false;
    }

    public boolean hasValidMoves() {
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null && board[i][j].color == turn && !board[i][j].generatePossibleMoves(this).isEmpty())
                    return true;
            }
        }
        return false;
    }

    public boolean check() {
        int kingX = turn ? whiteKingX : blackKingX, kingY = turn ? whiteKingY : blackKingY;
        return canBeAttacked(kingX, kingY);
    }

    public void print() {
        for (int j = board.length - 1; j >= 0; j--) {
            for (int i = 0; i < board[0].length; i++) {
                if (board[i][j] == null)
                    System.out.print("__ ");
                else if (board[i][j] instanceof Pawn)
                    System.out.print(board[i][j].color ? "wP " : "bP ");
                else if (board[i][j] instanceof Bishop)
                    System.out.print(board[i][j].color ? "wB " : "bB ");
                else if (board[i][j] instanceof Knight)
                    System.out.print(board[i][j].color ? "wN " : "bN ");
                else if (board[i][j] instanceof Rook)
                    System.out.print(board[i][j].color ? "wR " : "bR ");
                else if (board[i][j] instanceof Queen)
                    System.out.print(board[i][j].color ? "wQ " : "bQ ");
                else
                    System.out.print(board[i][j].color ? "wK " : "bK ");
            }
            System.out.println();
        }
    }

    public double eval(int depth, boolean AIColor) {
        if (isMate) {
            return 200.0 * (turn == AIColor ? -1 : 1);
        } else if (isDraw) {
            return 0.0;
        }

        double sum = 0.0;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null) {
                    if (board[i][j] instanceof Pawn)
                        sum += 1.0 * (board[i][j].color == AIColor ? 1 : -1);
                    else if (board[i][j] instanceof Bishop)
                        sum += 3.0 * (board[i][j].color == AIColor ? 1 : -1);
                    else if (board[i][j] instanceof Knight)
                        sum += 3.0 * (board[i][j].color == AIColor ? 1 : -1);
                    else if (board[i][j] instanceof Rook)
                        sum += 5.0 * (board[i][j].color == AIColor ? 1 : -1);
                    else if (board[i][j] instanceof Queen)
                        sum += 9.0 * (board[i][j].color == AIColor ? 1 : -1);
                    else if (board[i][j] instanceof King)
                        sum += 105.0 * (board[i][j].color == AIColor ? 1 : -1);
                }
            }
        }
        return sum;
    }

    private ArrayList<Position> generateSuccessors() {
        ArrayList<Position> successors = new ArrayList<>();
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null && board[i][j].color == turn) {
                    successors.addAll(board[i][j].generatePossibleMoves(this));
                }
            }
        }
        return successors;
    }

    public boolean isTerminal(){
        boolean hasMoves = hasValidMoves();
        boolean isCheck = check();
        if (!hasMoves && isCheck) {
            isMate = true;
            return true;
        } else if (!hasMoves) {
            isDraw = true;
            return true;
        }
        return false;
    }

    public boolean getTurn(){
        return turn;
    }

    private boolean isTerminal(int depth) {
        return isTerminal() || depth > MAX_AI_DEPTH;
    }

    private double maxValue(Position position, int depth, double alpha, double beta, boolean AIColor) {
        if (position.isTerminal(depth)) {
            return position.eval(depth, AIColor);
        }

        double value = -Double.MAX_VALUE;
        Position nextPosition = null;
        double currentValue;

        ArrayList<Position> successors = position.generateSuccessors();
        for (Position successor : successors) {
            currentValue = minValue(successor, depth, alpha, beta, AIColor);
            if (currentValue > value) {
                nextPosition = successor;
                value = currentValue;
            }
            if (value >= beta)
                return value;
            if (value > alpha) {
                alpha = value;
            }
        }

        this.nextPosition = nextPosition;
        return value;
    }

    private double minValue(Position position, int depth, double alpha, double beta, boolean AIColor) {
        if (position.isTerminal(depth)) {
            return position.eval(depth, AIColor);
        }

        double value = Double.MAX_VALUE;
        Position nextPosition = null;
        double currentValue;

        ArrayList<Position> successors = position.generateSuccessors();
        for (Position successor : successors) {
            currentValue = maxValue(successor, depth + 1, alpha, beta, AIColor);
            if (currentValue < value) {
                nextPosition = successor;
//                nextPosition.print();
                value = currentValue;
            }
            if (value <= alpha)
                return value;
            if (value < beta) {
                beta = value;
            }
        }

        this.nextPosition = nextPosition;
        return value;
    }

    public Position minimaxDecision(boolean AIColor) {
        maxValue(this, 0, Double.MIN_VALUE, Double.MAX_VALUE, AIColor);
        return nextPosition;
    }
}
