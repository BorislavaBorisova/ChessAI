public class Hash {
    private int hashCode;
    private Pawn canBeTaken;

    public static final int WHITE_PAWN = 1;
    public static final int WHITE_ROOK = 2;
    public static final int WHITE_KNIGHT = 3;
    public static final int WHITE_BISHOP = 4;
    public static final int WHITE_KING = 5;
    public static final int WHITE_QUEEN = 6;
    public static final int WHITE_KING_MOVED = 7;
    public static final int WHITE_ROOK_MOVED = 8;
    public static final int WHITE_CAN_BE_EN_PASSANTED_PAWN = 9;
    public static final int BLACK_PAWN = 10;
    public static final int BLACK_ROOK = 11;
    public static final int BLACK_KNIGHT = 12;
    public static final int BLACK_BISHOP = 13;
    public static final int BLACK_KING = 14;
    public static final int BLACK_QUEEN = 15;
    public static final int BLACK_KING_MOVED = 16;
    public static final int BLACK_ROOK_MOVED = 17;
    public static final int BLACK_CAN_BE_EN_PASSANTED_PAWN = 18;

    public Hash(Position position, int[][] table, int whiteTurn, int blackTurn){
        hashCode = hash(position.getBoard(), table, whiteTurn, blackTurn, position.getTurn());
        canBeTaken = null;
    }

    private int findPieceIndex(Piece piece, boolean isMoving){
        int index;
        boolean pieceColor = piece.color;
        if (piece instanceof Rook){
            index = pieceColor
                    ? (((Rook)piece).hasMoved() || isMoving ? WHITE_ROOK_MOVED : WHITE_ROOK)
                    : (((Rook)piece).hasMoved() || isMoving ? BLACK_ROOK_MOVED : BLACK_ROOK);
        } else if (piece instanceof Knight){
            index = pieceColor ? WHITE_KNIGHT : BLACK_KNIGHT;
        } else if (piece instanceof Bishop){
            index = pieceColor ? WHITE_BISHOP : BLACK_BISHOP;
        } else if (piece instanceof Queen){
            index = pieceColor ? WHITE_QUEEN : BLACK_QUEEN;
        } else if (piece instanceof King){
            index = pieceColor
                    ? (((King)piece).hasMoved() || isMoving ? WHITE_KING_MOVED : WHITE_KING)
                    : (((King)piece).hasMoved() || isMoving ? BLACK_KING_MOVED : BLACK_KING);
        } else {
            index = pieceColor ? WHITE_PAWN : BLACK_PAWN;
        }
        return index;
    }

    public int hash(Piece[][] board, int[][] table, int whiteTurn, int blackTurn, boolean currentTurn){
        int hash = 0, k;
        for (int i = 0; i < board[0].length; i++) {
            for (int j = 0; j < board.length; j++) {
                if(board[i][j] != null){
                    k = findPieceIndex(board[i][j], false);
                    hash = hash ^ table[j * board[0].length + i][k];
                }
            }
        }
        hash ^= (currentTurn ? whiteTurn : blackTurn);
        return hash;
    }

    public int hash(Move move, int[][] table, boolean turn, int whiteTurn, int blackTurn){
        hashCode ^= whiteTurn ^ blackTurn;
        if (canBeTaken != null) {
            hashCode ^= table[canBeTaken.y * 8 + canBeTaken.x][canBeTaken.color ? WHITE_CAN_BE_EN_PASSANTED_PAWN : BLACK_CAN_BE_EN_PASSANTED_PAWN];
            hashCode ^= table[canBeTaken.y * 8 + canBeTaken.x][canBeTaken.color ? WHITE_PAWN : BLACK_PAWN];
            canBeTaken = null;
        }
        if (move.isCapture() && !move.isPromotion()){
            //remove from old place
            hashCode ^= table[move.getPieceToMove().y * 8 + move.getPieceToMove().x][findPieceIndex(move.getPieceToMove(), false)];
            //take piece
            hashCode ^= table[move.getPieceToBeTaken().y * 8 + move.getPieceToBeTaken().x][findPieceIndex(move.getPieceToBeTaken(), false)];
            //put on new place
            hashCode ^= table[move.getY() * 8 + move.getX()][findPieceIndex(move.getPieceToMove(), true)];
        } else if (!move.isCapture() && !move.isPromotion() && !move.isCastle() && !move.isPawnJump()){
            //remove from old place
            hashCode ^= table[move.getPieceToMove().y * 8 + move.getPieceToMove().x][findPieceIndex(move.getPieceToMove(), false)];
            //put on new place
            hashCode ^= table[move.getY() * 8 + move.getX()][findPieceIndex(move.getPieceToMove(), true)];
        } else if (move.isCastle()){
            //remove king from old place
            hashCode ^= table[move.getPieceToMove().y * 8 + move.getPieceToMove().x][turn ? WHITE_KING : BLACK_KING];
            //put king on new place
            hashCode ^= table[move.getY() * 8 + move.getX()][turn ? WHITE_KING_MOVED : BLACK_KING_MOVED];
            //remove rook from old place
            hashCode ^= table[move.getCastlingRook().y * 8 + move.getCastlingRook().x][turn ? WHITE_ROOK : BLACK_ROOK];
            //put rook on new place
            hashCode ^= table[move.getY() * 8 + move.getX() + (move.getPieceToMove().x > move.getCastlingRook().x ? 1 : -1)][turn ? WHITE_ROOK_MOVED : BLACK_ROOK_MOVED];
        } else if (move.isPromotion()){
            //remove pawn
            hashCode ^= table[move.getPieceToMove().y * 8 + move.getPieceToMove().x][turn ? WHITE_PAWN : BLACK_PAWN];
            //add desired piece
            hashCode ^= table[move.getY() * 8 + move.getX()][findPieceIndex(move.getPromotedPiece(), true)];
            //if capture remove the captured piece
            if(move.isCapture()){
                hashCode ^= table[move.getPieceToBeTaken().y * 8 + move.getPieceToBeTaken().x][findPieceIndex(move.getPieceToBeTaken(), false)];
            }
        } else if (move.isPawnJump()){
            canBeTaken = (Pawn)move.getPieceToMove().clone();
            canBeTaken.setCoordinates(move.getX(), move.getY());
            //remove from old place
            hashCode ^= table[move.getPieceToMove().y * 8 + move.getPieceToMove().x][turn ? WHITE_PAWN : BLACK_PAWN];
            //put on new place
            hashCode ^= table[canBeTaken.y * 8 + canBeTaken.x][turn ? WHITE_CAN_BE_EN_PASSANTED_PAWN : BLACK_CAN_BE_EN_PASSANTED_PAWN];
        }
        return hashCode;
    }
}
