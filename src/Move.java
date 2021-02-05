
public class Move {
    
    private Piece pieceToMove;
    private int pieceToMoveNewX;
    private int pieceToMoveNewY;
    
    private Piece pieceToBeTaken;
    
    private Rook castlingRook;
    private Piece promotedPiece;

    public Move(Piece pieceToMove, int newX, int newY, Piece pieceToBeTaken, Rook castlingRook, Piece promotedPiece) {
        this.pieceToMove = pieceToMove;
        pieceToMoveNewX = newX;
        pieceToMoveNewY = newY;
        this.pieceToBeTaken = pieceToBeTaken;
        this.castlingRook = castlingRook;
        this.promotedPiece = promotedPiece;
    }
    
    public boolean isCapture() {
        return pieceToBeTaken != null;
    }
    
    public Piece getPieceToMove() {
        return pieceToMove;
    }
    
    public int getX() {
        return pieceToMoveNewX;
    }
    
    public int getY() {
        return pieceToMoveNewY;
    }
    
    public Piece getPieceToBeTaken() {
        return pieceToBeTaken;
    }
    
    public Rook getCastlingRook() {
        return castlingRook;
    }
    
    public boolean isCastle() {
        return castlingRook != null;
    }
    
    public Piece getPromotedPiece() {
        return promotedPiece;
    }
    
    public boolean isPromotion() {
        return promotedPiece != null;
    }
}
