package application.Pieces;

import java.util.Arrays;
import application.Square;

public class Rook extends Piece{

    private boolean hasRookMoved;

    public Rook(PieceType pieceName, String pieceUnicode, String color, String imgPath) {
        super(pieceName, pieceUnicode, color, imgPath);
        this.hasRookMoved = false;
    }

    @Override
    public boolean[] possibleMoves(Square[] board, Square square) {
        Arrays.fill(getMoves(), false);
        int index = Arrays.asList(board).indexOf(square);
        setMoves(getMovement().RookMovement(board, index, getMoves()));
        return getMoves();
    }

    public boolean isHasRookMoved() {
        return hasRookMoved;
    }

    public void setHasRookMoved(boolean hasRookMoved) {
        this.hasRookMoved = hasRookMoved;
    }
    
}
