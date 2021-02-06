import java.util.ArrayList;

public class Knight extends Piece{
    public Knight(int x, int y, boolean color){
        super(x, y, color);
    }

    @Override
    public Knight clone() {
        return new Knight(x, y, color);
    }

    @Override
    public ArrayList<Move> generatePossibleMoves(Position currentBoard) {
        ArrayList<Move> moves = new ArrayList<>();
        moves.add(new Move(this.clone(), x + 1, y - 2, currentBoard.getPiece(x + 1, y - 2), null, null));
        moves.add(new Move(this.clone(), x - 1, y - 2, currentBoard.getPiece(x - 1, y - 2), null, null));
        moves.add(new Move(this.clone(), x + 1, y + 2, currentBoard.getPiece(x + 1, y + 2), null, null));
        moves.add(new Move(this.clone(), x - 1, y + 2, currentBoard.getPiece(x - 1, y + 2), null, null));
        moves.add(new Move(this.clone(), x - 2, y + 1, currentBoard.getPiece(x - 2, y + 1), null, null));
        moves.add(new Move(this.clone(), x - 2, y - 1, currentBoard.getPiece(x - 2, y - 1), null, null));
        moves.add(new Move(this.clone(), x + 2, y + 1, currentBoard.getPiece(x + 2, y + 1), null, null));
        moves.add(new Move(this.clone(), x + 2, y - 1, currentBoard.getPiece(x + 2, y - 1), null, null));
        
        return moves;
    }

    @Override
    public boolean canReach(int goalX, int goalY, Position currentPosition) {
        return (x + 2 == goalX && y + 1 == goalY) || (x + 2 == goalX && y - 1 == goalY) ||
                (x - 2 == goalX && y + 1 == goalY) || (x - 2 == goalX && y - 1 == goalY) ||
                (x + 1 == goalX && y + 2 == goalY) || (x - 1 == goalX && y + 2 == goalY) ||
                (x + 1 == goalX && y - 2 == goalY) || (x - 1 == goalX && y - 2 == goalY);
    }
}