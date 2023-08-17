package application;

import java.util.Arrays;

import application.Pieces.*;

public class Rules {
    private static Rules instance;
    public int indexKingInCheck;
    public String colorPieceInCheck;
    public int attackPiece;
    private boolean inCheck;
    private boolean isCheckMate;

    // Singleton
    private Rules(){ 
        instance = null; 
        indexKingInCheck = -1;
        attackPiece = -1;
        inCheck = false;
        colorPieceInCheck = null;
        isCheckMate = false;
    }
    
    public static Rules getInstance(){
        if(instance == null){
            instance = new Rules();
        }

        return instance;
    }

    public boolean isInCheck(Square[] board, int attackIndex, int kingIndex){
        // King index isn't given, must find
        if(kingIndex == -1){
            if(board[attackIndex].getPiece() == null){
                return false;
            }

            // Here we need to find the king index of the opposite color 
            kingIndex = findKingIndex(board, board[attackIndex].getPiece().getColor().matches("WHITE") ? "BLACK" : "WHITE");
        }

        if(board[attackIndex].retrievePossibleMoves(board)[kingIndex]){
                indexKingInCheck = kingIndex;
                attackPiece = attackIndex;
                inCheck = true;
                return true;
        }

        return false;

    }

    public int findKingIndex(Square[] board, String color){
        for(int i = 0; i < board.length; i++){
            if(board[i].getPiece() != null){
                if(board[i].getPiece().getPieceName() == PieceType.KING && board[i].getPiece().getColor().matches(color)){
                    return i;
                }
            }

        }
        return -1;
    }

    // Function to determine if a move is made that puts its own king in danger
    public boolean[] parseSafeMoves(Square[] board, Square selected, boolean[] possibleMoves){
        // Get the color of the selected piece
        boolean kingSelected = selected.getPiece() instanceof King;
        String color = selected.getPiece().getColor();
        int pinnedKing = -1;

        // Find the index of the king potentially under attack
        if(!kingSelected){
            // Here we need to find the king index on the same team as selected
            pinnedKing = findKingIndex(board, selected.getPiece().getColor());

        } else{
            pinnedKing = Arrays.asList(board).indexOf(selected);
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
                            if(kingSelected){
                                if(isInCheck(board, square, move)){
                                    possibleMoves[move] = false;
                                }     
                            } else{
                                // See if the temp move has put that selected piece king in danger
                                if(isInCheck(board, square, pinnedKing)){
                                    // Mark as unplayable move
                                    possibleMoves[move] = false;
                                }
                            }
                        }
                    }
                }

                // Undo the temporary move
                board[move].setPiece(moveTemp);
                board[selectedIdx].setPiece(selectedOg);
            }
        }

        if(kingSelected){
            if(checkKingSideCastling(board, selectedIdx, possibleMoves)){
                possibleMoves[selectedIdx + 16] = true;
            }

            if(checkQueenSideCastling(board, selectedIdx, possibleMoves)){
                possibleMoves[selectedIdx - 16] = true;
            }
        }

        return possibleMoves;

    }

    public boolean checkKingSideCastling(Square[] board, int index, boolean[] moves){
        // Check if the king has moved from its original square 
        King king = ((King) board[index].getPiece());

        if(!king.isKingMoved()){
            int nextColumn = 8;
            int kingSideRook = index + (nextColumn * 3);

            // Check the see if the king side rook is on its home square
            if(board[kingSideRook].getPiece() != null && board[kingSideRook].getPiece() instanceof Rook){
                // See if the rook hasn't moved yet
                if(!((Rook) board[kingSideRook].getPiece()).isHasRookMoved()){
                    if(moves[index + nextColumn] && moves[index + (nextColumn * 2)]){
                        return true;
                    }
                }
            } 
            moves[index + (nextColumn * 2)] = false;
        }


        return false;
    }

    public boolean checkQueenSideCastling(Square[] board, int index, boolean[] moves){
        // Check if the king has moved from its original square 
        King king = ((King) board[index].getPiece());

        if(!king.isKingMoved()){
            int nextColumn = 8;
            int queenSideRook = index - (nextColumn * 4);

            // Check the see if the king side rook is on its home square
            if(board[queenSideRook].getPiece() != null && board[queenSideRook].getPiece() instanceof Rook){
                // See if the rook hasn't moved yet
                if(!((Rook) board[queenSideRook].getPiece()).isHasRookMoved()){
                    if(moves[index - nextColumn] && moves[index - (nextColumn * 2)]){
                        return true;
                    }
                }
            } 
            moves[index - (nextColumn * 2)] = false;
        }


        return false;
    }

    public void checkResolved(){
        indexKingInCheck = -1;
        inCheck = false;
        colorPieceInCheck = null;
    }

    public boolean isCheckMate(Square[] board, int moveIdx){
        String teamCheckMateColor = board[moveIdx].getPiece().getColor().matches("WHITE") ? "BLACK" : "WHITE";
        
         for(int i = 0; i < board.length; i++){
            if(board[i].getPiece() != null){
                if(board[i].getPiece().getColor().matches(teamCheckMateColor)){
                    boolean[] safeMoves = parseSafeMoves(board, board[i], board[i].getPiece().possibleMoves(board, board[i]));
                    for(int j = 0; j < safeMoves.length; j++){
                        if(safeMoves[j]){   
                            return false;
                        }
                    }
                }
            }
         }
         
        isCheckMate = true;

        return true;
    }

    public boolean isInCheck() {
        return inCheck;
    }

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

    public boolean isCheckMate() {
        return isCheckMate;
    }

    public void setCheckMate(boolean isCheckMate) {
        this.isCheckMate = isCheckMate;
    }

}


