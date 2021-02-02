import java.util.ArrayList;

public class Pawn extends Piece{

    public Pawn(int x, int y, boolean color) {
        super(x, y, color);
    }

    @Override //TODO
    public Pawn clone() {
        return null;
    }

    @Override //TODO
    public ArrayList<Position> generatePossibleMoves(Position currentBoard) {
        return null;
    }

    @Override
    public boolean canReach(int x, int y, Position currentPosition) {
        return false;
    }
}
