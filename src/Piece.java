import java.util.ArrayList;

public abstract class Piece {
    protected int x;
    protected int y;
    protected boolean color;

    public Piece(int x, int y, boolean color){
        this.x = x;
        this.y = y;
        this.color = color;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public abstract Piece clone();

    public abstract ArrayList<Position> generatePossibleMoves(Position currentBoard);

    public abstract boolean canReach(int goalX, int goalY, Position currentPosition);
}
