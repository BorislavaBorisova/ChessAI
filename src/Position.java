public class Position {
    private Piece[][] board;
    private boolean turn;
    private int move = 0;
    private int whiteKingX = 4;
    private int whiteKingY = 0;
    private int blackKingX = 4;
    private int blackKingY = 7;

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
        for(int i = 0; i < board[0].length; i++) {
            for(int j = board.length - 1; j >= 0; j--) {
                if(board[i][j] == null){
                    System.out.print("   ");
                } else if(board[i][j] instanceof Pawn){
                    System.out.print("pw ");
                }else if(board[i][j] instanceof Bishop){
                    System.out.print("bs ");
                }else if(board[i][j] instanceof Knight){
                    System.out.print("kn ");
                }else if(board[i][j] instanceof Rook){
                    System.out.print("ro ");
                }else if(board[i][j] instanceof Queen){
                    System.out.print("qu ");
                }else if(board[i][j] instanceof King){
                    System.out.print("kg ");
                }
            }
            System.out.println();
        }
    }
}
