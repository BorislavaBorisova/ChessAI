import java.util.ArrayList;
import java.util.stream.Collectors;

public class King extends Piece {
    private boolean hasMoved = false;

    public King(int x, int y, boolean color) {
        super(x, y, color);
    }

    public void move() {
        hasMoved = true;
    }

    public boolean hasMoved() {
        return hasMoved;
    }

    @Override
    public King clone() {
        return new King(x, y, color);
    }

    @Override
    public ArrayList<Position> generatePossibleMoves(Position currentBoard) {
        ArrayList<Position> positions = new ArrayList<>();
        Piece down = currentBoard.getPiece(x, y - 1);
        Piece downRight = currentBoard.getPiece(x + 1, y - 1);
        Piece right = currentBoard.getPiece(x + 1, y);
        Piece upRight = currentBoard.getPiece(x + 1, y + 1);
        Piece up = currentBoard.getPiece(x, y + 1);
        Piece upLeft = currentBoard.getPiece(x - 1, y + 1);
        Piece left = currentBoard.getPiece(x - 1, y);
        Piece downLeft = currentBoard.getPiece(x - 1, y - 1);
        if (down == null || (down != null && down.color != color)) {
            positions.add(currentBoard.move(x, y, x, y - 1));
        }
        if (downRight == null || (downRight != null && downRight.color != color)) {
            positions.add(currentBoard.move(x, y, x + 1, y - 1));
        }
        if (right == null || (right != null && right.color != color)) {
            positions.add(currentBoard.move(x, y, x + 1, y));
        }
        if (upRight == null || (upRight != null && upRight.color != color)) {
            positions.add(currentBoard.move(x, y, x + 1, y + 1));
        }
        if (up == null || (up != null && up.color != color)) {
            positions.add(currentBoard.move(x, y, x, y + 1));
        }
        if (upLeft == null || (upLeft != null && upLeft.color != color)) {
            positions.add(currentBoard.move(x, y, x - 1, y + 1));
        }
        if (left == null || (left != null && left.color != color)) {
            positions.add(currentBoard.move(x, y, x - 1, y));
        }
        if (downLeft == null || (downLeft != null && downLeft.color != color)) {
            positions.add(currentBoard.move(x, y, x - 1, y - 1));
        }
        if (!hasMoved) {
            if (color) {
                Piece leftCorner = currentBoard.getPiece(0, 0);
                Piece rightCorner = currentBoard.getPiece(7, 0);
                Rook leftRook = null, rightRook = null;
                if (leftCorner instanceof Rook) {
                    leftRook = (Rook) leftCorner;
                }
                if (rightCorner instanceof Rook) {
                    rightRook = (Rook) rightCorner;
                }
                if (leftRook != null && !leftRook.hasMoved() && currentBoard.getPiece(3, 0) == null
                        && currentBoard.getPiece(2, 0) == null && currentBoard.getPiece(1, 0) == null
                        && !currentBoard.canBeAttacked(3, 0)
                        && !currentBoard.canBeAttacked(2, 0)) {
                    positions.add(currentBoard.castle(x, y, 0, 0));
                }
                if (rightRook != null && !rightRook.hasMoved() && currentBoard.getPiece(5, 0) == null
                        && currentBoard.getPiece(6, 0) == null && !currentBoard.canBeAttacked(5, 0)
                        && !currentBoard.canBeAttacked(6, 0)) {
                    positions.add(currentBoard.castle(x, y, 7, 0));
                }
            } else {
                Piece leftCorner = currentBoard.getPiece(0, 7);
                Piece rightCorner = currentBoard.getPiece(7, 7);
                Rook leftRook = null, rightRook = null;
                if (leftCorner instanceof Rook) {
                    leftRook = (Rook) leftCorner;
                }
                if (rightCorner instanceof Rook) {
                    rightRook = (Rook) rightCorner;
                }
                if (leftRook != null && !leftRook.hasMoved() && currentBoard.getPiece(3, 7) == null
                        && currentBoard.getPiece(2, 7) == null && currentBoard.getPiece(1, 7) == null
                        && !currentBoard.canBeAttacked(3, 7)
                        && !currentBoard.canBeAttacked(2, 7)) {
                    positions.add(currentBoard.castle(x, y, 0, 7));
                }
                if (rightRook != null && !rightRook.hasMoved() && currentBoard.getPiece(5, 7) == null
                        && currentBoard.getPiece(6, 7) == null && !currentBoard.canBeAttacked(5, 7)
                        && !currentBoard.canBeAttacked(6, 7)) {
                    positions.add(currentBoard.castle(x, y, 7, 7));
                }
            }
        }

        return new ArrayList<>(positions.stream().filter(position -> position.valid()).collect(Collectors.toList()));
    }

    @Override
    public boolean canReach(int goalX, int goalY, Position currentPosition) {
        return Math.abs(x - goalX) < 2 && Math.abs(y - goalY) < 2 && Math.abs(x - goalX) + Math.abs(y - goalY) > 0;
    }
}