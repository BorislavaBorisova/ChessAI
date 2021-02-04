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
        int newX = x+1, newY = y-1;
        while(Helpers.onBoard(newX, newY)){
            if(currentBoard.getPiece(newX, newY) != null){
                if(currentBoard.getPiece(newX, newY).color != currentBoard.getTurn()){
                    positions.add(currentBoard.move(x, y, newX, newY));
                }
                break;
            }
            positions.add(currentBoard.move(x, y, newX, newY));
            newX++;
            newY--;
        }

        newX = x-1; newY = y+1;
        while(Helpers.onBoard(newX, newY)){
            if(currentBoard.getPiece(newX, newY) != null){
                if(currentBoard.getPiece(newX, newY).color != currentBoard.getTurn()){
                    positions.add(currentBoard.move(x, y, newX, newY));
                }
                break;
            }
            positions.add(currentBoard.move(x, y, newX, newY));
            newX--;
            newY++;
        }

        newX = x+1; newY = y+1;
        while(Helpers.onBoard(newX, newY)){
            if(currentBoard.getPiece(newX, newY) != null){
                if(currentBoard.getPiece(newX, newY).color != currentBoard.getTurn()){
                    positions.add(currentBoard.move(x, y, newX, newY));
                }
                break;
            }
            positions.add(currentBoard.move(x, y, newX, newY));
            newX++;
            newY++;
        }

        newX = x-1; newY = y-1;
        while(Helpers.onBoard(newX, newY)){
            if(currentBoard.getPiece(newX, newY) != null){
                if(currentBoard.getPiece(newX, newY).color != currentBoard.getTurn()){
                    positions.add(currentBoard.move(x, y, newX, newY));
                }
                break;
            }
            positions.add(currentBoard.move(x, y, newX, newY));
            newX--;
            newY--;
        }
        ArrayList<Position> filteredPositions = new ArrayList<>(positions.stream().filter(position -> position != null).collect(Collectors.toList()));
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
