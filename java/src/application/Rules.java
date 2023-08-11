package application;

import java.util.Arrays;

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
        Piece temp = null;
        if(board[potentialBlockCheckSquare].getPiece() != null){
            temp = board[potentialBlockCheckSquare].getPiece();
        }

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
        board[potentialBlockCheckSquare].setPiece(temp);
        return solvesCheck;
        
    }

    public void checkResolved(){
        indexKingInCheck = -1;
        inCheck = false;
        colorPieceInCheck = null;
    }

    // Function to determine if a move is made that puts its own king in danger
    public boolean[] parseSafeMoves(Square[] board, Square selected, boolean[] possibleMoves){
        // Get the color of the selected piece
        String color = selected.getPiece().getColor();
        int pinnedKing = -1;

        // Find the index of the king potentially under attack
        kingFind: for(int i = 0; i < board.length; i++){
            if(board[i].getPiece() != null){
                if(board[i].getPiece().getPieceName() == PieceType.KING && board[i].getPiece().getColor().matches(color)){
                    pinnedKing = i;
                    break kingFind;
                }
            }

        }

        /*
         * Go through all possible selected square moves
         * Temporally make each one of those moves
         * Then see if the selected piece has put its own king in danger
         * If so, make that move as a non-move 
         */
        int selectedIdx = Arrays.asList(board).indexOf(selected);
        for(int move = 0; move < possibleMoves.length; move++){
            if(possibleMoves[move]){
                Piece selectedOg = board[selectedIdx].getPiece();
                Piece moveTemp = board[move].getPiece();

                // Temporally make the move
                board[move].setPiece(selectedOg);
                board[selectedIdx].setPiece(null);

                for(int square = 0; square < board.length; square++){
                    if(board[square].getPiece() != null){
                        if(!board[square].getPiece().getColor().matches(color)){
                            // See if the temp move has put that selected piece king in danger
                            if(isInCheck(board, square, pinnedKing)){
                                // Mark as unplayable move
                                possibleMoves[move] = false;
                            }
                        }
                    }
                }

                // Undo the temporary move
                board[move].setPiece(moveTemp);
                board[selectedIdx].setPiece(selectedOg);
            }
        }

        return possibleMoves;

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
