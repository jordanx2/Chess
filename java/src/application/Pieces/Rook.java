package application.Pieces;

import java.util.Arrays;

import application.Square;

public class Rook extends Piece{

    public Rook(PieceType pieceName, String pieceUnicode, String color, String imgPath) {
        super(pieceName, pieceUnicode, color, imgPath);
    }

    @Override
    public boolean[] possibleMoves(Square[] board, Square square) {
        Arrays.fill(getMoves(), false);
        int index = Arrays.asList(board).indexOf(square);
        setMoves(getMovement().RookMovement(board, index, getMoves()));
        return getMoves();
    }

    @Override
    public CheckPair blockCheck(Square[] board, Square square) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'blockCheck'");
    }    
}
