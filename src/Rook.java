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
        int newX = x++, newY = y;
        while(Helpers.onBoard(newX, newY) && ((currentBoard.getPiece(newX, newY) != null && currentBoard.getPiece(newX, newY).color != color) || currentBoard.getPiece(newX, newY) == null)){
            boolean currentPieceColor = currentBoard.getPiece(newX, newY).color;
            positions.add(currentBoard.move(x, y, newX, newY));
            if(currentPieceColor != color) break;
            newX++;
        }

        newX = x--; newY = y;
        while(Helpers.onBoard(newX, newY) && ((currentBoard.getPiece(newX, newY) != null && currentBoard.getPiece(newX, newY).color != color) || currentBoard.getPiece(newX, newY) == null)){
            boolean currentPieceColor = currentBoard.getPiece(newX, newY).color;
            positions.add(currentBoard.move(x, y, newX, newY));
            if(currentPieceColor != color) break;
            newX--;
        }

        newX = x; newY = y++;
        while(Helpers.onBoard(newX, newY) && ((currentBoard.getPiece(newX, newY) != null && currentBoard.getPiece(newX, newY).color != color) || currentBoard.getPiece(newX, newY) == null)){
            boolean currentPieceColor = currentBoard.getPiece(newX, newY).color;
            positions.add(currentBoard.move(x, y, newX, newY));
            if(currentPieceColor != color) break;
            newY++;
        }

        newX = x; newY = y--;
        while(Helpers.onBoard(newX, newY) && ((currentBoard.getPiece(newX, newY) != null && currentBoard.getPiece(newX, newY).color != color) || currentBoard.getPiece(newX, newY) == null)){
            boolean currentPieceColor = currentBoard.getPiece(newX, newY).color;
            positions.add(currentBoard.move(x, y, newX, newY));
            if(currentPieceColor != color) break;
            newY--;
        }
        ArrayList<Position> filteredPositions = new ArrayList<>(positions.stream().filter(position -> position.valid()).collect(Collectors.toList()));
        return filteredPositions;
    }

    @Override
    public boolean canReach(int goalX, int goalY, Position currentPosition) {
        int newX = goalX, newY = goalY;
        do{
            newX++;
            if(newX == goalX && newY == goalY) return true;
        } while(Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);

        newX = goalX; newY = goalY;
        do{
            newX--;
            if(newX == goalX && newY == goalY) return true;
        } while(Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);

        newX = goalX; newY = goalY;
        do{
            newY++;
            if(newX == goalX && newY == goalY) return true;
        } while(Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);

        newX = goalX; newY = goalY;
        do{
            newY--;
            if(newX == goalX && newY == goalY) return true;
        } while(Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);
        return false;
    }
}
