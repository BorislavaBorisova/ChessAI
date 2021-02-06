import java.util.ArrayList;
import java.util.stream.Collectors;

public class Position {
    private Piece[][] board;
    private boolean turn;
    private int move;
    private int whiteKingX = 4;
    private int whiteKingY = 0;
    private int blackKingX = 4;
    private int blackKingY = 7;
    private Move nextMove;
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

    public Piece[][] getBoard(){
        return board;
    }

    public Position clone() {
        Position newPosition = new Position(turn, move);
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null)
                    newPosition.placePiece(board[i][j].clone());
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

    public Position move(Move move) {
        if (move.isCapture()) {
            Piece pieceToBeTaken = move.getPieceToBeTaken();
            board[pieceToBeTaken.getX()][pieceToBeTaken.getY()] = null;
        }

        Piece pieceToMove = move.getPieceToMove();
        board[move.getX()][move.getY()] = !move.isPromotion()
                ? board[pieceToMove.getX()][pieceToMove.getY()]
                : move.getPromotedPiece();
        board[move.getX()][move.getY()].setCoordinates(move.getX(), move.getY());

        board[pieceToMove.getX()][pieceToMove.getY()] = null;

        if (move.isCastle()) {
            Rook castlingRook = move.getCastlingRook();
            board[castlingRook.getX()][castlingRook.getY()].setCoordinates(move.getX(), move.getY());
            updateStatus(castlingRook.getX(), castlingRook.getY());
            board[castlingRook.getX() < pieceToMove.getX() ? move.getX() + 1 : move.getX() - 1][move
                    .getY()] = board[castlingRook.getX()][castlingRook.getY()];
            board[castlingRook.getX()][castlingRook.getY()] = null;
        }

        turn = !turn;
        this.move = this.move + 1;
        updateStatus(move.getX(), move.getY());

        return this.valid() ? this : null;
    }

    public Position undoMove(Move move) {
        board[move.getX()][move.getY()] = null;
        
        if (move.isCapture()) {
            Piece takenPiece = move.getPieceToBeTaken();
            board[takenPiece.getX()][takenPiece.getY()] = takenPiece.clone();
        }
        
        Piece movedPiece = move.getPieceToMove();
        board[movedPiece.getX()][movedPiece.getY()] = movedPiece.clone();
        
        if(move.isCastle()) {
            Rook castlingRook = move.getCastlingRook();
            int rookX = castlingRook.getX() < movedPiece.getX() ? move.getX() + 1 : move.getX() - 1;
            board[rookX][move.getY()] = null;
            board[castlingRook.getX()][castlingRook.getY()] = castlingRook.clone();
        }

        turn = !turn;
        this.move = this.move - 1;

        return this;
    }

    public boolean valid() {
        int kingX = turn ? whiteKingX : blackKingX, kingY = turn ? whiteKingY : blackKingY;
        return canBeAttacked(kingX, kingX);
    }

    private boolean isOpponentKnight(int x, int y){
        return Helpers.onBoard(x, y) && board[x][y] != null && board[x][y] instanceof Knight && board[x][y].color != turn;
    }

    private boolean isOpponentPawn(int x, int y){
        return Helpers.onBoard(x, y) && board[x][y] != null && board[x][y] instanceof Pawn && board[x][y].color != turn;
    }

    public boolean canBeAttacked(int x, int y) {
//        for (int i = 0; i < board[0].length; i++) {
//            for (int j = 0; j < board.length; j++) {
//                if (board[i][j] != null && board[i][j].color == !turn && board[i][j].canReach(x, y, this))
//                    return true;
//            }
//        }

        int newX = x, newY = y;
        do{
            newX++; newY++;
            if(board[newX][newY] != null && board[newX][newY].color != turn && (board[newX][newY] instanceof Queen || board[newX][newY] instanceof Bishop)) return true;
        } while(Helpers.onBoard(newX, newY) && board[newX][newY] != null);

        newX = x; newY = y;
        do{
            newX++; newY--;
            if(board[newX][newY] != null && board[newX][newY].color != turn && (board[newX][newY] instanceof Queen || board[newX][newY] instanceof Bishop)) return true;
        } while(Helpers.onBoard(newX, newY) && board[newX][newY] != null);

        newX = x; newY = y;
        do{
            newX--; newY++;
            if(board[newX][newY] != null && board[newX][newY].color != turn && (board[newX][newY] instanceof Queen || board[newX][newY] instanceof Bishop)) return true;
        } while(Helpers.onBoard(newX, newY) && board[newX][newY] != null);

        newX = x; newY = y;
        do{
            newX--; newY--;
            if(board[newX][newY] != null && board[newX][newY].color != turn && (board[newX][newY] instanceof Queen || board[newX][newY] instanceof Bishop)) return true;
        } while(Helpers.onBoard(newX, newY) && board[newX][newY] != null);

        newX = x; newY = y;
        do{
            newX++;
            if(board[newX][newY] != null && board[newX][newY].color != turn && (board[newX][newY] instanceof Queen || board[newX][newY] instanceof Rook)) return true;
        } while(Helpers.onBoard(newX, newY) && board[newX][newY] != null);

        newX = x; newY = y;
        do{
            newX--;
            if(board[newX][newY] != null && board[newX][newY].color != turn && (board[newX][newY] instanceof Queen || board[newX][newY] instanceof Rook)) return true;
        } while(Helpers.onBoard(newX, newY) && board[newX][newY] != null);

        newX = x; newY = y;
        do{
            newY++;
            if(board[newX][newY] != null && board[newX][newY].color != turn && (board[newX][newY] instanceof Queen || board[newX][newY] instanceof Rook)) return true;
        } while(Helpers.onBoard(newX, newY) && board[newX][newY] != null);

        newX = x; newY = y;
        do{
            newY++;
            if(board[newX][newY] != null && board[newX][newY].color != turn && (board[newX][newY] instanceof Queen || board[newX][newY] instanceof Rook)) return true;
        } while(Helpers.onBoard(newX, newY) && board[newX][newY] != null);

        if(isOpponentKnight(x + 2, y + 1) || isOpponentKnight(x + 2, y - 1) ||
            isOpponentKnight(x - 2, y + 1) || isOpponentKnight(x - 2, y - 1) ||
            isOpponentKnight(x + 1, y + 2) || isOpponentKnight(x - 1, y + 2) ||
            isOpponentKnight(x + 1, y - 2) || isOpponentKnight(x - 1, y - 2)) return true;

        if((turn && (isOpponentPawn(x + 1, y + 1) || isOpponentPawn(x - 1, y + 1))) ||
            (!turn && (isOpponentPawn(x + 1, y - 1) || isOpponentPawn(x - 1, y - 1)))) return true;

        return false;
    }

    public boolean hasValidMoves() {
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null && board[i][j].color == turn
                        && !board[i][j].generatePossibleMoves(this).isEmpty())
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

    private ArrayList<Move> generateSuccessors() {
        ArrayList<Move> successors = new ArrayList<>();
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] != null && board[i][j].color == turn) {
                    successors.addAll(board[i][j].generatePossibleMoves(this));
                }
            }
        }

        return new ArrayList<>(successors.stream().filter(move -> Helpers.onBoard(move.getX(), move.getY()) &&
                (board[move.getX()][move.getY()] == null || board[move.getX()][move.getY()].color != turn))
                .collect(Collectors.toList()));
    }

    public boolean isTerminal() {
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

    public boolean getTurn() {
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
        Move nextMove = null;
        double currentValue;

        ArrayList<Move> successors = position.generateSuccessors();
        for (Move successor : successors) {
            currentValue = minValue(position.move(successor), depth, alpha, beta, AIColor);
            if (currentValue > value) {
                nextMove = successor;
                value = currentValue;
            }
            if (value >= beta)
                return value;
            if (value > alpha) {
                alpha = value;
            }
            position.undoMove(successor);
        }

        this.nextMove = nextMove;
        return value;
    }

    private double minValue(Position position, int depth, double alpha, double beta, boolean AIColor) {
        if (position.isTerminal(depth)) {
            return position.eval(depth, AIColor);
        }

        double value = Double.MAX_VALUE;
        Move nextMove = null;
        double currentValue;

        ArrayList<Move> successors = position.generateSuccessors();
        for (Move successor : successors) {
            currentValue = maxValue(position.move(successor), depth + 1, alpha, beta, AIColor);
            if (currentValue < value) {
                nextMove = successor;
                value = currentValue;
            }
            if (value <= alpha)
                return value;
            if (value < beta) {
                beta = value;
            }
            position.undoMove(successor);
        }

        this.nextMove = nextMove;
        return value;
    }

    public Position minimaxDecision(boolean AIColor) {
        maxValue(this, 0, Double.MIN_VALUE, Double.MAX_VALUE, AIColor);
        return this.move(nextMove);
    }
}
