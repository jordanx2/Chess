package application.Pieces;

import java.util.Arrays;

import application.Square;

public class Pawn extends Piece{
    private boolean startingPos;
    private int step;

    public Pawn(PieceType pieceName, String pieceUnicode, String color, String imgPath) {
        super(pieceName, pieceUnicode, color, imgPath);
        this.startingPos = true;

        // With black pawns we increment, with white pawns we decrement
        this.step = (color.equals("WHITE")) ? -1 : 1;
    }

    @Override
    public boolean[] possibleMoves(Square[] board, Square square) {
        int step = getStep();
        boolean[] moves = getMoves();
        Arrays.fill(moves, false);
        int index = Arrays.asList(board).indexOf(square);

        // Promotion
        if(getMovement().isFirstOrLastColumn(index)){
            System.out.println("PROMOTE");
            return moves;
        }

        Piece piece = board[index + step].getPiece();

        if(startingPos){
            if(piece == null && board[index + (step * 2)].getPiece() == null){
                moves[index + step] = true;
                moves[index + (step * 2)] = true;
            }

            else if(piece == null && board[index + (step * 2)].getPiece() != null){
                moves[index + step] = true;
            }
        } else{
            if(piece == null){
                moves[index + step] = true;
            }
        }

        // Calculate attack moves
        int rightAttack = (index + step) + 8;
        int leftAttack = (index + step) - 8;

        if(rightAttack >= 0 && rightAttack <= 63){
            if(board[rightAttack].getPiece() != null && board[rightAttack].getPiece().getColor() != board[index].getPiece().getColor()){
                moves[rightAttack] = true;
            }

            if(checkForEnpassant(board, rightAttack, index, index + 8)){
                moves[rightAttack] = true;
            }

        }

        if(leftAttack >= 0 && leftAttack <= 63){
            if(board[leftAttack].getPiece() != null && board[leftAttack].getPiece().getColor() != board[index].getPiece().getColor()){
                moves[leftAttack] = true;
            }
            
            if(checkForEnpassant(board, leftAttack, index, index - 8)){
                moves[leftAttack] = true;
            }
        }

        return moves;
    }

    private boolean checkForEnpassant(Square[] board, int attackSquare, int index, int sameRankCheck){
        // Check if its a square where enpassant is true
        if(board[attackSquare].isEnpassantSquare()){

            // Check if the piece on the same rank is a pawn
            if(board[sameRankCheck].getPiece() != null && board[sameRankCheck].getPiece().getPieceName() == PieceType.PAWN){

                // Check if the piece is of the opposite color
                if(board[index].getPiece().getColor() != board[sameRankCheck].getPiece().getColor()){
                    return true;
                }

            }
        }

        return false;
    }

    public boolean checkEnpassant(){
        return true;
    }

    public boolean isStartingPos() {
        return startingPos;
    }

    public void setStartingPos(boolean startingPos) {
        this.startingPos = startingPos;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    @Override
    public CheckPair blockCheck(Square[] board, Square square) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'blockCheck'");
    }
    
}
