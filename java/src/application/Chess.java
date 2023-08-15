package application;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.Arrays;

import application.Pieces.Pawn;
import application.Pieces.PieceType;

class PromotionBlock{
    class PromotionSquare{
        float promotionBoxX;
        float promotionBoxY;

        public PromotionSquare(){
            this.promotionBoxX = 0;
            this.promotionBoxY = 0;
        }

    } // End inner class

    float boxW;
    PieceType[] types;
    String[] displayImages;
    boolean promotionDisplayed;
    boolean pawnPromotionOptions;
    private static PromotionBlock promotionInstance;
    PromotionSquare[] promotionSquares;
    float blockSpawnX;
    float blockSpawnY;
    int prevPromoteSquare;
    int newPromoteSquare;

    private PromotionBlock(float boxW){
        this.displayImages = new String[]{
            ChessSymbols.WHITE_CHESS_QUEEN_IMG, 
            ChessSymbols.WHITE_CHESS_KNIGHT_IMG, 
            ChessSymbols.WHITE_CHESS_ROOK_IMG,
            ChessSymbols.WHITE_CHESS_BISHOP_IMG
        };  

        this.types = new PieceType[]{PieceType.QUEEN, PieceType.KNIGHT, PieceType.ROOK, PieceType.BISHOP};
        this.boxW = boxW;

        this.promotionDisplayed = false;
        this.pawnPromotionOptions = false;
        this.promotionSquares = new PromotionSquare[4];
        for(int i = 0; i < promotionSquares.length; i++){
            promotionSquares[i] = new PromotionSquare();
        }
        this.blockSpawnX = 0;
        this.blockSpawnY = 0;
        this.prevPromoteSquare = -1;
        this.newPromoteSquare = -1;

    }

    public static PromotionBlock getInstance(float boxW){
        if(promotionInstance == null){
            promotionInstance = new PromotionBlock(boxW);
        }

        return promotionInstance;
    }

    public void promotionCompleted(){ promotionInstance = new PromotionBlock(boxW);}

}

public class Chess extends PApplet{
    Board board;
    Square[] boardSquares;
    Square selected;
    boolean whiteToMove;
    boolean TESTING = false;
    Rules rules;
    int moveCounter;
    PromotionBlock promotionBlock;
    
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
        promotionBlock = PromotionBlock.getInstance(board.getSquareW());
    }

    public void mouseReleased(){
        if(!promotionBlock.pawnPromotionOptions){
            checkIfPieceSelected();
            board.renderBoard();
            boardSquares = board.getSquares();
        }

        if(promotionBlock.pawnPromotionOptions){
            displayPawnPromotion();
        }

    }

    public void displayPawnPromotion(){
        float boxW = promotionBlock.boxW;

        // Display the promotion block
        if(!promotionBlock.promotionDisplayed){
            fill(255);
            stroke(0);

            PImage p;
            for(int i = 0; i < promotionBlock.types.length; i++){
                float x = promotionBlock.blockSpawnX;
                float y = promotionBlock.blockSpawnY + (i * boxW);
                promotionBlock.promotionSquares[i].promotionBoxX = x;
                promotionBlock.promotionSquares[i].promotionBoxY = y;

                square(x, y, boxW);
                p = loadImage(promotionBlock.displayImages[i]);
                image(p, x, y, boxW, boxW);
            }

            promotionBlock.promotionDisplayed = true;

        }

        // Check to see if a promotion square has been selected
        else{
            if(checkPromotionSelected()){
                // Reset everything
                promotionBlock.promotionCompleted();
                board.renderBoard();
                promotionBlock = PromotionBlock.getInstance(boxW);
                moveCounter++;
                System.out.println("COMPLETED");

            }
        }
    }

    public boolean checkPromotionSelected(){
        for(int i = 0; i < promotionBlock.promotionSquares.length; i++){
            float x = promotionBlock.promotionSquares[i].promotionBoxX;
            float y = promotionBlock.promotionSquares[i].promotionBoxY;


            if(checkBounds(x, y)){
                board.registerPawnPromotion(promotionBlock.types[i], promotionBlock.prevPromoteSquare, promotionBlock.newPromoteSquare);
                System.out.println("REGISTERING THE PROMOTION");
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
                            promotionBlock.pawnPromotionOptions = true;
                            promotionBlock.blockSpawnX = mouseX;
                            promotionBlock.blockSpawnY = mouseY;
                            promotionBlock.prevPromoteSquare = selectedIndex;
                            promotionBlock.newPromoteSquare = i;
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
