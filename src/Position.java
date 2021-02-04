import java.util.ArrayList;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

public class Position {
    private Piece[][] board;
    private boolean turn;
    private int move = 0;
    private int whiteKingX = 4;
    private int whiteKingY = 0;
    private int blackKingX = 4;
    private int blackKingY = 7;
    private Position nextPosition;
    private boolean isDraw = false;
    private boolean isMate = false;

    public static final int MAX_AI_DEPTH = 2;

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

    public double eval(int depth) {
        if (isMate) {
            return 200.0;
        } else if (isDraw) {
            return 0.0;
        }

        double sum = 0.0;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null && board[i][j].color == turn) {
                    if (board[i][j] instanceof Pawn)
                        sum += 1.0;
                    else if (board[i][j] instanceof Bishop)
                        sum += 3.0;
                    else if (board[i][j] instanceof Knight)
                        sum += 3.0;
                    else if (board[i][j] instanceof Rook)
                        sum += 5.0;
                    else if (board[i][j] instanceof Queen)
                        sum += 9.0;
                    else if (board[i][j] instanceof King)
                        sum += 105.0;
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

    private double maxValue(Position position, int depth, double alpha, double beta) {
        if (position.isTerminal(depth)) {
            return position.eval(depth);
        }

        double value = Double.MIN_VALUE;
        Position nextPosition = null;
        double currentValue;

        ArrayList<Position> successors = position.generateSuccessors();
//        for(Position a : successors) a.print();
//        System.exit(-1);
        for (Position successor : successors) {
            currentValue = minValue(successor, depth, alpha, beta);
            if (currentValue > value) {
                nextPosition = successor;
//                nextPosition.print();
                value = currentValue;
            }
            if (value >= beta)
                return value;
            if (value > alpha) {
                alpha = value;
            }
        }

        this.nextPosition = nextPosition;
//        System.out.println("**");
//        nextPosition.print();
//        System.out.println("**");
        return value;
    }

    private double minValue(Position position, int depth, double alpha, double beta) {
        if (position.isTerminal(depth)) {
            return position.eval(depth);
        }

        double value = Double.MAX_VALUE;
        Position nextPosition = null;
        double currentValue;

        ArrayList<Position> successors = position.generateSuccessors();
        for (Position successor : successors) {
            currentValue = maxValue(successor, depth + 1, alpha, beta);
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

    public Position minimaxDecision() {
        maxValue(this, 0, Double.MIN_VALUE, Double.MAX_VALUE);
        System.out.println("hui");
        return nextPosition;
    }
}
