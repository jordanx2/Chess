package application;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.Arrays;

import application.Pieces.Pawn;
import application.Pieces.PieceType;

public class Chess extends PApplet{
    Board board;
    Square[] boardSquares;
    Square selected;
    boolean whiteToMove;
    boolean TESTING = false;
    Rules rules;
    int moveCounter;
    boolean pawnPromotionOptions = false;
    int promotionBoxX = 0;
    int promotionBoxY = 0;


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
        moveCounter = 1;
    }

    public void mouseReleased(){
        if(!pawnPromotionOptions){
            checkIfPieceSelected();
            board.renderBoard();
            boardSquares = board.getSquares();
        }

        if(pawnPromotionOptions){
            displayPawnPromotion();
        }

    }

    boolean promotionDisplayed = false;
    public void displayPawnPromotion(){
        float boxW = board.getSquareW();
        
        if(!promotionDisplayed){
            PieceType[] types = new PieceType[]{PieceType.QUEEN, PieceType.KNIGHT, PieceType.ROOK, PieceType.BISHOP};
            String[] displayImages = new String[]{
                ChessSymbols.WHITE_CHESS_QUEEN_IMG, 
                ChessSymbols.WHITE_CHESS_KNIGHT_IMG, 
                ChessSymbols.WHITE_CHESS_ROOK_IMG,
                ChessSymbols.WHITE_CHESS_BISHOP_IMG
            };

            fill(255);
            stroke(0);

            PImage p;
            for(int i = 0; i < types.length; i++){
                square(promotionBoxX, promotionBoxY + (i * boxW), boxW);
                p = loadImage(displayImages[i]);
                image(p, promotionBoxX, promotionBoxY + (i * boxW), boxW, boxW);
            }

        }

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
        return whiteToMove && !(moveCounter % 2 == 0);
    }

    public void changeTurns(){ whiteToMove = !whiteToMove; }

    public void renderPossibleMoves(){
        if(checkSelected()){
            board.plotPotentialMoves(selected);
        }
    }

    public boolean isMoveMade(){
        if(checkSelected()){    
            boolean[] potentialMoves = board.getPotentialMoves();
            for(int i = 0; i < potentialMoves.length; i++){
                if(checkBounds(boardSquares[i].getX(), boardSquares[i].getY()) && potentialMoves[i]){
                    int selectedIndex = Arrays.asList(boardSquares).indexOf(selected);
                    if(Rules.getInstance().isInCheck()){
                        Rules.getInstance().checkResolved();
                    }

                    try{
                        if(((Pawn) boardSquares[selectedIndex].getPiece()).isPawnPromotion(i)){
                            System.out.println("Promotion: " + i);
                            pawnPromotionOptions = true;
                            promotionBoxX = mouseX;
                            promotionBoxY = mouseY;
                            return false;
                        }
                    } catch(Exception e){}
        

                    board.applyMove(i, selectedIndex);
                    moveCounter++;
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
                            renderPossibleMoves();
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


    public void draw() {}
}
