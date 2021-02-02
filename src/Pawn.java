import java.util.ArrayList;
import java.util.stream.Collectors;

public class Pawn extends Piece {
    private int firstMove = -1;

    public Pawn(int x, int y, boolean color) {
        super(x, y, color);
    }

    @Override
    public Pawn clone() {
        return new Pawn(x, y, color);
    }

    public void setFirstMove(int move) {
        firstMove = move;
    }

    @Override
    public ArrayList<Position> generatePossibleMoves(Position currentBoard) {
        ArrayList<Position> positions = new ArrayList<>();
        if (color) {
            if (currentBoard.getPiece(x + 1, y) == null) {
                if (x == 6) {
                    positions.add(currentBoard.promote(x, y, x + 1, y, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x + 1, y));
                }
            }
            if (currentBoard.getPiece(x + 1, y + 1) != null) {
                if (x == 6) {
                    positions.add(currentBoard.promote(x, y, x + 1, y + 1, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y + 1, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y + 1, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y + 1, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x + 1, y + 1));
                }
            }
            if (currentBoard.getPiece(x + 1, y - 1) != null) {
                if (x == 6) {
                    positions.add(currentBoard.promote(x, y, x + 1, y - 1, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y - 1, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y - 1, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y - 1, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x + 1, y - 1));
                }
            }
            if (firstMove == -1 && currentBoard.getPiece(x + 1, y) == null && currentBoard.getPiece(x + 2, y) == null) {
                positions.add(currentBoard.move(x, y, x + 2, y));
            }
            if (x == 4 && currentBoard.getPiece(x, y + 1) instanceof Pawn
                    && ((Pawn) currentBoard.getPiece(x, y + 1)).firstMove + 1 == currentBoard.getMove()) {
                positions.add(currentBoard.enPassant(x, y, x + 1, y + 1));
            }
            if (x == 4 && currentBoard.getPiece(x, y - 1) instanceof Pawn
                    && ((Pawn) currentBoard.getPiece(x, y - 1)).firstMove + 1 == currentBoard.getMove()) {
                positions.add(currentBoard.enPassant(x, y, x + 1, y - 1));
            }
        } else {
            if (currentBoard.getPiece(x - 1, y) == null) {
                if (x == 1) {
                    positions.add(currentBoard.promote(x, y, x - 1, y, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x - 1, y));
                }
            }
            if (currentBoard.getPiece(x - 1, y + 1) != null) {
                if (x == 1) {
                    positions.add(currentBoard.promote(x, y, x - 1, y + 1, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y + 1, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y + 1, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y + 1, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x - 1, y + 1));
                }
            }
            if (currentBoard.getPiece(x - 1, y - 1) != null) {
                if (x == 1) {
                    positions.add(currentBoard.promote(x, y, x - 1, y - 1, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y - 1, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y - 1, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y - 1, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x - 1, y - 1));
                }
            }
            if (firstMove == -1 && currentBoard.getPiece(x - 1, y) == null && currentBoard.getPiece(x - 2, y) == null) {
                positions.add(currentBoard.move(x, y, x - 2, y));
            }
            if (x == 3 && currentBoard.getPiece(x, y + 1) instanceof Pawn
                    && ((Pawn) currentBoard.getPiece(x, y + 1)).firstMove + 1 == currentBoard.getMove()) {
                positions.add(currentBoard.enPassant(x, y, x - 1, y + 1));
            }
            if (x == 3 && currentBoard.getPiece(x, y - 1) instanceof Pawn
                    && ((Pawn) currentBoard.getPiece(x, y - 1)).firstMove + 1 == currentBoard.getMove()) {
                positions.add(currentBoard.enPassant(x, y, x - 1, y - 1));
            }
        }

        return new ArrayList<>(
                positions.stream().filter(position -> position.valid()).collect(Collectors.toList()));
    }

    @Override
    public boolean canReach(int goalX, int goalY, Position currentPosition) {
        if(color) {
            if(goalX == x + 1 && (goalY == y + 1 || goalY == y - 1)) return true;
        } else {
            if(goalX == x - 1 && (goalY == y + 1 || goalY == y - 1)) return true;
        }
        return false;
    }
}
