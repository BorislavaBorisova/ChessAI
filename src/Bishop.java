import java.util.ArrayList;

public class Bishop extends Piece {
    public Bishop(int x, int y, boolean color) {
        super(x, y, color);
    }

    @Override
    public Bishop clone() {
        return new Bishop(x, y, color);
    }

    @Override
    public ArrayList<Move> generatePossibleMoves(Position currentBoard) {
        ArrayList<Move> moves = new ArrayList<>();
        int newX = x + 1, newY = y - 1;
        while (Helpers.onBoard(newX, newY)) {
            if (currentBoard.getPiece(newX, newY) != null) {
                if (currentBoard.getPiece(newX, newY).color != currentBoard.getTurn()) {
                    moves.add(new Move(this.clone(), newX, newY, currentBoard.getPiece(newX, newY), null, null));
                }
                break;
            }
            moves.add(new Move(this.clone(), newX, newY, null, null, null));
            newX++;
            newY--;
        }

        newX = x - 1;
        newY = y + 1;
        while (Helpers.onBoard(newX, newY)) {
            if (currentBoard.getPiece(newX, newY) != null) {
                if (currentBoard.getPiece(newX, newY).color != currentBoard.getTurn()) {
                    moves.add(new Move(this.clone(), newX, newY, currentBoard.getPiece(newX, newY), null, null));
                }
                break;
            }
            moves.add(new Move(this.clone(), newX, newY, null, null, null));
            newX--;
            newY++;
        }

        newX = x + 1;
        newY = y + 1;
        while (Helpers.onBoard(newX, newY)) {
            if (currentBoard.getPiece(newX, newY) != null) {
                if (currentBoard.getPiece(newX, newY).color != currentBoard.getTurn()) {
                    moves.add(new Move(this.clone(), newX, newY, currentBoard.getPiece(newX, newY), null, null));
                }
                break;
            }
            moves.add(new Move(this.clone(), newX, newY, null, null, null));
            newX++;
            newY++;
        }

        newX = x - 1;
        newY = y - 1;
        while (Helpers.onBoard(newX, newY)) {
            if (currentBoard.getPiece(newX, newY) != null) {
                if (currentBoard.getPiece(newX, newY).color != currentBoard.getTurn()) {
                    moves.add(new Move(this.clone(), newX, newY, currentBoard.getPiece(newX, newY), null, null));
                }
                break;
            }
            moves.add(new Move(this.clone(), newX, newY, null, null, null));
            newX--;
            newY--;
        }
        
        return moves;
    }

    @Override
    public boolean canReach(int goalX, int goalY, Position currentPosition) {
        int newX = goalX, newY = goalY;
        do {
            newX++;
            newY++;
            if (newX == goalX && newY == goalY)
                return true;
        } while (Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);

        newX = goalX;
        newY = goalY;
        do {
            newX++;
            newY--;
            if (newX == goalX && newY == goalY)
                return true;
        } while (Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);

        newX = goalX;
        newY = goalY;
        do {
            newX--;
            newY++;
            if (newX == goalX && newY == goalY)
                return true;
        } while (Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);

        newX = goalX;
        newY = goalY;
        do {
            newX--;
            newY--;
            if (newX == goalX && newY == goalY)
                return true;
        } while (Helpers.onBoard(newX, newY) && currentPosition.getPiece(newX, newY) == null);
        return false;
    }
}
