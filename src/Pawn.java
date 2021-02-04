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
    
    public int getFirstMove() {
        return firstMove;
    }

    @Override
    public ArrayList<Position> generatePossibleMoves(Position currentBoard) {
        ArrayList<Position> positions = new ArrayList<>();
        if (color) {
            if (currentBoard.getPiece(x, y + 1) == null) {
                if (y == 6) {
                    positions.add(currentBoard.promote(x, y, x, y + 1, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x, y + 1, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x, y + 1, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x, y + 1, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x, y + 1));
                }
            }
            if (currentBoard.getPiece(x + 1, y + 1) != null && currentBoard.getPiece(x + 1, y + 1).color != color) {
                if (x == 6) {
                    positions.add(currentBoard.promote(x, y, x + 1, y + 1, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y + 1, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y + 1, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y + 1, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x + 1, y + 1));
                }
            }
            if (currentBoard.getPiece(x - 1, y + 1) != null && currentBoard.getPiece(x - 1, y + 1).color != color) {
                if (x == 6) {
                    positions.add(currentBoard.promote(x, y, x - 1, y + 1, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y + 1, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y + 1, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y + 1, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x - 1, y + 1));
                }
            }
            if (firstMove == -1 && currentBoard.getPiece(x, y + 1) == null && currentBoard.getPiece(x, y + 2) == null) {
                positions.add(currentBoard.move(x, y, x, y + 2));
            }
            if (y == 4 && currentBoard.getPiece(x + 1, y) instanceof Pawn
                    && ((Pawn) currentBoard.getPiece(x + 1, y)).firstMove + 1 == currentBoard.getMove()) {
                positions.add(currentBoard.enPassant(x, y, x + 1, y + 1));
            }
            if (y == 4 && currentBoard.getPiece(x - 1, y) instanceof Pawn
                    && ((Pawn) currentBoard.getPiece(x - 1, y)).firstMove + 1 == currentBoard.getMove()) {
                positions.add(currentBoard.enPassant(x, y, x - 1, y + 1));
            }
        } else {
            if (currentBoard.getPiece(x, y - 1) == null) {
                if (y == 1) {
                    positions.add(currentBoard.promote(x, y, x, y - 1, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x, y - 1, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x, y - 1, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x, y - 1, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x, y - 1));
                }
            }
            if (currentBoard.getPiece(x + 1, y - 1) != null && currentBoard.getPiece(x + 1, y - 1).color != color) {
                if (x == 1) {
                    positions.add(currentBoard.promote(x, y, x + 1, y - 1, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y - 1, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y - 1, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x + 1, y - 1, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x + 1, y - 1));
                }
            }
            if (currentBoard.getPiece(x - 1, y - 1) != null && currentBoard.getPiece(x - 1, y - 1).color != color) {
                if (x == 1) {
                    positions.add(currentBoard.promote(x, y, x - 1, y - 1, new Queen(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y - 1, new Knight(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y - 1, new Rook(x, y, color)));
                    positions.add(currentBoard.promote(x, y, x - 1, y - 1, new Bishop(x, y, color)));
                } else {
                    positions.add(currentBoard.move(x, y, x - 1, y - 1));
                }
            }
            if (firstMove == -1 && currentBoard.getPiece(x, y - 1) == null && currentBoard.getPiece(x, y - 2) == null) {
                positions.add(currentBoard.move(x, y, x, y - 2));
            }
            if (y == 3 && currentBoard.getPiece(x + 1, y) instanceof Pawn
                    && ((Pawn) currentBoard.getPiece(x + 1, y)).firstMove + 1 == currentBoard.getMove()) {
                positions.add(currentBoard.enPassant(x, y, x + 1, y - 1));
            }
            if (y == 3 && currentBoard.getPiece(x - 1, y) instanceof Pawn
                    && ((Pawn) currentBoard.getPiece(x - 1, y)).firstMove + 1 == currentBoard.getMove()) {
                positions.add(currentBoard.enPassant(x, y, x - 1, y - 1));
            }
        }

        return new ArrayList<>(
                positions.stream().filter(position -> position != null).collect(Collectors.toList()));
    }

    @Override
    public boolean canReach(int goalX, int goalY, Position currentPosition) {
        if(color) {
            if(goalY == y + 1 && (goalX == x + 1 || goalX == x - 1)) return true;
        } else {
            if(goalY == y - 1 && (goalX == x + 1 || goalX == x - 1)) return true;
        }
        return false;
    }
}
