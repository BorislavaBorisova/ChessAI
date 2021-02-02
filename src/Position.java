public class Position {
    private Piece[][] board;
    private boolean turn;

    public Position(boolean turn){
        this.board = new Piece[8][8];
    }

    public void placePiece(Piece piece){
        board[piece.getX()][piece.getY()] = piece;
    }

    public Piece getPiece(int x, int y){
        return board[x][y];
    }

    public Position clone(){
        Position newPosition = new Position(turn);
        for(int i = 0; i < board[0].length; i++){
            for(int j = 0; j < board.length; j++){
                newPosition.placePiece(board[i][j].clone());
            }
        }
        return newPosition;
    }

    public Position move(int oldX, int oldY, int newX, int newY){
        if(!Helpers.onBoard(newX, newY)) return null;
        Position newPosition = this.clone();
        if(newPosition.board[oldX][oldY].color == turn) return null; //checks if i am trying to take my own piece
        newPosition.board[newX][newY] = newPosition.board[oldX][oldY];
        newPosition.board[oldX][oldY] = null;
        newPosition.turn = !turn;
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
