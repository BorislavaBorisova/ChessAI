import java.util.ArrayList;

public abstract class Piece {
    private int[] position;
    boolean color;

    public abstract ArrayList<Position> generatePossibleMoves(Position currentBoard);
}
