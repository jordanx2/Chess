package application.Pieces;

import java.util.Arrays;

import application.BlockCheck;
import application.Square;

public class Bishop extends Piece {
    boolean[] visitedCols;
    public Bishop(PieceType pieceName, String pieceUnicode, String color, String imgPath) {
        super(pieceName, pieceUnicode, color, imgPath);
        this.visitedCols = new boolean[9];
    }

    @Override
    public boolean[] possibleMoves(Square[] board, Square square) {
        Arrays.fill(getMoves(), false);
        int index = Arrays.asList(board).indexOf(square);
        setMoves(getMovement().BishopMovement(board, index, getMoves()));
        return getMoves();
    }

    @Override
    public BlockCheck blockCheck(Square[] board, Square square) {
        throw new UnsupportedOperationException("Unimplemented method 'blockCheck'");
    }
    
}
