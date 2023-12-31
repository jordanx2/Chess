package application.Pieces;

import java.util.Arrays;
import application.Square;

public class Queen extends Piece {

    public Queen(PieceType pieceName, String pieceUnicode, String color, String imgPath) {
        super(pieceName, pieceUnicode, color, imgPath);
    }

    @Override
    public boolean[] possibleMoves(Square[] board, Square square) {
        Arrays.fill(getMoves(), false);
        int index = Arrays.asList(board).indexOf(square);
        setMoves(getMovement().RookMovement(board, index, getMoves()));
        setMoves(getMovement().BishopMovement(board, index, getMoves()));
        return getMoves();
    }
    
}
