public class Position {
    private Piece[][] board;
    private boolean turn;

    public Position(boolean turn){
        this.board = new Piece[8][8];
    }

    public void placePiece(Piece piece){
        board[piece.getX()][piece.getY()] = piece;
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
        Position newPosition = this.clone();
        newPosition.board[newX][newY] = newPosition.board[oldX][oldY];
        newPosition.board[oldX][oldY] = null;
        newPosition.turn = !turn;
        return newPosition;
    }

    public boolean valid(){//TODO
        return true;
    }
}
