package application;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PImage;
import java.util.Arrays;

public class Chess extends PApplet{
    Board board;
    Square[] boardSquares;
    Square selected;
    boolean whiteToMove;
    boolean TESTING = false;
    Rules rules;
    int moveCounter;
    PromotionBlock promotionBlock;
    int boardSpawnX;
    int boardSpawnY;
    PlayerTimer whiteTimer;
    PlayerTimer blackTimer;
    PlaySound playSound;
    
    public void settings() {
        size(1000, 900);             
    }

    public void setup() {
        frameRate(10);
        background(0);
        stroke(0);
        noFill();
        textSize(24);
        PFont font = createFont("Arial", 32);
        textFont(font);
        board = new Board(this, true);
        board.renderBoard();
        boardSquares = board.getSquares();
        whiteToMove = true;
        rules = Rules.getInstance();
        moveCounter = 1;
        promotionBlock = PromotionBlock.getInstance();

        int timeConstraint = 10;
        whiteTimer = new PlayerTimer(timeConstraint, true, this, board, board.getBorder() + (board.getSquareW() * 7));
        blackTimer = new PlayerTimer(timeConstraint, false, this, board, board.getBorder());
        playSound = PlaySound.getInstance(this);
        whiteTimer.start();
        blackTimer.start();
        playSound.start();
    }

    public void mouseReleased(){
        if(!rules.isGameOver(whiteTimer, blackTimer)){
            checkIfPieceSelected();
            if(PromotionBlock.pawnPromotionOptions){
                displayPawnPromotion();
            } else{
                board.renderBoard();
                renderPlayerTimes();
            }
            boardSquares = board.getSquares();  
        }
    }

    public void displayPawnPromotion(){
        float boxW = board.getSquareW();
        
        // Display the promotion block
        if(!PromotionBlock.promotionDisplayed){
            fill(255);
            stroke(0);
            
            PImage p;
            for(int i = 0; i < promotionBlock.types.length; i++){
                float x = promotionBlock.blockSpawnX;
                float y = promotionBlock.verticleIncrement(i, boxW);

                promotionBlock.promotionSquares[i].promotionBoxX = x;
                promotionBlock.promotionSquares[i].promotionBoxY = y;

                square(x, y, boxW);
                p = loadImage(promotionBlock.displayImages[i]);
                image(p, x, y, boxW, boxW);
            }

            PromotionBlock.promotionDisplayed = true;
            noFill();

        }

        // Check to see if a promotion square has been selected
        else{
            if(checkPromotionSelected()){
                promotionBlock.applyPromotion(board);
                removeSelected();
                changeTurns();
            } else{
                promotionBlock.reset();
            }
            board.renderBoard();
        }
    }

    public boolean checkPromotionSelected(){
        for(int i = 0; i < promotionBlock.promotionSquares.length; i++){
            float x = promotionBlock.promotionSquares[i].promotionBoxX;
            float y = promotionBlock.promotionSquares[i].promotionBoxY;

            if(checkBounds(x, y)){
                PromotionBlock.registerPawnPromotion(promotionBlock.types[i], promotionBlock.prevPromoteSquare, promotionBlock.newPromoteSquare, boardSquares);
                return true;

            } 
        }

        return false;
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

    public void changeTurns(){ 
        moveCounter++;
        whiteToMove = !whiteToMove; 

        if(whiteToMove){
            whiteTimer.resumeTime();
            blackTimer.pauseTime();
        } else{ 
            blackTimer.resumeTime();
            whiteTimer.pauseTime();
        }
    }

    public void renderPossibleMoves(){
        if(checkSelected()){
            board.plotPotentialMoves(selected);
        }
    }

    public boolean isMoveMade(){
        if(checkSelected() && !PromotionBlock.pawnPromotionOptions){    
            boolean[] potentialMoves = board.getPotentialMoves();
            for(int i = 0; i < potentialMoves.length; i++){
                if(checkBounds(boardSquares[i].getX(), boardSquares[i].getY()) && potentialMoves[i]){
                    int selectedIndex = Arrays.asList(boardSquares).indexOf(selected);
                    if(Rules.getInstance().isInCheck()){
                        Rules.getInstance().checkResolved();
                    }

                    board.applyMove(i, selectedIndex);
                    if(!PromotionBlock.pawnPromotionOptions){
                        board.setSquares(boardSquares);
                        removeSelected();
                        changeTurns();

                    }
                    
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
        
        if(selected != null){
            selected.setSelected(false);
            selected = null;
        }

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

    public void renderPlayerTimes(){
        if(whiteTimer.updateTime){
            whiteTimer.render();
        }
        
        if(blackTimer.updateTime){
            blackTimer.render();
        }
    }

    public void draw() {
        renderPlayerTimes();
    }
}
