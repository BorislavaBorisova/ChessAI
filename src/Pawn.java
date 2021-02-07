import java.util.ArrayList;

public class Pawn extends Piece {
    private int firstMove = -1;

    public Pawn(int x, int y, boolean color) {
        super(x, y, color);
    }

    @Override
    public Pawn clone() {
        Pawn clone = new Pawn(x, y, color);
        clone.setFirstMove(firstMove);
        return clone;
    }

    public void setFirstMove(int move) {
        firstMove = move;
    }

    public int getFirstMove() {
        return firstMove;
    }

    @Override
    public ArrayList<Move> generatePossibleMoves(Position currentBoard) {
        ArrayList<Move> moves = new ArrayList<>();
        int direction = color ? 1 : -1;
        if (currentBoard.getPiece(x, y + direction) == null) {
            if (y == (color ? 6 : 1)) {
                moves.add(new Move(this.clone(), x, y + direction, null, null, new Queen(x, y, color)));
                moves.add(new Move(this.clone(), x, y + direction, null, null, new Knight(x, y, color)));
                moves.add(new Move(this.clone(), x, y + direction, null, null, new Rook(x, y, color)));
                moves.add(new Move(this.clone(), x, y + direction, null, null, new Bishop(x, y, color)));
            } else {
                moves.add(new Move(this.clone(), x, y + direction, null, null, null));
            }
        }
        if (currentBoard.getPiece(x + 1, y + direction) != null
                && currentBoard.getPiece(x + 1, y + direction).color != color) {
            if (y == (color ? 6 : 1)) {
                moves.add(new Move(this.clone(), x + 1, y + direction, currentBoard.getPiece(x + 1, y + direction),
                        null, new Queen(x, y, color)));
                moves.add(new Move(this.clone(), x + 1, y + direction, currentBoard.getPiece(x + 1, y + direction),
                        null, new Knight(x, y, color)));
                moves.add(new Move(this.clone(), x + 1, y + direction, currentBoard.getPiece(x + 1, y + direction),
                        null, new Rook(x, y, color)));
                moves.add(new Move(this.clone(), x + 1, y + direction, currentBoard.getPiece(x + 1, y + direction),
                        null, new Bishop(x, y, color)));
            } else {
                moves.add(new Move(this.clone(), x + 1, y + direction, currentBoard.getPiece(x + 1, y + direction),
                        null, null));
            }
        }
        if (currentBoard.getPiece(x - 1, y + direction) != null
                && currentBoard.getPiece(x - 1, y + direction).color != color) {
            if (y == (color ? 6 : 1)) {
                moves.add(new Move(this.clone(), x - 1, y + direction, currentBoard.getPiece(x - 1, y + direction),
                        null, new Queen(x, y, color)));
                moves.add(new Move(this.clone(), x - 1, y + direction, currentBoard.getPiece(x - 1, y + direction),
                        null, new Knight(x, y, color)));
                moves.add(new Move(this.clone(), x - 1, y + direction, currentBoard.getPiece(x - 1, y + direction),
                        null, new Rook(x, y, color)));
                moves.add(new Move(this.clone(), x - 1, y + direction, currentBoard.getPiece(x - 1, y + direction),
                        null, new Bishop(x, y, color)));
            } else {
                moves.add(new Move(this.clone(), x - 1, y + direction, currentBoard.getPiece(x - 1, y + direction),
                        null, null));
            }
        }
        if (firstMove == -1 && currentBoard.getPiece(x, y + direction) == null
                && currentBoard.getPiece(x, y + direction * 2) == null) {
            moves.add(new Move(this.clone(), x, y + direction * 2, null, null, null));
        }
        if (y == (color ? 4 : 3) && currentBoard.getPiece(x + 1, y) instanceof Pawn
                && ((Pawn) currentBoard.getPiece(x + 1, y)).firstMove + 1 == currentBoard.getMove()) {
            moves.add(new Move(this.clone(), x + 1, y + direction, currentBoard.getPiece(x + 1, y), null, null));
        }
        if (y == (color ? 4 : 3) && currentBoard.getPiece(x - 1, y) instanceof Pawn
                && ((Pawn) currentBoard.getPiece(x - 1, y)).firstMove + 1 == currentBoard.getMove()) {
            moves.add(new Move(this.clone(), x - 1, y + direction, currentBoard.getPiece(x - 1, y), null, null));
        }

        return moves;
    }

    @Override
    public boolean canReach(int goalX, int goalY, Position currentPosition) {
        if (color) {
            if (goalY == y + 1 && (goalX == x + 1 || goalX == x - 1))
                return true;
        } else {
            if (goalY == y - 1 && (goalX == x + 1 || goalX == x - 1))
                return true;
        }
        return false;
    }
}
