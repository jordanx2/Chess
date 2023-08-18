package application.Pieces;

import application.Square;

public class Movement {
    public boolean[] RookMovement(Square[] board, int index, boolean[] moves) {
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++){
                if(Math.abs(x) != Math.abs(y)){
                    int idx = index + x + (y * 8);

                    loop: while(checkBounds(idx)){
                        if(x != 0){
                            if(isFirstOrLastColumn(idx)){
                                if((idx + 1) % 8 == 0 || idx % 8 == 0){
                                    if(!((index - idx) == 1) && !((idx - index) == 1)){
                                        if(canAttackSquare(board, idx, index)){
                                            moves[idx] = true;
                                        }
                                    }
                                }
                         

                                break loop;
                            }

                        } 

                        if(board[idx].getPiece() != null){       
                            if(board[index].getPiece().getColor() != board[idx].getPiece().getColor()){
                                moves[idx] = true;
                            }
                            break loop;
                        } else{
                            moves[idx] = true;
                        }

                        if(x != 0){
                            idx += x;
                        }

                        if(y != 0){
                            idx += (y * 8);
                        }
    
                    }

                }
            }
        }

        return moves;
    }

    public boolean canAttackSquare(Square[] board, int idx, int index){
        if(board[idx].getPiece() != null){       
            if(board[index].getPiece().getColor() != board[idx].getPiece().getColor()){
                return true;
            }
        } else{
            return true;
        }

        return false;
    }

    public boolean[] BishopMovement(Square[] board, int index, boolean[] moves) {
        for(int x = -1; x <= 1; x++){
            for(int y = -1; y <= 1; y++){
                if (Math.abs(x) == Math.abs(y) && x != 0 && y != 0) {
                    int idx = index + x + (y * 8);

                    loop: while(checkBounds(idx)){  
                        // Basically bounds checking
                        if((index % 8 == 0 && idx % 8 == 7) || (index % 8 == 7 && idx % 8 == 0)){
                            break loop;
                        }

                        if(board[idx].getPiece() != null){
                            if(board[index].getPiece().getColor() != board[idx].getPiece().getColor()){
                                moves[idx] = true;
                            }
                            break loop;
                        }

                        // First check if the original pos of the piece is on the first or last column
                        if(isFirstOrLastColumn(index)){
                            // Check if the pos of the piece plus the x increment overlaps to a first or last column
                            if(isFirstOrLastColumn(index + x)){
                                break loop;
                            }
                        }

                        // Check if the potential move is in either first or last col or row
                        if(isFirstOrLastRow(idx) || isFirstOrLastColumn(idx)){
                            if(board[idx].getPiece() == null){
                                moves[idx] = true;
                            }
                            break loop;
                        }

                        if(checkBounds(idx)){
                            moves[idx] = true;
                        }
                        idx += x;
                        idx += (y * 8);
                        
                    }
                }
            }
        
        }

        return moves;
    }
    
    public boolean checkBounds(int idx){
        if(idx >= 0 && idx <= 63) return true;
        
        return false ;
    }

    public boolean isFirstOrLastColumn(int index) {
        int column = index % 8;
        return column == 0 || column == 7;
    }

    public boolean isFirstOrLastRow(int index) {
        int row = index / 8;
        return row == 0 || row == 8;
    }
}
