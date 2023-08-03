package application;

import processing.core.PApplet;
import java.util.Arrays;

public class Chess extends PApplet{
    Board board;
    Square[] boardSquares;
    Square selected;
    boolean whiteToMove;
    boolean TESTING = true;
    Rules rules;


    public void settings() {
        size(800, 800);        
    }

    public void setup() {
        background(0);
        stroke(0);
        noFill();
        textSize(24);
        board = new Board(this, true);
        board.renderBoard();
        boardSquares = board.getSquares();
        whiteToMove = true;
        rules = Rules.getInstance();
    }

    public void mouseReleased(){
        checkIfPieceSelected();
        board.renderBoard();
        boardSquares = board.getSquares();
    }

    public boolean isValidSelection(String color){
        boolean isWhiteToMove = isWhitesTurn();
        if(isWhiteToMove && color.matches("WHITE")){
            return true;
        }

        else if(!isWhiteToMove && color.matches("BLACK")){
            return true; 
        }

        return false;
    }

    public boolean isWhitesTurn(){
        /*
         * If the move counter is even then it is blacks turn
         * Otherwise if its odd then it is whites turn
         */
        return whiteToMove && !(board.getMoveCounter() % 2 == 0);
    }

    public void changeTurns(){ whiteToMove = !whiteToMove; }

    public void renderPossibleMoves(){
        if(checkSelected()){
            board.plotPotentialMoves(selected);
        }
    }

    public boolean isMoveMade(){
        // System.out.println("testing: " + Rules.getInstance().isInCheck());
        if(checkSelected()){    
            boolean[] potentialMoves = board.getPotentialMoves();
            for(int i = 0; i < potentialMoves.length; i++){
                if(checkBounds(boardSquares[i].getX(), boardSquares[i].getY()) && potentialMoves[i]){
                    board.applyMove(i, Arrays.asList(boardSquares).indexOf(selected));
                    selected.setPiece(null);
                    board.setSquares(boardSquares);
                    removeSelected();
                    changeTurns();
                    return true;

                }
            }
        }
        return false;
    }

    public boolean checkIfPieceSelected(){
        if(isMoveMade()){  
            return false; 
        }

        for(Square s : boardSquares){
            if(checkBounds(s.getX(), s.getY())){

                if(s.getPiece() != null){
                    if(!s.isSelected()){
                        if(selected != null){
                            removeSelected();
                        }


                        if(isValidSelection(s.getPiece().getColor()) || TESTING){
                            selected = s;
                            selected.setSelected(true);
                            s.setColor(color(255, 255, 120));


                            // See if there is a CHECK move made
                            if(!Rules.getInstance().isInCheck()){
                                renderPossibleMoves();
                            }else{
                                board.renderPossibleCheckSolutions(selected);
                            }

                            return true;
                        } 

                        return false;
                        
                    } else{
                        removeSelected();
                    }
                } 
                else{
                    if(selected != null){
                        removeSelected();
                    }
                }
                return false;
            }
        }

        return false;
    }

    public void removeSelected(){;
        for(Square s : boardSquares){
            s.setColor(s.getOriginalColor());
        }
        
        selected.setSelected(false);
        selected = null;
        Arrays.fill(board.getPotentialMoves(), false);
    }

    public boolean checkSelected(){
        return selected != null;
    }

    public boolean checkBounds(float x, float y){
        float half = board.getHalf();
        if(dist(x + half, y + half, mouseX, mouseY) < 40){
            return true;
        }

        return false;
    }


    public void draw() {

    }
}
