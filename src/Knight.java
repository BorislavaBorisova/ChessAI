import java.util.ArrayList;
import java.util.stream.Collectors;

public class Knight extends Piece{
    public Knight(int x, int y, boolean color){
        super(x, y, color);
    }

    @Override
    public Knight clone() {
        return new Knight(x, y, color);
    }

    @Override
    public ArrayList<Position> generatePossibleMoves(Position currentBoard) {
        ArrayList<Position> positions = new ArrayList<>();
        positions.add(currentBoard.move(x, y, x + 1, y - 2));
        positions.add(currentBoard.move(x, y, x - 1, y - 2));
        positions.add(currentBoard.move(x, y, x + 1, y + 2));
        positions.add(currentBoard.move(x, y, x - 1, y + 2));
        positions.add(currentBoard.move(x, y, x - 2, y + 1));
        positions.add(currentBoard.move(x, y, x - 2, y - 1));
        positions.add(currentBoard.move(x, y, x + 2, y + 1));
        positions.add(currentBoard.move(x, y, x + 2, y - 1));
        ArrayList<Position> filteredPositions = new ArrayList<>(positions.stream().filter(position -> position.valid()).collect(Collectors.toList()));
        return filteredPositions;
    }

    @Override
    public boolean canReach(int goalX, int goalY, Position currentPosition) {
        return (x + 2 == goalX && y + 1 == goalY) || (x + 2 == goalX && y - 1 == goalY) ||
                (x - 2 == goalX && y + 1 == goalY) || (x - 2 == goalX && y - 1 == goalY) ||
                (x + 1 == goalX && y + 2 == goalY) || (x - 1 == goalX && y + 2 == goalY) ||
                (x + 1 == goalX && y - 2 == goalY) || (x - 1 == goalX && y - 2 == goalY);
    }
}