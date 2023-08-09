package application.Pieces;

import java.util.Arrays;

import application.Square;
import application.Rules;

public class King extends Piece{

    private boolean kingMoved;
    private boolean inCheck;
    private boolean inCheckMate;


    public void setInCheckMate(boolean inCheckMate) {
        this.inCheckMate = inCheckMate;
    }

    public King(PieceType pieceName, String pieceUnicode, String color, String imgPath) {
        super(pieceName, pieceUnicode, color, imgPath);
        this.kingMoved = false;
        this.inCheck = false;
        this.inCheckMate = false;
    }

    @Override
    public boolean[] possibleMoves(Square[] board, Square square) {
        setMoves(new boolean[64]);
        Arrays.fill(getMoves(), false);
        int index = Arrays.asList(board).indexOf(square);

        for(int i = -1; i <= 1; i++){
            for(int j = -1; j <= 1; j++){
                int idx = index + j + (8 * i);
                if(getCheck().checkBounds(index, idx, board.length) && board[idx].getPiece() == null){
                    getMoves()[idx] = true;
                }

                if(getCheck().checkBounds(index, idx, board.length) && board[idx].getPiece() != null){
                    if(board[idx].getPiece().getColor() != board[index].getPiece().getColor()){
                        getMoves()[idx] = true;
                    }
                }
            }
        }

        if(checkKingSideCastling(board, index)){
            getMoves()[index + 16] = true;
        }

        if(checkQueenSideCastling(board, index)){
            getMoves()[index - 16] = true;
        }

        return getMoves();
    }  
    
    public boolean checkKingSideCastling(Square[] board, int index){
        // Check if the king has moved from its original square 
        if(!isKingMoved()){
            if(board[index + 8].getPiece() == null && board[index + 16].getPiece() == null){
                return true;
            }
        }
        return false;
    }

    public boolean checkQueenSideCastling(Square[] board, int index){
        // Check if the king has moved from its original square 
        if(!isKingMoved()){
            if(board[index - 8].getPiece() == null && board[index - 16].getPiece() == null){
                return true;
            }
        }
        return false;
    }

    public boolean[] solveCheck(Square[] board){
        /*
             * Firstly check can the king move to safety

             * Secondly see if the king can capture the piece that is performing the check
        */
        
        // Get the index of the king that is in check
        int index = rules.indexKingInCheck;

        // Will contain all of the simple moves that the king can make to get out of check
        boolean[] safeMoves = new boolean[64];

        // Call the local method to get all of the moves that the king can generally make
        safeMoves = possibleMoves(board, board[index]);

        // Filter out all of the moves that if the king makes will still make the king in check
        for(int i = 0; i < safeMoves.length; i++){
            // System.out.println("i: " + i + ", test: " + safeMoves[i]);
            if(safeMoves[i]){
                if(rules.isInCheck(board, rules.attackPiece, i)){
                    safeMoves[i] = false;
                    
                }

            }
        }

        return safeMoves;

    }

    // Getters and Setters
    public boolean isKingMoved() {
        return kingMoved;
    }

    public void setKingMoved(boolean kingMoved) {
        this.kingMoved = kingMoved;
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

    public boolean isInCheckMate() {
        return inCheckMate;
    }

    public Rules getRules() {
        return rules;
    }

    public void setRules(Rules rules) {
        this.rules = rules;
    }

    @Override
    public boolean[] blockCheck(Square[] board, Square square) {
        throw new UnsupportedOperationException("Unimplemented method 'blockCheck'");
    }
    
}
