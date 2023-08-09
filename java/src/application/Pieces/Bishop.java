package application.Pieces;

import java.util.Arrays;
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
    public boolean[] blockCheck(Square[] board, Square square) {
        boolean[] blockMoves = new boolean[64];
        Arrays.fill(getMoves(), false);
        int index = Arrays.asList(board).indexOf(square);
        boolean[] moves = getMovement().BishopMovement(board, index, getMoves());

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
