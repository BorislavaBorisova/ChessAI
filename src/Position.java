import java.util.ArrayList;

public class Position {
    private Piece[][] board;
    private boolean turn;
    private int move = 0;
    private int whiteKingX = 4;
    private int whiteKingY = 0;
    private int blackKingX = 4;
    private int blackKingY = 7;
    private Position nextPosition;

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
                newPosition.placePiece(board[i][j].clone());
            }
        }
        return newPosition;
    }

    public Position move(int oldX, int oldY, int newX, int newY) {
        //checks if on board and if i am trying to take my own piece
        if (!Helpers.onBoard(newX, newY) || board[newX][newY].color == turn)
            return null;
        Position newPosition = this.clone();
        newPosition.board[newX][newY] = newPosition.board[oldX][oldY];
        newPosition.board[newX][newY].setCoordinates(newX, newY);
        if(newPosition.board[newX][newY] instanceof Pawn) {            
            ((Pawn)newPosition.board[newX][newY]).setFirstMove(move);
        }
        newPosition.board[oldX][oldY] = null;
        newPosition.turn = !turn;
        newPosition.move = move + 1;
        return newPosition.valid() ? newPosition : null;
    }

    public Position promote(int oldX, int oldY, int newX, int newY, Piece piece) {
        //checks if on board and if i am trying to take my own piece
        if (!Helpers.onBoard(newX, newY) || board[newX][newY].color == turn)
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
        //checks if on board and if i am trying to take my own piece
        if (!Helpers.onBoard(newX, newY) || board[newX][newY].color == turn)
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

    public boolean valid(){
        int kingX = turn ? whiteKingX : blackKingX,  kingY = turn ? whiteKingY : blackKingY;
        for(int k = 0; k < board[0].length; k++){
            for(int l = 0; l < board.length; l++) {
                if(board[k][l] != null && board[k][l].color != turn && board[k][l].canReach(kingX, kingY, this)) return false;
            }
        }
        return true;
    }

    public boolean canBeAttacked(int x, int y){
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board.length; j++) {
                if(board[i][j].color == !turn && board[i][j].canReach(x, y, this)) return true;
            }
        }
        return false;
    }

    public boolean hasValidMoves(){
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board.length; j++) {
                if(board[i][j].color == turn && !board[i][j].generatePossibleMoves(this).isEmpty()) return true;
            }
        }
        return false;
    }

    public boolean check(){
        int kingX = turn ? whiteKingX : blackKingX,  kingY = turn ? whiteKingY : blackKingY;
        return canBeAttacked(kingX, kingY);
    }

    public void print(){
        for(int j = board.length - 1; j >= 0; j--) {
            for(int i = 0; i < board[0].length; i++) {
                if(board[i][j] == null) System.out.print("   ");
                else if(board[i][j] instanceof Pawn) System.out.print("pw ");
                else if(board[i][j] instanceof Bishop) System.out.print("bs ");
                else if(board[i][j] instanceof Knight) System.out.print("kn ");
                else if(board[i][j] instanceof Rook) System.out.print("ro ");
                else if(board[i][j] instanceof Queen) System.out.print("qu ");
                else System.out.print("kg ");
            }
            System.out.println();
        }
    }

    public double eval(int depth){
        double sum = 0.0;
        for(int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j].color == turn){
                    if(board[i][j] instanceof Pawn) sum += 1.0;
                    else if(board[i][j] instanceof Bishop) sum += 3.0;
                    else if(board[i][j] instanceof Knight) sum += 3.0;
                    else if(board[i][j] instanceof Rook) sum += 5.0;
                    else if(board[i][j] instanceof Queen) sum += 9.0;
                    else if(board[i][j] instanceof King)sum += 105.0;
                }
            }
        }
        return sum;
    }

    private ArrayList<Position> generateSuccessors(){
        ArrayList<Position> successors = new ArrayList<>();
        for(int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j].color == true){
                    successors.addAll(board[i][j].generatePossibleMoves(this));
                }
            }
        }
        return successors;
    }

//    private double maxValue(Position state, int depth, double alpha, double beta){
//        if(isTerminalState(state)){
//            return state.eval(depth);
//        }
//        double v = Double.MIN_VALUE;
//        ArrayList<Position> successors = generateSuccessors();
//        double minValue;
//        Position nextStateLocal = null;
//        for(Position successor : successors){
//            minValue = minValue(successor, depth + 1, alpha, beta);
//            if(minValue > v){
//                nextStateLocal = successor;
//            }
//            v = Math.max(v, minValue);
//            if(v >= beta)
//                return v;
//            alpha = Math.max(alpha, v);
//        }
//        nextPosition = nextStateLocal;
//        return v;
//    }

    private double maxValue(Position position, int depth, double alpha, double beta) {
        if (this.isTerminal(position)) {
            return position.eval(depth);
        }

        double value = Double.MIN_VALUE;
        Position nextPosition = null;
        double currentValue;

        ArrayList<Position> successors = position.generateSuccessors();
        for (Position successor : successors) {
            currentValue = minValue(successor, depth + 1, alpha, beta);
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

    private double minValue(Position position, int depth, double alpha, double beta) {
        if (this.isTerminal(position)) {
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

    public Position minimaxDecision(){
        maxValue(this, 0, Double.MIN_VALUE, Double.MAX_VALUE);
        return nextPosition;
    }
}
