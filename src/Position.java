public class Position {
    private Piece[][] board;
    private boolean turn;
    private int move = 0;

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
        if (!Helpers.onBoard(newX, newY))
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
        if (!Helpers.onBoard(newX, newY))
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
        if (!Helpers.onBoard(newX, newY))
            return null;
        Position newPosition = this.clone();
        if(newPosition.board[oldX][oldY].color == turn) return null; //checks if i am trying to take my own piece
        newPosition.board[newX][newY] = newPosition.board[oldX][oldY];
        newPosition.board[newX][newY].setCoordinates(newX, newY);
        newPosition.board[oldX][oldY] = null;
        newPosition.board[oldX][newY] = null;
        newPosition.turn = !turn;
        newPosition.move = move + 1;
        return newPosition.valid() ? newPosition : null;
    }

    public boolean valid(){
        int kingX = 0, kingY = 0;
        for(int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] != null && board[i][j].color == turn && board[i][j] instanceof King){
                    kingX = board[i][j].x;
                    kingY = board[i][j].y;
                }
            }
        }

        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board.length; j++) {
                if(board[i][j] != null && board[i][j].color != turn && board[i][j].canReach(kingX, kingY, this)) return false;
            }
        }
        return true;
    }
}
