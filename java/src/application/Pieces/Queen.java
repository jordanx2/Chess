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

    @Override
    public boolean[] blockCheck(Square[] board, Square square) {
        boolean[] blockMoves = new boolean[64];
        Arrays.fill(getMoves(), false);
        int index = Arrays.asList(board).indexOf(square);

        boolean[] moves = possibleMoves(board, square);
        for(int i = 0; i < moves.length; i++ ){
            if(moves[i]){
                if(rules.canBlockCheck(board, index, i)){
                    blockMoves[i] = true;
                }
            }
        }
    
        return blockMoves;
    }
}
