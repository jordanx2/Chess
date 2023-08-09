package application;

import application.Pieces.*;

public class Rules {
    private static Rules instance;
    public int indexKingInCheck;
    public String colorPieceInCheck;
    public int attackPiece;
    private boolean inCheck;

    // Singleton
    private Rules(){ 
        instance = null; 
        indexKingInCheck = -1;
        attackPiece = -1;
        inCheck = false;
        colorPieceInCheck = null;
    }
    
    public static Rules getInstance(){
        if(instance == null){
            instance = new Rules();
        }

        return instance;
    }



    public boolean isInCheck(Square[] board, int attackIndex){
        boolean[] attackMoves = board[attackIndex].retrievePossibleMoves(board);
        for(int i = 0; i < attackMoves.length; i++){
            if(attackMoves[i] && board[i].getPiece() != null && board[i].getPiece().getPieceName() == PieceType.KING){
                ((King) board[i].getPiece()).setInCheck(true);
                indexKingInCheck = i;
                attackPiece = attackIndex;
                inCheck = true;
                return true;

            }
            
        }
        return false;
    }

    public boolean isInCheck(Square[] board, int attackIndex, int kingIndex){
        return board[attackIndex].retrievePossibleMoves(board)[kingIndex];
    }

    public boolean canBlockCheck(Square[] board, int previousPieceSquare, int potentialBlockCheckSquare){
        // Make the potential move to see if it solves the check
        board[potentialBlockCheckSquare].setPiece(board[previousPieceSquare].getPiece());

        /*
         * isInCheck returns true if the king is still in check.
         * So, if isInCheck returns false then return true from this function
         * indicating that the move can block the check
         * 
         */

        boolean solvesCheck = !isInCheck(board, attackPiece, indexKingInCheck);

        // Undo the move as this function doesn't deal with making moves
        board[potentialBlockCheckSquare].setPiece(null);
        return solvesCheck;
        
    }

    public void checkResolved(){
        indexKingInCheck = -1;
        inCheck = false;
        colorPieceInCheck = null;
    }


    public boolean isCheckMate(Square[] board, int kingIndex){

        return true;
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }


}
