import java.util.ArrayList;

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
    public ArrayList<Move> generatePossibleMoves(Position currentBoard) {
        ArrayList<Move> moves = new ArrayList<>();
        Piece down = currentBoard.getPiece(x, y - 1);
        Piece downRight = currentBoard.getPiece(x + 1, y - 1);
        Piece right = currentBoard.getPiece(x + 1, y);
        Piece upRight = currentBoard.getPiece(x + 1, y + 1);
        Piece up = currentBoard.getPiece(x, y + 1);
        Piece upLeft = currentBoard.getPiece(x - 1, y + 1);
        Piece left = currentBoard.getPiece(x - 1, y);
        Piece downLeft = currentBoard.getPiece(x - 1, y - 1);
        if (down == null || (down != null && down.color != color)) {
            moves.add(new Move(this.clone(), x, y - 1, currentBoard.getPiece(x, y - 1), null, null));
        }
        if (downRight == null || (downRight != null && downRight.color != color)) {
            moves.add(new Move(this.clone(), x + 1, y - 1, currentBoard.getPiece(x + 1, y - 1), null, null));
        }
        if (right == null || (right != null && right.color != color)) {
            moves.add(new Move(this.clone(), x + 1, y, currentBoard.getPiece(x + 1, y), null, null));
        }
        if (upRight == null || (upRight != null && upRight.color != color)) {
            moves.add(new Move(this.clone(), x + 1, y + 1, currentBoard.getPiece(x + 1, y + 1), null, null));
        }
        if (up == null || (up != null && up.color != color)) {
            moves.add(new Move(this.clone(), x, y + 1, currentBoard.getPiece(x, y + 1), null, null));
        }
        if (upLeft == null || (upLeft != null && upLeft.color != color)) {
            moves.add(new Move(this.clone(), x - 1, y + 1, currentBoard.getPiece(x - 1, y + 1), null, null));
        }
        if (left == null || (left != null && left.color != color)) {
            moves.add(new Move(this.clone(), x - 1, y, currentBoard.getPiece(x - 1, y), null, null));
        }
        if (downLeft == null || (downLeft != null && downLeft.color != color)) {
            moves.add(new Move(this.clone(), x - 1, y - 1, currentBoard.getPiece(x - 1, y - 1), null, null));
        }
        if (!hasMoved) {
                Piece leftCorner = currentBoard.getPiece(0, y);
                Piece rightCorner = currentBoard.getPiece(7, y);
                Rook leftRook = null, rightRook = null;
                if (leftCorner instanceof Rook) {
                    leftRook = (Rook) leftCorner;
                }
                if (rightCorner instanceof Rook) {
                    rightRook = (Rook) rightCorner;
                }
                if (leftRook != null && !leftRook.hasMoved() && currentBoard.getPiece(x - 1, y) == null
                        && currentBoard.getPiece(x - 2, y) == null && currentBoard.getPiece(x - 3, y) == null
                        && !currentBoard.canBeAttacked(x - 1, y)
                        && !currentBoard.canBeAttacked(x - 2, y)) {
                    moves.add(new Move(this.clone(), x - 2, y, null, leftRook.clone(), null));
                }
                if (rightRook != null && !rightRook.hasMoved() && currentBoard.getPiece(x + 1, 0) == null
                        && currentBoard.getPiece(x + 2, 0) == null && !currentBoard.canBeAttacked(x + 1, 0)
                        && !currentBoard.canBeAttacked(x + 2, 0)) {
                    moves.add(new Move(this.clone(), x + 2, y, null, rightRook.clone(), null));
                }
            
        }

        return moves;
    }

    @Override
    public boolean canReach(int goalX, int goalY, Position currentPosition) {
        return Math.abs(x - goalX) < 2 && Math.abs(y - goalY) < 2 && Math.abs(x - goalX) + Math.abs(y - goalY) > 0;
    }
}