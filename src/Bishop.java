import java.util.ArrayList;
import java.util.stream.Collectors;

public class Bishop extends Piece{
    public Bishop(int x, int y, boolean color){
        super(x, y, color);
    }

    @Override
    public Bishop clone() {
        return new Bishop(x, y, color);
    }

    @Override
    public ArrayList<Position> generatePossibleMoves(Position currentBoard) {
        ArrayList<Position> positions = new ArrayList<>();
        int newX = x++, newY = y--;
        while(Helpers.onBoard(newX, newY) && ((currentBoard.getPiece(newX, newY) != null && currentBoard.getPiece(newX, newY).color != color) || currentBoard.getPiece(newX, newY) == null)){
            boolean currentPieceColor = currentBoard.getPiece(newX, newY).color;
            positions.add(currentBoard.move(x, y, newX, newY));
            if(currentPieceColor != color) break;
            newX++;
            newY--;
        }

        newX = x--; newY = y++;
        while(Helpers.onBoard(newX, newY) && ((currentBoard.getPiece(newX, newY) != null && currentBoard.getPiece(newX, newY).color != color) || currentBoard.getPiece(newX, newY) == null)){
            boolean currentPieceColor = currentBoard.getPiece(newX, newY).color;
            positions.add(currentBoard.move(x, y, newX, newY));
            if(currentPieceColor != color) break;
            newX--;
            newY++;
        }

        newX = x++; newY = y++;
        while(Helpers.onBoard(newX, newY) && ((currentBoard.getPiece(newX, newY) != null && currentBoard.getPiece(newX, newY).color != color) || currentBoard.getPiece(newX, newY) == null)){
            boolean currentPieceColor = currentBoard.getPiece(newX, newY).color;
            positions.add(currentBoard.move(x, y, newX, newY));
            if(currentPieceColor != color) break;
            newX++;
            newY++;
        }

        newX = x--; newY = y--;
        while(Helpers.onBoard(newX, newY) && ((currentBoard.getPiece(newX, newY) != null && currentBoard.getPiece(newX, newY).color != color) || currentBoard.getPiece(newX, newY) == null)){
            boolean currentPieceColor = currentBoard.getPiece(newX, newY).color;
            positions.add(currentBoard.move(x, y, newX, newY));
            if(currentPieceColor != color) break;
            newX--;
            newY--;
        }
        ArrayList<Position> filteredPositions = new ArrayList<>(positions.stream().filter(position -> position.valid()).collect(Collectors.toList()));
        return filteredPositions;
    }

    @Override
    public boolean canReach(int goalX, int goalY, Position currentPosition) {
        int newX = goalX, newY = goalY;
        do{
            newX++; newY++;
            if(newX == goalX && newY == goalY) return true;
        } while(Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);

        newX = goalX; newY = goalY;
        do{
            newX++; newY--;
            if(newX == goalX && newY == goalY) return true;
        } while(Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);

        newX = goalX; newY = goalY;
        do{
            newX--; newY++;
            if(newX == goalX && newY == goalY) return true;
        } while(Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);

        newX = goalX; newY = goalY;
        do{
            newX--; newY--;
            if(newX == goalX && newY == goalY) return true;
        } while(Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);
        return false;
    }
}
