import java.util.ArrayList;
import java.util.stream.Collectors;

public class Rook extends Piece{
    public Rook(int x, int y, boolean color){
        super(x, y, color);
    }

    @Override
    public Rook clone() {
        return new Rook(x, y, color);
    }

    @Override
    public ArrayList<Position> generatePossibleMoves(Position currentBoard) {
        ArrayList<Position> positions = new ArrayList<>();
        int newX = x, newY = y;
        while(Helpers.onBoard(newX, newY)){
            newX++;
            positions.add(currentBoard.move(x, y, newX, newY));
        }
        while(Helpers.onBoard(newX, newY)){
            newX--;
            positions.add(currentBoard.move(x, y, newX, newY));
        }
        while(Helpers.onBoard(newX, newY)){
            newY++;
            positions.add(currentBoard.move(x, y, newX, newY));
        }
        while(Helpers.onBoard(newX, newY)){
            newY--;
            positions.add(currentBoard.move(x, y, newX, newY));
        }
        ArrayList<Position> filteredPositions = new ArrayList<>(positions.stream().filter(position -> position.valid()).collect(Collectors.toList()));
        return filteredPositions;
    }
}