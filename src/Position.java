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
    public static final double[][] KING_POSITIONAL_COEFFICIENTS= {{-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
                                                                    {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
                                                                    {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
                                                                    {-3.0, -4.0, -4.0, -5.0, -5.0, -4.0, -4.0, -3.0},
                                                                    {-2.0, -3.0, -3.0, -4.0, -4.0, -3.0, -3.0, -2.0},
                                                                    {-1.0, -2.0, -2.0, -2.0, -2.0, -2.0, -2.0, -1.0},
                                                                    {2.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 2.0},
                                                                    {2.0, 3.0, 1.0, 0.0, 0.0, 1.0, 3.0, 2.0}};
    public static final double[][] QUEEN_POSITIONAL_COEFFICIENTS= {{-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0},
                                                                    {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
                                                                    {-1.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0},
                                                                    {-0.5, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
                                                                    {0.0, 0.0, 0.5, 0.5, 0.5, 0.5, 0.0, -0.5},
                                                                    {-1.0, 0.5, 0.5, 0.5, 0.5, 0.5, 0.0, -1.0},
                                                                    {-1.0, 0.0, 0.5, 0.0, 0.0, 0.0, 0.0, -1.0},
                                                                    {-2.0, -1.0, -1.0, -0.5, -0.5, -1.0, -1.0, -2.0}};
    public static final double[][] ROOK_POSITIONAL_COEFFICIENTS= {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                                    {0.5, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 0.5},
                                                                    {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
                                                                    {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
                                                                    {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
                                                                    {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
                                                                    {-0.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -0.5},
                                                                    {0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0}};
    public static final double[][] BISHOP_POSITIONAL_COEFFICIENTS= {{-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0},
                                                                    {-1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -1.0},
                                                                    {-1.0, 0.0, 0.5, 1.0, 1.0, 0.5, 0.0, -1.0},
                                                                    {-1.0, 0.5, 0.5, 1.0, 1.0, 0.5, 0.5, -1.0},
                                                                    {-1.0, 0.0, 1.0, 1.0, 1.0, 1.0, 0.0, -1.0},
                                                                    {-1.0, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0, -1.0},
                                                                    {-1.0, 0.5, 0.0, 0.0, 0.0, 0.0, 0.5, -1.0},
                                                                    {-2.0, -1.0, -1.0, -1.0, -1.0, -1.0, -1.0, -2.0}};
    public static final double[][] KNIGHT_POSITIONAL_COEFFICIENTS= {{-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0},
                                                                    {-4.0, -2.0, 0.0, 0.0, 0.0, 0.0, -2.0, -4.0},
                                                                    {-3.0, 0.0, 1.0, 1.5, 1.5, 1.0, 0.0, -3.0},
                                                                    {-3.0, 0.5, 1.5, 2.0, 2.0, 1.5, 0.5, -3.0},
                                                                    {-3.0, 0.0, 1.5, 2.0, 2.0, 1.5, 0.0, -3.0},
                                                                    {-3.0, 0.5, 1.0, 1.5, 1.5, 1.0, 0.5, -3.0},
                                                                    {-4.0, -2.0, 0.0, 0.5, 0.5, 0.0, -2.0, -4.0},
                                                                    {-5.0, -4.0, -3.0, -3.0, -3.0, -3.0, -4.0, -5.0}};
    public static final double[][] PAWN_POSITIONAL_COEFFICIENTS= {{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0},
                                                                    {5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0, 5.0},
                                                                    {1.0, 1.0, 2.0, 3.0, 3.0, 2.0, 2.0, 1.0},
                                                                    {0.5, 0.5, 1.0, 2.5, 2.5, 1.0, 0.5, 0.5},
                                                                    {0.0, 0.0, 0.0, 2.0, 2.0, 0.0, 0.0, 0.0},
                                                                    {0.5, -0.5, -1.0, 0.0, 0.0, -1.0, -0.5, 0.5},
                                                                    {0.5, 1.0, 1.0, -2.0, -2.0, 1.0, 1.0, 0.5},
                                                                    {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}};

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
//        System.out.println("-----------------");
//        System.out.println("");
//        System.out.println("Move");
//        move.print();
//        print();
        

//        if(move.getPieceToMove().getClass() == Pawn.class && move.getPieceToMove().getY() == 1 && move.getPieceToMove().getX() == 0) {
//            print();
//            System.out.println(move.getPieceToMove().getX() + " old " + move.getPieceToMove().getY());
//            System.out.println(move.getX() + " new " + move.getY());
//            System.out.println(move.getPieceToBeTaken() != null ? move.getPieceToBeTaken().getClass() : null);
//            
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }

        if (move.isCapture()) {
            Piece pieceToBeTaken = move.getPieceToBeTaken();
            board[pieceToBeTaken.getX()][pieceToBeTaken.getY()] = null;
        }

        Piece pieceToMove = move.getPieceToMove();
        board[move.getX()][move.getY()] = !move.isPromotion()
                ? board[pieceToMove.getX()][pieceToMove.getY()]
                : move.getPromotedPiece();
//        if (board[move.getX()][move.getY()] == null) {
//            System.out.println(pieceToMove.getClass());
//            System.out.println(pieceToMove.getX() + " old " + pieceToMove.getY());
//            System.out.println(move.getX() + " new " + move.getY());
//            print();
//        }

//        if(move.getPieceToMove().getClass() == Pawn.class && move.getPieceToMove().getY() == 1 && move.getPieceToMove().getX() == 0) {
//            print();
//            
//            try {
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }

        board[move.getX()][move.getY()].setCoordinates(move.getX(), move.getY());

        board[pieceToMove.getX()][pieceToMove.getY()] = null;

        if (move.isCastle()) {
            Rook castlingRook = move.getCastlingRook();
            board[castlingRook.getX()][castlingRook.getY()].setCoordinates(
                    castlingRook.getX() < pieceToMove.getX() ? move.getX() + 1 : move.getX() - 1, move.getY());
            updateStatus(castlingRook.getX(), castlingRook.getY());
            board[castlingRook.getX() < pieceToMove.getX() ? move.getX() + 1 : move.getX() - 1][move
                    .getY()] = board[castlingRook.getX()][castlingRook.getY()];
            board[castlingRook.getX()][castlingRook.getY()] = null;
        }

        updateStatus(move.getX(), move.getY());
        turn = !turn;
        this.move = this.move + 1;
        
//        System.out.println("");
//        print();
//        if ((move.getPieceToMove().getClass() == Pawn.class && move.getPieceToMove().getX() == 3
//                && move.getPieceToMove().color) || (move.getPieceToBeTaken() != null && move.getPieceToBeTaken().color && move.getPieceToBeTaken().getClass() == Pawn.class && move.getPieceToBeTaken().getX() == 3)) {
//            try {
//                System.out.println("-----------------");
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }

//        if(board[3][3] == null) this.pleaseHelp--;

        return this.valid() ? this : null;
    }

    public Position undoMove(Move move) {
//        if(this.pleaseHelp < 0) System.exit(-1);
//        System.out.println("-----------------");
//
//        System.out.println("");
//        System.out.println("Doing undo " + (turn ? "white" : "black"));
//        move.print();
//        print();
//        boolean printlog = false;
//        if ((move.getPieceToMove().getClass() == Pawn.class && move.getPieceToMove().getX() == 3
//                && move.getPieceToMove().color) || (move.getPieceToBeTaken() != null && move.getPieceToBeTaken().color && move.getPieceToBeTaken().getClass() == Pawn.class && move.getPieceToBeTaken().getX() == 3)) {
//            printlog = true;
//        }

        board[move.getX()][move.getY()] = null;

        if (move.isCapture()) {
            Piece takenPiece = move.getPieceToBeTaken();
            board[takenPiece.getX()][takenPiece.getY()] = takenPiece.clone();
        }

        Piece movedPiece = move.getPieceToMove();
        board[movedPiece.getX()][movedPiece.getY()] = movedPiece.clone();
        if (movedPiece instanceof King)
            setKingPosition(movedPiece.getX(), movedPiece.getY());

        if (move.isCastle()) {
            Rook castlingRook = move.getCastlingRook();
            int rookX = castlingRook.getX() < movedPiece.getX() ? move.getX() + 1 : move.getX() - 1;
            board[rookX][move.getY()] = null;
            board[castlingRook.getX()][castlingRook.getY()] = castlingRook.clone();
        }

        turn = !turn;
        this.move = this.move - 1;
        
//        System.out.println("");
//        print();
//        if (printlog) {
//            try {
//                System.out.println("-----------------");
//                Thread.sleep(10000);
//            } catch (InterruptedException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
        
//        if(board[3][3] == null) this.pleaseHelp--;
        
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
                    boolean color = board[i][j].color;
                    int x = color ? i : Math.abs(7 - i);
                    int y = color ? j : Math.abs(7 - j);
                    if (board[i][j] instanceof Pawn)
                        sum += (1.0 + PAWN_POSITIONAL_COEFFICIENTS[x][y]) * (color == AIColor ? 1 : -1);
                    else if (board[i][j] instanceof Bishop)
                        sum += (3.0 + BISHOP_POSITIONAL_COEFFICIENTS[x][y]) * (color == AIColor ? 1 : -1);
                    else if (board[i][j] instanceof Knight)
                        sum += (3.0 + KNIGHT_POSITIONAL_COEFFICIENTS[x][y]) * (color == AIColor ? 1 : -1);
                    else if (board[i][j] instanceof Rook)
                        sum += (5.0 + ROOK_POSITIONAL_COEFFICIENTS[x][y]) * (color == AIColor ? 1 : -1);
                    else if (board[i][j] instanceof Queen)
                        sum += (9.0 + QUEEN_POSITIONAL_COEFFICIENTS[x][y]) * (color == AIColor ? 1 : -1);
                    else if (board[i][j] instanceof King)
                        sum += (105.0 + KING_POSITIONAL_COEFFICIENTS[x][y]) * (color == AIColor ? 1 : -1);
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
//            System.out.println("Terminating in max; Color:" + turn);
            return position.eval(depth, AIColor);
        }

        double value = -Double.MAX_VALUE;
        Move nextMove = null;
        double currentValue;

        ArrayList<Move> successors = position.generateSuccessors();
        for (Move successor : successors) {
            if (position.move(successor) == null) {
                position.undoMove(successor);
                continue;
            }
            currentValue = minValue(position, depth, alpha, beta, AIColor);
            if (currentValue > value) {
                nextMove = successor;
                value = currentValue;
            }
//            System.out.println("Max doing undo");
            position.undoMove(successor);
            if (value >= beta) {
                this.nextMove = nextMove;
                return value;
            }
            if (value > alpha) {
                alpha = value;
            }
        }

//        System.out.println("Last update " + depth);
        this.nextMove = nextMove;
        return value;
    }

    private double minValue(Position position, int depth, double alpha, double beta, boolean AIColor) {
        if (position.isTerminal(depth)) {
//            System.out.println("Terminating in min; Color:" + turn);
            return position.eval(depth, AIColor);
        }

        double value = Double.MAX_VALUE;
        Move nextMove = null;
        double currentValue;

        ArrayList<Move> successors = position.generateSuccessors();
        for (Move successor : successors) {
            if (position.move(successor) == null) {
                position.undoMove(successor);
                continue;
            }
            currentValue = maxValue(position, depth + 1, alpha, beta, AIColor);
            if (currentValue < value) {
                nextMove = successor;
                value = currentValue;
            }
//            System.out.println("Min doing undo");
//            successor.print();
            position.undoMove(successor);
            if (value <= alpha) {
                this.nextMove = nextMove;
                return value;
            }
            if (value < beta) {
                beta = value;
            }
        }

        this.nextMove = nextMove;
        return value;
    }

    public Position minimaxDecision(boolean AIColor) {
        maxValue(this, 0, Double.MIN_VALUE, Double.MAX_VALUE, AIColor);
//        System.out.println(nextMove.getPieceToMove().getClass());
//        System.out.println(nextMove.getX() + " new " + nextMove.getY());
//        System.out.println(nextMove.getPieceToMove().getX() + " old " + nextMove.getPieceToMove().getY());
//        Position pos = this.move(nextMove);
//        if (pos == null)
//            System.out.println("Got it");
////        return this.move(nextMove);
        return this.move(nextMove);
    }
}
