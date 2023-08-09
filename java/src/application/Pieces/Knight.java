package application.Pieces;

import java.util.Arrays;
import application.Square;

public class Knight extends Piece{
    public Knight(PieceType pieceName, String pieceUnicode, String color, String imgPath) {
        super(pieceName, pieceUnicode, color, imgPath);
    }

    @Override
    public boolean[] possibleMoves(Square[] board, Square square) {
        Arrays.fill(getMoves(), false);
        int index = Arrays.asList(board).indexOf(square);

        for(int x = -2; x <= 2; x++){
            for(int y = -2; y <= 2; y++){
                if(x != 0 && y != 0 && x != y && Math.abs(x) != y && Math.abs(y) != x){
                    int idx = index + y + (8 * x);

                    if(getCheck().checkBounds(index, idx, board.length) && board[idx].getPiece() == null){
                        getMoves()[idx] = true;
                    }

                    // Check attack moves
                    if(getCheck().checkBounds(index, idx, board.length) && board[idx].getPiece() != null && board[idx].getPiece().getColor() != board[index].getPiece().getColor()){
                        getMoves()[idx] = true;
                    }
                }
            }
        }

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
